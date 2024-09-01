package elsayed;

import java.util.HashSet;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
/*
 * P.S: high score is 105 586, try to beat me
 */

public class CrazyHighway extends Application {

	//timer that causes code to repeat
	GameTimer timer = new GameTimer (elapsedTime -> updateGame(elapsedTime));

	//time until one of these can happen
	private double newCarTimer= 2;
	private double gunCooldown = 0;//you can start shooting whenever you want

	//score tracking
	public int score = 0;
	public Label scoreCounter = new Label("0");

	//Hash set keyboard
	public static HashSet<KeyCode> keyboard = new HashSet<KeyCode>();

	//scene and scene dimensions
	Scene scene;
	public static final double GAME_WIDTH = 450;
	public static final double GAME_HEIGHT = 600;

	//groups (game states)
	private boolean playing = false;
	Group titleScreen;
	Group gameScreen;
	Group gameOverScreen;

	//Sprites
	Sprite backgroundA = new Sprite (new Rectangle (GAME_WIDTH, GAME_HEIGHT), 0, 0, GAME_WIDTH, GAME_HEIGHT, "background.png");
	Sprite backgroundB = new Sprite (new Rectangle (GAME_WIDTH, GAME_HEIGHT), 0, -GAME_HEIGHT, GAME_WIDTH, GAME_HEIGHT, "background.png");
	Player player;
	private Group traffic = new Group ();
	private Group bullets = new Group ();

	//Music, I think loading the media player as a class variable is better for the program, otherwise it doesn't load sometimes
	MediaPlayer music;

	//font
	public static final Font SILLY_RACER = Font.loadFont("file:ResourcesElsayed/RacingSans.ttf", 40);

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("CRAZY HIGHWAY!");
		stage.setResizable(false);

		//set up the screens/different menus
		setUpTitleScreen();
		gameScreen = setUpGameScreen();

		scene = new Scene (titleScreen, GAME_WIDTH, GAME_HEIGHT);

		stage.setScene(scene);
		stage.show();

		//making media
		//weird how I need the whole path but still works, audio clip does not need it all
		String path = ("file:/" + System.getProperty("user.dir") + "/ResourcesElsayed/HighWayMusic.mp3").replace("\\", "/");
		Media media = new Media (path);

		//playing the music and having it set on loop
		music = new MediaPlayer (media);
		music.setCycleCount(MediaPlayer.INDEFINITE);
		music.play();

		//detect keyboard input
		scene.setOnKeyPressed(key -> keyPressed(key));
		scene.setOnKeyReleased(key -> keyReleased(key));
	}

	public static void main(String[] args) {
		launch();

	}
	/*
	 * what to do when the key is pressed
	 * adds it to the hash set
	 */
	public void keyPressed(KeyEvent key) {
		keyboard.add(key.getCode());
	}
	/*
	 * what to do when the user lets go of a key
	 * remove from hash set
	 */
	public void keyReleased(KeyEvent key) {
		keyboard.remove(key.getCode());
	}
	/*
	 * code that repeats over and over again because of the timer
	 */
	private void updateGame(double time) {

		/*
		 * update all Sprites, some sprites still move even when the game is over
		 * the background will continue moving, the traffic and the bullets
		 * player movement will be hindered
		 */
		updateBackgrounds(time);
		updateTraffic(time);
		updatePlayer(time);
		updateBullets(time);

		if (playing ==true) {//if the player actually is playing
			//score timer goes up
			updateScore(time);


			//check for collisions
			checkBulletCollisions();//bullets on traffic
			checkPlayerCollisions();//traffic on player
		}

	}
	/*
	 * updates the score counter
	 */
	public void updateScore(double elapsedTime) {

		//felt this was reasonable, 1000 every second
		//arbitrary and is just based on my preference
		score +=(int)(elapsedTime*1000);

		//update the scoreCounter text at the top
		scoreCounter.setText(Integer.toString(score));

	}
	/*
	 * checks to see if the player hits a car
	 */
	public void checkPlayerCollisions() {
		//for every car
		for (int i = 0; i<traffic.getChildren().size(); i++) {
			Traffic car = (Traffic)traffic.getChildren().get(i);//temp variable
			if (player.intersects(car)) {//check
				//add smoke and stuff to car and player
				car.dies();
				player.dies();
				
				//R.I.P
				gameOver();
			}
		}
	}
	/*
	 * ends the game
	 */
	public void gameOver() {
		//player is not playing anymore
		//timer is still going but now some actions don't happen
		//example: background can still move but player can't
		playing = false;
		
		//gameOverScreen is set up
		gameScreen.getChildren().add(setUpGameOverScreen());
	}
	/*
	 * checks to see if the bullets hit an enemy
	 */
	public void checkBulletCollisions() {
		//for every bullet
		for (int i =0; i<bullets.getChildren().size(); i++) {
			Sprite bullet = (Sprite)bullets.getChildren().get(i);//temp
			//for every car
			//game is checking if bullet intersects any of the cars
			for (int l = 0; l<traffic.getChildren().size(); l++) {
				Traffic car = (Traffic)traffic.getChildren().get(l);//temp

				//if there is a bullet and it intersects and the bullet is not hitting the car above the screen
				if (bullet != null && bullet.intersects(car) && bullet.getY()>-27) {
					
					//get rid of bullet
					bullets.getChildren().remove(i);
					
					/*
					 * one of the decisions I made with this game is having the cars still stay on screen
					 * after they get shot, they still act as an obstacle, but they are slower
					 * this can be helpful and get you out of tricky situations
					 * however, if you misuse it you can corner yourself into a slow death
					 */

					if (car.isDead() == false) {//if the car is not dead already, increase score
						score+=3000;
					}
					
					//add smoke to car
					car.dies();
				}
			}
		}
	}
	/*
	 * update backgrounds
	 */
	public void updateBackgrounds(double time) {
		//move backgrounds
		backgroundA.update(time);
		backgroundB.update(time);

		//backgrounds move to be above each other
		//*condition: if one goes off screen, it teleports above the other
		if (backgroundA.getY()>GAME_HEIGHT) {
			backgroundA.setY(backgroundB.getY()-GAME_HEIGHT);
		}
		if (backgroundB.getY()>GAME_HEIGHT) {
			backgroundB.setY(backgroundA.getY()-GAME_HEIGHT);
		}
	}
	/*
	 * updates bullets
	 */
	public void updateBullets(double elapsedTime) {
		//for every bullet
		for (int i =0; i<bullets.getChildren().size(); i++) {
			
			//temp bullet
			Sprite tempB = (Sprite)bullets.getChildren().get(i);
			
			//update that bad boy
			tempB.update(elapsedTime);
			
			//if bullet is off screen, remove it
			if (tempB.getY()<-27) {
				bullets.getChildren().remove(i);
				
			}
		}
	}
	/*
	 * update player
	 */
	public void updatePlayer(double time) {

		if (playing ==true) {//if game is going on
			
			//check for movement, otherwise velocity is 0
			player.setXVelocity(0);
			
			//if player is trying to press both keys, nothing happens
			if (keyboard.contains(KeyCode.LEFT) && keyboard.contains(KeyCode.RIGHT)) {
				player.setXVelocity(0);
			} //moving left
			else if (keyboard.contains(KeyCode.LEFT)) {
				player.setXVelocity(-300);
			}//moving right
			else if (keyboard.contains(KeyCode.RIGHT)) {
				player.setXVelocity(300);
			}

			//-------------------------add bullets
			//pretty great addition by me, you don't have to hold space bar for gun to cool down
			gunCooldown-=time;

			//cool down is over
			if (keyboard.contains(KeyCode.SPACE) && gunCooldown<0) {
				bullets.getChildren().add(player.newBullet());
				gunCooldown = 0.4;//cool down is set to 0.4 seconds
			}
			
		}// if game is not going on playing == false
		
		//player moves to center when game ends 
		else if (player.getX()>(GAME_WIDTH-30)/2){//if on right
			//move them left
			player.setXVelocity(-100);
			
		} else if (player.getX()<(GAME_WIDTH-50)/2) {//if on left
			//move them to the right
			player.setXVelocity(100);
			
		} else {//if the player is near the center
			player.setXVelocity(0); //don't move anymore
			player.setX((GAME_WIDTH-40)/2);//just move them to the center
		}
		//player doing player things, player can still move if game is over
		//just not through user control
		player.update(time);
	}
	/*
	 * updates all cars in traffic
	 */
	public void updateTraffic(double elapsedTime) {

		//for adding new cars
		if (playing ==true) {//if playing, new cars are added
			
			if (newCarTimer<0) {//if the cooldown for cars is up
				
				Traffic newCar = new Traffic ();//creates a random car
				
				newCar.setYVelocity(200*Math.random() + 300);//random velocity
				
				traffic.getChildren().add(newCar);// add to group of traffic
				
				newCarTimer = 0.1+Math.random();//timer change
				//timer has a minimum of 0.1, otherwise there's too much chaos
				
			} else {//timer goes down
				newCarTimer-=elapsedTime;
			}
		}
		//updating cars
		for (int i = 0; i<traffic.getChildren().size(); i++) {
			Traffic car = (Traffic)traffic.getChildren().get(i);
			car.update(elapsedTime);

			//check if a car is out of bounds, remove it if so
			if (car.getY()>GAME_HEIGHT) {
				traffic.getChildren().remove(car);
			}
		}
	}

	/*
	 * sets up the title screen
	 */
	public void setUpTitleScreen() {

		//background image (road)
		ImageView background = new ImageView (new Image ("file:ResourcesElsayed/background.png"));
		background.setPreserveRatio(false);
		background.setFitWidth(GAME_WIDTH);
		background.setFitHeight(GAME_HEIGHT);

		//start button
		Button startButton = new Button("START!");
		startButton.setPrefSize(200, 100);
		startButton.relocate((GAME_WIDTH-200)/2, 400);
		startButton.setFont(SILLY_RACER);
		startButton.setTextFill(Color.ORANGE);

		//add to group
		titleScreen = new Group(background, startButton);

		//start button changes root, starts timer, and sets playing to true
		startButton.setOnAction(e -> {
			scene.setRoot(gameScreen);
			timer.start();
			playing = true;});
	}
	/*
	 * sets up game screen
	 * for backgrounds there is no need for a hitbox
	 * background A is where the game starts, background B then comes on
	 */
	public Group setUpGameScreen() {
		
		//set the velocity of the backgrounds since they move
		backgroundA.setYVelocity(200);
		backgroundB.setYVelocity(200);

		//label that keeps track of score
		scoreCounter.setFont(SILLY_RACER);
		scoreCounter.setTextFill(Color.WHITE);

		//this is to center the score counter
		StackPane score = new StackPane(scoreCounter);
		score.setPrefSize(GAME_WIDTH, 0);

		//add the player to the group
		player = new Player (new Rectangle (40, 80), (GAME_WIDTH-40)/2, 440, 40, 80, "Player.png");
		
		//bullets and traffic are added now because children of those groups will be added during game play
		return new Group (backgroundA, backgroundB, player, traffic, bullets, score);
	}
	/*
	 * sets up game over screen
	 */
	public Group setUpGameOverScreen() {
		
		//game over label
		Label gameOverLabel = new Label ("GAME OVER!");
		gameOverLabel.setFont(SILLY_RACER);
		gameOverLabel.setTextFill(Color.WHITE);

		//button that allows you to play again
		Button playAgain = new Button("PLAY AGAIN");
		playAgain.setFont(SILLY_RACER);
		playAgain.relocate((GAME_WIDTH-300)/2, 350);
		playAgain.setPrefSize(300, 100);

		//button that takes you to the title
		Button toTitle = new Button("TO MENU");
		toTitle.setFont(SILLY_RACER);
		toTitle.relocate((GAME_WIDTH-300)/2, 470);
		toTitle.setPrefSize(300, 100);

		//center the gameOverLabel
		StackPane gameOver = new StackPane(gameOverLabel);
		gameOver.setPrefSize(GAME_WIDTH, GAME_HEIGHT);

		//add buttons and label to group
		Group gameOverScreen = new Group(gameOver, playAgain, toTitle);

		//on button press
		playAgain.setOnAction(play -> {
			startGame();
			//gets rid of all the extra text and labels from the gameOverScreen
			//and adds the regular gameScreen items
			gameScreen.getChildren().clear();
			gameScreen.getChildren().addAll(setUpGameScreen());
		});

		//takes you to the title
		toTitle.setOnAction(back -> {
			
			//go to title screen
			scene.setRoot(titleScreen);
			
			//reset the game screen and remove the gameOverScreen elements
			gameScreen.getChildren().clear();
			gameScreen.getChildren().addAll(setUpGameScreen());
			
			//reset score and counter
			score = 0;
			scoreCounter.setText("0");
			
			//remove smoke off player
			player.revive();
		});
		return gameOverScreen;//return the group
	}
	/*
	 * starts the game
	 */
	public void startGame() {
		player.revive();//remove smoke
		
		//reset score and score counter
		score = 0;
		scoreCounter.setText("0");
		
		playing = true;
	}
}

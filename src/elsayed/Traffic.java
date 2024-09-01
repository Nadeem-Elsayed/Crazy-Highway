package elsayed;

import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Traffic extends Sprite{
	
	//different types of cars
	public static final int SPY = 0;
	public static final int TAXI = 1;
	public static final int VAN = 2;
	
	//car can be dead or alive, add smoke if dead
	private boolean dead = false;
	
	//array of all the images for all the traffic types
	private static final Image[] IMAGES = {
			new Image ("file:ResourcesElsayed/car.png"),
			new Image ("file:ResourcesElsayed/taxi.png"),
			new Image ("file:ResourcesElsayed/Mini_van.png"),
	};
	
	//random number generator used to pick the type of car
	public static final Random RNG = new Random();
	
	//keep track of the type of car
	public int type = 0;

	//constructor for the traffic
	public Traffic() {
		super();
		
		//type is random
		type = RNG.nextInt(3);
		
		//graphic is based on type
		graphic = new ImageView (IMAGES[type]);
		
		//car types have a different hit box
		if (type == 0) {
			hitBox = new Rectangle (40,80);
			
		} else if (type == 1) {
			hitBox = new Rectangle (40,80);
			
		} else if (type == 2) {//van
			hitBox = new Rectangle (45,90);
		}
		
		//hit box
		hitBox.setFill(Color.TRANSPARENT);
		hitBox.setStroke(Color.TRANSPARENT);//set to green to show the hit box
		
		//set the traffic to be at the top of the screen at a random location
		relocate(100+Math.random()*(CrazyHighway.GAME_WIDTH-getGraphicWidth()-200-20), -90);
		this.getChildren().addAll(hitBox, graphic);// add elements
	}
	/*
	 * update method that gets repeated by the timer
	 */
	public void update(double elapsedTime) {
		
		//x and y values increase based on velocity
		x.set(x.get() + xVelocity*elapsedTime);
		y.set(y.get() + yVelocity*elapsedTime);
		
		//relocate based on x and y
		this.relocate(x.get(), y.get());
	}
	/*
	 * dies
	 */
	public void dies() {
		//if not already dead
		if (dead == false) {
			
			//add smoke
			getChildren().add(new Sprite (new Rectangle (0, 0), 0, 0, 80, 80, "fireSmoke.png"));
			
			//slow down to match the speed of the road
			yVelocity = 200;
			
			//now it is dead
			dead = true;
		}
	}
	/*
	 * checks if car is dead
	 */
	public boolean isDead() {
		return dead;
	}
}

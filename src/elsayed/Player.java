package elsayed;

import javafx.scene.shape.Rectangle;

public class Player extends Sprite{

	//smoke for when the player is dead
	private Sprite smoke =new Sprite (new Rectangle (0, 0), 0, 0, 80, 80, "playerSmoke.png");
	
	//keep track whether or not the player is dead
	private boolean dead = false;	
	
	public Player(Rectangle hitBox, double posX, double posY, double width, double height, String imagePath) {
		super(hitBox, posX, posY, width, height, imagePath);
	}
	
	/*
	 * repeated over and over again by timer
	 */
	public void update (double elapsedTime) {	
		
		//only need to move left and right since player can not move up or down
		x.set(x.get()+ xVelocity*elapsedTime);
		
		//-----------max and minimum values, player can't go off road
		// minimum
		if (x.get()<100) {
			x.set(100);
		}
		// max
		if (x.get()+width>CrazyHighway.GAME_WIDTH-100) {
			x.set(CrazyHighway.GAME_WIDTH-width-100);
		}
		
		//correct the player if they move past max or minimum
		relocate(x.get() , y.get());
	}
	/*
	 * creates a bullet
	 */
	public Sprite newBullet() {
		//bullet position is based on player position, width, height, and hit box are based on image dimensions
		Sprite bullet = new Sprite(new Rectangle (14, 27), x.get()+(width)/2-27/4, 440, 14, 27, "bullet.png");
		
		//bullet moves up quickly
		bullet.setYVelocity(-500);
		
		//return
		return bullet;
	}
	/*
	 * kills the player
	 */
	public void dies() {
		//if the player is not dead already
		if (dead == false) {
			
			//add smoke
			getChildren().add(smoke);
			
			//player is dead
			dead = true;
			}
	}
	/*
	 * revive the player, remove smoke and the player is not dead anymore
	 */
	public void revive() {
		//if already dead
		if (dead == true) {
			//not dead anymore
			dead = false;
			
			//remove all elements of this group
			//add back everything except the smoke
			getChildren().clear();
			getChildren().addAll(hitBox, graphic);
		}
	}
}

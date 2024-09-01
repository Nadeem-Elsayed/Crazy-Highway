package elsayed;
import javafx.scene.shape.Rectangle;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

//same as my Soccer Kick Ups Sprite class
public class Sprite extends Group{

//x and y coordinates for movement
protected DoubleProperty x = new SimpleDoubleProperty();
protected DoubleProperty y = new SimpleDoubleProperty();

//width and height
protected double width;
protected double height;

//hit box
protected Rectangle hitBox;

//image view
protected ImageView graphic;

//used to track x and y coordinates
private Translate location = new Translate ();

//velocities and accelerations to have fun with movement
protected double xVelocity;
protected double yVelocity;

	
// boring usual constructor used for traffic, no parameters

	public Sprite () {
		//translate transformation
		location.xProperty().bind(x);
		location.yProperty().bind(y);
		this.getTransforms().add(location);
	}
	/*
	 * cool constructor, all the parameters, used for player
	 */
	public Sprite(Rectangle hitBox, double posX, double posY, double width, double height, String imagePath) {
		//position set
		x.set(posX);
		y.set(posY);
		
		//dimensions
		this.width = width;
		this.height = height;
		
		//hit box
		this.hitBox = hitBox;
		this.hitBox.setFill(Color.TRANSPARENT);
		this.hitBox.setStroke(Color.TRANSPARENT);//set to green to check for hit boxes
		
		//image view of the sprite
		Image graphicImage = new Image ("file:ResourcesElsayed/" + imagePath);
		graphic = new ImageView (graphicImage);
		graphic.setPreserveRatio(false);
		
		//resize the image
		graphic.setFitWidth(width);
		graphic.setFitHeight(height);
		
		//apply transformation
		location.xProperty().bind(x);
		location.yProperty().bind(y);
		getTransforms().add(location);
		
		getChildren().addAll(this.hitBox, graphic);
	}
	/*
	 * ------------------all the get methods
	 */
	/*
	 * get x
	 */
	public double getX () {
		return x.get();
	}
	/*
	 * get y
	 */
	public double getY () {
		return y.get();
	}
	/*
	 * get the hit box
	 */
	public Rectangle getHitBox () {
		return hitBox;
	}
	/*
	 * get the image view/graphic
	 */
	public ImageView getGraphic () {
		return graphic;
	}
	/*
	 * get the x velocity
	 */
	public double getXVelocity() {
		return xVelocity;
	}
	/*
	 * get the y velocity
	 */
	public double getYVelocity () {
		return yVelocity;
	}
	/*
	 * get the width of the graphic, I'm too lazy to used hit box sometimes so I just used this for the soccer balls
	 * this is forgivable though because the hit box of the balls is the same as the bounds of the image view itself
	 */
	public double getGraphicWidth() {
		return graphic.getFitWidth();
	}
	/*
	 * get the graphic height
	 */
	public double getGraphicHeight() {
		return graphic.getFitHeight();
	}
	/*
	 * pretty much updates where the computer thinks the hitBox is
	 * and then returns the bounds from that
	 */
	public Bounds getBoundary () {
		Transform parentTransform = this.getLocalToParentTransform();
		return parentTransform.transform(hitBox.getBoundsInLocal());
	}
	/*
	 * -------------------------all the set methods
	 */
	/*
	 * set x
	 */
	public void setX (double x) {
		this.x.set(x);
	}
	/*
	 * set y
	 */
	public void setY (double y) {
		this.y.set(y);
	}
	/*
	 * set width of sprite
	 */
	public void setWidth (double width) {
		this.width = width;
	}
	/*
	 * set the height of the sprite
	 */
	public void setHeight (double height) {
		this.height = height;
	}
	/*
	 * set x velocity
	 */
	public void setXVelocity (double velX) {
		xVelocity= velX;
	}
	/*
	 * set y velocity
	 */
	public void setYVelocity (double velY) {
		yVelocity= velY;
	}
	/*
	 * checks if the Sprites intersect
	 */
	public boolean intersects (Sprite other) {
		return this.getBoundary().intersects(other.getBoundary());
	}
	/*
	 * overrides the Group's relocate method, sets the x and y variables
	 */
	public void relocate(double x, double y) {
		this.x.set(x);
		this.y.set(y);
	}
	/*
	 * updates the position of the sprite
	 */
	public void update (double elapsedTime) {
		//set the x and y coordinates
		x.set(x.get()+xVelocity*elapsedTime);
		y.set(y.get()+yVelocity*elapsedTime);
	}
}

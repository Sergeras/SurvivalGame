package application;

import javafx.geometry.Point2D;

import javafx.scene.Node;

public class GameObject {
	private Node view; // Used for collision detection and other animation features
	private Point2D velocity = new Point2D(0,0); // For setting the direction the object will move
	private int rotationSpeed = 5; // rotation speed attribute for object just to have easy way to control it
	private boolean alive = true; // for object to know if is dead or alive and remove if dead
	
	public GameObject(Node view) {
		this.view = view;
	}
	//for updating the position, movement of the object
	public void update() {
		view.setTranslateX(view.getTranslateX() + velocity.getX());
		view.setTranslateY(view.getTranslateY() + velocity.getY());
	}

	public Node getView() {
		return view;
	}

	public void setView(Node view) {
		this.view = view;
	}

	public Point2D getVelocity() {
		return velocity;
	}

	public void setVelocity(Point2D velocity) {
		this.velocity = velocity;
	}

	public boolean isAlive() {
		return alive;
	}
	public boolean isDead() {
		return !alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	public void setDead(boolean alive) {
		this.alive = !alive;
	}
	
	public double getRotate() {
		return view.getRotate();
	}
	
	public void moveLeft() {
		//this.velocity = new Point2D(velocity.getX() -1,velocity.getY());
	}
	public void moveRight() {
		//this.velocity = new Point2D(velocity.getX() +1,velocity.getY());
	}
	public void moveUp() {
		//this.velocity = new Point2D(velocity.getX() ,velocity.getY()-1);
	}
	public void moveDown() {
		//this.velocity = new Point2D(velocity.getX() ,velocity.getY()+1);
	}
	// to be used later on in development for rotating object
	public void rotateRight() {
		view.setRotate(view.getRotate() + rotationSpeed);
		setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())), Math.sin(Math.toRadians(getRotate()))));
	}
	
	public void rotateLeft() {
		view.setRotate(view.getRotate() - rotationSpeed);
		setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())), Math.sin(Math.toRadians(getRotate()))));
	}
	// check for collision, comparing the 2 objects boundries
	public boolean isColliding (GameObject other) {
		return getView().getBoundsInParent().intersects(other.getView().getBoundsInParent());
	}
	
}//End Game object


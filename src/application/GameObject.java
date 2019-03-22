package application;

import javafx.geometry.Point2D;

import javafx.scene.Node;

public class GameObject {
	private Node view;
	private Point2D velocity = new Point2D(0,0);
	private int rotationSpeed = 5;
	
	private boolean alive = true;
	
	public GameObject(Node view) {
		this.view = view;
	}
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
	
	public void rotateRight() {
		view.setRotate(view.getRotate() + rotationSpeed);
		setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())), Math.sin(Math.toRadians(getRotate()))));
	}
	
	public void rotateLeft() {
		view.setRotate(view.getRotate() - rotationSpeed);
		setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())), Math.sin(Math.toRadians(getRotate()))));
	}
	
	public boolean isColliding (GameObject other) {
		return getView().getBoundsInParent().intersects(other.getView().getBoundsInParent());
	}
	
	
}//End Game object


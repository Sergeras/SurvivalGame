/*
 * Simple game to learn principles. Graphics are basic as they are not important at this stage. Its survival
 * game where you try to kill enemies before they touch you. The more you kill the harder the game gets.
 * Simple controls of WASD for moving and left mouse button for shooting.
 */
package application;
	
import java.util.List;


import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Main extends Application {
	static private int height = 600;
	static private int width = 600;
	static private int enemiesKilled = 0; // counter for enemies killed
	private Pane root;
	private GameObject player; // player object
	private List<GameObject> enemies = new ArrayList<>(); //holds all the enemies
	private List<GameObject> bullets = new ArrayList<>(); //holds all the bullets
	// score and timer shower, had to be done outside of body
	Text scoreBoard = new Text(0,0, ("Enemies Killed " + enemiesKilled));
	final long starMiliTime = System.currentTimeMillis();
	long currentTime = System.currentTimeMillis();
	Text timer = new Text(100,100,"Survived for " +(currentTime - starMiliTime) + "seconds" );
	// function for content creation thats called in start, just to make the code easier to read
	private Parent createContent() {
		root = new Pane();
		root.setPrefSize(width,height);
		// ensuring they are always 0 and 1 position
		root.getChildren().add(scoreBoard);
		root.getChildren().add(timer);
		//player adding and position
		player = new Player();
		addGameObject(player, width/2, height/2);
		
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				onUpdate();
			}
		};
		timer.start();
		return root;
	}
	
	private void addGameObject(GameObject object, double x, double y) {
		object.getView().setTranslateX(x);
		object.getView().setTranslateY(y);
		root.getChildren().add(object.getView());
	}
	
	private void addBullet (GameObject bullet , double x, double y) {
		bullets.add(bullet);
		addGameObject(bullet, x, y);
	}
	
	private void addEnemy(GameObject enemy, double x, double y) {
		enemies.add(enemy);
		addGameObject(enemy, x, y);
	}
	/*player with overriden controls from super class,
	TODO replace circle with graphic and then add method under update that will rotate the player towards
	TODO the mouse*/
	private static class Player extends GameObject{
		public Player() {
			super(new Circle(4,5,15, Color.MIDNIGHTBLUE) );
		}
		@Override
		public void moveLeft() {
			super.setVelocity(new Point2D(super.getVelocity().getX()-5, super.getVelocity().getY()).normalize()); 
			}
		@Override
		public void moveRight() {
			super.setVelocity(new Point2D(super.getVelocity().getX()+5, super.getVelocity().getY()).normalize());
		}
		@Override
		public void moveUp() {
			super.setVelocity(new Point2D(super.getVelocity().getX(), super.getVelocity().getY()-5).normalize());
		}
		@Override
		public void moveDown() {
			super.setVelocity(new Point2D(super.getVelocity().getX(), super.getVelocity().getY()+5).normalize());
		}
	}
	
	private static class Enemy extends GameObject{
		public Enemy() {
			super(new Circle(8,8,8, Color.RED) );
		}
	}
	
	private static class Bullet extends GameObject{
		public Bullet() {
			super(new Circle(3,3,3, Color.YELLOW) );
		}
	}
	
	
	// main function for graphics
	private void onUpdate() {
		// score board, just replacing the old one with new 
		Text scoreBoard = new Text(0,20, ("Enemies Killed " + enemiesKilled));
		scoreBoard.setFill(Color.BLACK);
		scoreBoard.setFont(Font.font(20));
		root.getChildren().set(0, scoreBoard);
		// stop the app when the player is dead, and also while running it shows time how long you are alive		
		if (player.isDead()) {
			Text text = new Text(width/2-100,height/2,"Game Over");
			text.setFill(Color.RED);
			text.setFont(Font.font(null, FontWeight.BOLD,40));
			root.getChildren().add(text);
			return;
		} else {
			// timer
			currentTime = System.currentTimeMillis();
			Text timer = new Text(200,20,"Survived for " +(currentTime - starMiliTime)/1000 + "seconds" );
			timer.setFill(Color.BLACK);
			timer.setFont(Font.font(20));
			root.getChildren().set(1, timer);
		}
		
		player.update();
		
		//boundries for the player
		if (player.getView().getTranslateX() < 10) 
		{player.getView().setTranslateX(10);}
		else if (player.getView().getTranslateX() >= (root.getPrefWidth() -18 )) 
		{player.getView().setTranslateX(root.getPrefWidth() -18);}
		else if (player.getView().getTranslateY() <= 10 ) 
		{player.getView().setTranslateY(10);}
		else if (player.getView().getTranslateY() >= (root.getPrefHeight() -20 ) ) 
		{player.getView().setTranslateY(root.getPrefHeight()-20);}
	
		//updating positions of bullets
		for(GameObject bullet: bullets) {
			bullet.update();
		}
		/*//updating positions of enemies, first inner loop checks for positions of enemy and bullet
		 * and they match they are both set as dead and the counter for kills go up by 1
		*/
		for (GameObject enemy: enemies) {
			for (GameObject bullet: bullets) {
				if (bullet.isColliding(enemy)) {
					bullet.setDead(true);
					enemy.setDead(true);
					root.getChildren().removeAll(bullet.getView(), enemy.getView());
					enemiesKilled++;
				}
			}
			// guiding system for enemies, its pretty much is getting to players position and then the speed
			// of enemies is adjusted
			if (player.isColliding(enemy)){player.setDead(true);}
			enemy.setVelocity(new Point2D(player.getView().getTranslateX()-enemy.getView().getTranslateX()
					,player.getView().getTranslateY()- enemy.getView().getTranslateY())
					.normalize().multiply(0.5));
			enemy.update();
		}
		// removing the enemies and the bullets which are dead
		bullets.removeIf(GameObject::isDead);
		enemies.removeIf(GameObject::isDead);
		/* random generator for enemies, it goes beyond the minimal spawning rate and changes 
		 * to faster spawn rate according to kill count. Its divided to 4 sides for top,bottom,left and right
		*/
		if (Math.random() < enemiesKilled * 0.001 || Math.random() < 0.01) {
			if (Math.random() < 0.25) {
				addEnemy(new Enemy(), 0, Math.random() * root.getPrefHeight());
			} else if (Math.random() < 0.5) {
				addEnemy(new Enemy(), width-16, Math.random() * root.getPrefHeight());
			} else if (Math.random() < 0.75) {
				addEnemy(new Enemy(), Math.random() * root.getPrefWidth(), 0);
			} else if (Math.random() <= 1) {
				addEnemy(new Enemy(), Math.random() * root.getPrefWidth(), height-16);
			}
		}
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		stage.setScene(new Scene(createContent()));
		//controls for movement
		stage.getScene().setOnKeyPressed( e ->{
			if (e.getCode() == KeyCode.A) {
				player.moveLeft();
			} else if (e.getCode() == KeyCode.D) {
				player.moveRight();
			} else if (e.getCode() == KeyCode.W) {
				player.moveUp();
			} else if (e.getCode() == KeyCode.S) {
				player.moveDown();
			} 
		});
		//controls for shooting, 
		stage.getScene().setOnMouseClicked(e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				Bullet bullet = new Bullet();
				// get position of the mouse, then - player position allow to shoot where I aim with mouse			
				bullet.setVelocity(new Point2D((e.getX()-player.getView().getTranslateX())
						,(e.getY()-player.getView().getTranslateY()))
						.normalize().multiply(3));
				addBullet(bullet, player.getView().getTranslateX(), player.getView().getTranslateY());
			}
		});
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

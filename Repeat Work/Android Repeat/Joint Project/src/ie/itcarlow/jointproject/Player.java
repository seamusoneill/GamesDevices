package ie.itcarlow.jointproject;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;

public class Player extends AnimatedSprite{

	private Vector2 m_velocity;
	private int lives = 3;
	
	//Constructor
	 public Player(float pX, float pY, VertexBufferObjectManager vbo, Camera camera){
		 
		 super(pX, pY, ResourcesManager.getInstance().player2_region, vbo);
	 }
	 
	public String getUsername() {
		return "Player 1";
	}
	
	public void setVelocity(Vector2 vel)
	{
		m_velocity = vel;
	}
	
	public int getLives(){
		return lives;
	}
	
	public void setLives(int l)
	{
		lives = l;
	}

}

package ie.itcarlow.jumpinggame;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import ie.itcarlow.jumpinggame.ResourcesManager;

public abstract class Player extends AnimatedSprite{

	//Variables
	private Body body;
	
	private boolean canRun = false;
	private boolean onPlatform = false;
	
	//Constructor
	 public Player(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld){
		 
		 super(pX, pY, ResourcesManager.getInstance().player_region, vbo);
		 createPhysics(camera,physicsWorld);
		 camera.setChaseEntity(this); //Makes the camera follow the player
	 }
	 /**
	  * What we just did there? We initialized players body, set user data so we can latter recognise this body in certain events
	  *  (such as contact between bodies detection), we also set fixed body rotation,
	  *   so body will not rotate. Latter we registered physics connector
	  *    (so player`s sprite, will automatically update its position, following body updates) 
	  *    inside we override onUpdate() method, and used camera onUpdate method,
	  *     it helps to reduce camera "jitering" effect while moving.
	  *      On the end, we created code checking player`s Y coordinate, 
	  *      if player`s Y is less than 0, onDie will be executed. 
	  *       We also check if canRun boolean returns true, and if yes we set linear velocity
	  *        on the X axis to stimulate run.

	  * @param camera
	  * @param physicsWorld
	  */
	 private void createPhysics(final Camera camera, PhysicsWorld physicsWorld){
		 
		 body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		 
		 body.setUserData("player");
		 body.setFixedRotation(true);
		 
		    physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false){
			 @Override
			 public void onUpdate(float pSecondsElapsed){
				 
				 super.onUpdate(pSecondsElapsed);
				 camera.onUpdate(0.1f);
				 
				 if (getY()<=0 || getY() >= 800){
					 onDie();
				 }
				 if(canRun){
					 body.setLinearVelocity(new Vector2(5,body.getLinearVelocity().y));
				 }
			 }
		 });
	 }
	 
	 public void setOnPlatform(boolean b){
		 onPlatform = b;
	 }
	 
	 //Starts the game
	 public void setRunning()
	 {
	     canRun = true;
	     
	     final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100 }; //The length of each frame of animation in ms
	     
	     animate(PLAYER_ANIMATE, 0, 2, true);
	 }
	 
	 public void flipGravity(PhysicsWorld physicsWorld)
	 {
		 if (onPlatform == false) 
		    {
		        return; 
		    }
		 ResourcesManager.getInstance().flipEffect.play();
			physicsWorld.setGravity(new Vector2 (0 , -physicsWorld.getGravity().y)); //Flip Gravity
			if (this.isFlippedVertical() == false){
				this.setFlippedVertical(true);
			}
			else if(this.isFlippedVertical() == true){
				this.setFlippedVertical(false);
			}
	 }
	 
	 //Call when the player dies
	 public abstract void onDie();
}

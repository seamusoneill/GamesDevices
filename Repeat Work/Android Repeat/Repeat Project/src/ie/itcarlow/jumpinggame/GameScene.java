package ie.itcarlow.jumpinggame;

import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.xml.sax.Attributes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

import ie.itcarlow.jumpinggame.BaseScene;
import ie.itcarlow.jumpinggame.SceneManager.SceneType;

public class GameScene extends BaseScene implements IOnSceneTouchListener {

	private HUD gameHUD;
	private Text scoreText;
	private static int score;
	private Text gameOverText;
	private boolean gameOverDisplayed = false;
	
	private boolean firsttouch = false;
	
	private Player player;
	private PhysicsWorld physicsWorld;
	
	//new constructor for game scene to allow levels
	GameScene (int levelID){
		this.resourcesManager = ResourcesManager.getInstance();
		this.engine = resourcesManager.engine;
        this.activity = resourcesManager.activity;
        this.vbom = resourcesManager.vbom;
        this.camera = resourcesManager.camera;
        createScene(levelID);
	}
	
	public void createScene(int levelID){
        //reset score for new game and don't start music again for 2nd level
        if (levelID == 1){
        	score = 0;
        	ResourcesManager.backgroundTheme.play();
        	ResourcesManager.backgroundTheme.setLooping(true);
        }
        
		createBackground();
		createHUD();
		createPhysics();
		loadLevel(levelID);
		if (levelID > 1){
		     player.setColor(40,200,40);  //for the marks, turns him pink
		}
		createGameOverText(); 	
		
		
		setOnSceneTouchListener(this);
	}
	
	@Override	//This method not used
	public void createScene() {
		createBackground();
		createHUD();
		createPhysics();
		loadLevel(1);
		createGameOverText();
		setOnSceneTouchListener(this);
		}
	


	@Override
	public void onBackKeyPressed() {
	    SceneManager.getInstance().loadMenuScene(engine);
	    ResourcesManager.backgroundTheme.stop();
	    ResourcesManager.backgroundTheme = null;
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene() {
		camera.setHUD(null);
		camera.setCenter(400,240);
		camera.setChaseEntity(null);//Fixes glitch where camera on menu is off.
	}
	
	private void createBackground(){
		attachChild(new Sprite(400,400, resourcesManager.game_background_region, vbom)
		{
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera)
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		});
		attachChild(new Sprite(1200,400, resourcesManager.game_background_region, vbom)
		{
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera)
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		});
	}
	
	private void createHUD(){
		gameHUD = new HUD();
		
		//All the characters that will be used in score are loaded now,
		//they will not be used instantly but will remain in memory for faster updating.
		scoreText = new Text(20,420,resourcesManager.font,"Score: 0123456879", new TextOptions(HorizontalAlign.LEFT), vbom);
		scoreText.setAnchorCenter(0,0);
		scoreText.setText("Score:" + score);
		gameHUD.attachChild(scoreText);
		
		camera.setHUD(gameHUD);
	}
	
	private void createGameOverText(){
		gameOverText = new Text(0,0, resourcesManager.font, "Game Over!", vbom);
	}
	
	private void setGameOver()
	{
	    camera.setChaseEntity(null);
	    gameOverText.setPosition(camera.getCenterX(), camera.getCenterY());
	    attachChild(gameOverText);
	    gameOverDisplayed = true;
	}
	
	public void addToScore(){
		if (!gameOverDisplayed && firsttouch){
			score++;
			scoreText.setText("Score:" + score);
		}
	}
	
	private void createPhysics(){
		physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -17),false);
		physicsWorld.setContactListener(contactListener());
		registerUpdateHandler(physicsWorld);
	}
	
	//LEVEL LOADER
	
	//Variables needed
	private static final String TAG_ENTITY = "entity";
	private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
	private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE ="type";
	
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM = "platform";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GOAL = "goal";
	
	private void loadLevel(final int levelID){ //Pass in the ID of the level you wish to load
	
		final SimpleLevelLoader levelLoader = new SimpleLevelLoader(vbom);
		
		final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f); // variables Density,elasticity,friction
		
		/**
		 * TODO: Learn and delete this:
		 * "We return "mother entity" for all entities loaded from xml file. In another words, entity
		 *  where all loaded entities are going to be attached to. As you can conclude,
		 *   we should return our game scene (like we do in the code below)"*/
		
		levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL){
			
			public IEntity onLoadEntity(final String pEntityName,final IEntity pParent, final Attributes pAttributes, 
					final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException{
				
				final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
				final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
				
				camera.setBounds(0,0,width,height); //setting camera bounds
				camera.setBoundsEnabled(true);
				
				return GameScene.this;
			}	
		});
		
		//Parses our xml file 
		levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(TAG_ENTITY){	
			
			public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, 
					final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException {
				final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
				final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
				final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);
				
				final Sprite levelObject;
				if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM)){
					
		                levelObject = new Sprite(x, y, resourcesManager.platform_region, vbom);
		                PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF).setUserData("platform");
		        }
				else if(type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER)){
					player = new Player(x,y,vbom,camera,physicsWorld)
					{
						@Override
						public void onDie()
						{
							if (!gameOverDisplayed)
							{
								setGameOver();
								firsttouch = false;
							}
						}
					};
					
					levelObject = player;
				}
				else if(type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GOAL)){
					levelObject =  new Sprite(x, y, resourcesManager.goal_region, vbom){
						
						@Override
						protected void onManagedUpdate(float pSecondsElapsed){
						addToScore();
							super.onManagedUpdate(pSecondsElapsed);
							if (player.collidesWith(this)){
								disposeScene();//Remove the previous gamescene
								SceneManager.getInstance().loadGameScene(engine, 2); //Keeps repeating level 2 as their are no other levels.
							}
						}	
					};
					levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1,1,1.3f)));
				}
				else{
					throw new IllegalArgumentException();	
				}
				
				
				levelObject.setCullingEnabled(true);
				return levelObject;
			}
		});
		
		levelLoader.loadLevelFromAsset(activity.getAssets(), "level/" + levelID + ".lvl");
	}
	
	//END LEVEL LOADER
	
	//Create contact listener for physics world that determines if player is in contact with platform
	private ContactListener contactListener(){
		
		ContactListener contactListener = new ContactListener(){
			public void beginContact(Contact contact){
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();
				
				if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null){
					
					if (x2.getBody().getUserData().equals("player")){
						player.setOnPlatform(true);
					}
				}
			}

			@Override
			public void endContact(Contact contact) {
				
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();
				
				if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null){
					if (x2.getBody().getUserData().equals("player")){
						player.setOnPlatform(false);
					}
				}
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}
		};
		
		return contactListener;
	}
	//Controls
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		
		if (pSceneTouchEvent.isActionDown())
	    {	//First Touch begins the game
			if(!firsttouch){
				firsttouch = true;
				player.setRunning();
			}
			else{
				player.flipGravity(physicsWorld);
			};
	    }
		return false;
	}
}

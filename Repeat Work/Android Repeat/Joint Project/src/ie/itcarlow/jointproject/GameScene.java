package ie.itcarlow.jointproject;

import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
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

import ie.itcarlow.jointproject.SceneManager.SceneType;
import ie.itcarlow.jointproject.BaseScene;

public class GameScene extends BaseScene implements IOnAreaTouchListener{

	private HUD gameHUD;
	private Text player1Text;
	private Text player2Text;
	
	PhysicsWorld physicsWorld;
	private Player player;
	private Obstacle[] obstacles;
	
	GameScene(){
		this.resourcesManager = ResourcesManager.getInstance();
		this.engine = resourcesManager.engine;
        this.activity = resourcesManager.activity;
        this.vbom = resourcesManager.vbom;
        this.camera = resourcesManager.camera;
        createScene();
	}
	
	@Override
	public void createScene() {
		obstacles = new Obstacle[40];
		for (int i = 0; i < obstacles.length; i++)
		{final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);
			obstacles[i] = new Obstacle(10*i,10*i,ResourcesManager.getInstance().obstacle_region,vbom);
			// PhysicsFactory.createBoxBody(physicsWorld, obstacles[i], BodyType.DynamicBody, FIXTURE_DEF);       
		}
		player = new Player(0, 0, vbom, camera);
		createBackground();
		createHUD();
		setOnAreaTouchListener(this);
	}

	private void createHUD() {
		gameHUD = new HUD();
		
		//All the characters that will be used in score are loaded now,
		//they will not be used instantly but will remain in memory for faster updating.
		player1Text = new Text(20,420,resourcesManager.font,"Score: 0123456879", new TextOptions(HorizontalAlign.LEFT), vbom);
		player1Text.setAnchorCenter(0,0);
		player1Text.setText(player.getUsername());
		gameHUD.attachChild(player1Text);
		
		camera.setHUD(gameHUD);
	}

	private void createBackground() {
		attachChild(new Sprite(400,240, resourcesManager.game_background_region, vbom)
		{
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera)
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		});
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadMenuScene(engine);
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
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed)
	{
	
		super.onManagedUpdate(pSecondsElapsed);
		for(int i = 0; i < obstacles.length; i++){
			
			if (player.collidesWith(obstacles[i]))
			{
				player.setLives(player.getLives()- 1);
			}
		}
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		if (pSceneTouchEvent.isActionDown())
		{
		//	player.setVelocity(new Vector2(pTouchAreaLocalX, pTouchAreaLocalY));
		}
		return false;
	}

}

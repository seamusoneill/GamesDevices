package ie.itcarlow.jointproject;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.app.Activity;

import ie.itcarlow.jointproject.ResourcesManager;
import ie.itcarlow.jointproject.SceneManager.SceneType;

public abstract class BaseScene extends Scene{
	
	//Variables
	
	protected Engine engine;
	protected Activity activity;
	protected ResourcesManager resourcesManager;
	protected VertexBufferObjectManager vbom;
	protected Camera camera;
	
	//Constructor
	public BaseScene(){
		
		this.resourcesManager = ResourcesManager.getInstance();
		this.engine = resourcesManager.engine;
        this.activity = resourcesManager.activity;
        this.vbom = resourcesManager.vbom;
        this.camera = resourcesManager.camera;
        createScene();
	}
	
	public abstract void createScene();
	
	public abstract void onBackKeyPressed();
	
	public abstract SceneType getSceneType();
	
	public abstract void disposeScene();
}

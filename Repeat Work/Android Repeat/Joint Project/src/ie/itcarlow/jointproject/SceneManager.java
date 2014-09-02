package ie.itcarlow.jointproject;

import ie.itcarlow.jointproject.BaseScene;
import ie.itcarlow.jointproject.LoadingScene;
import ie.itcarlow.jointproject.MainMenuScene;
import ie.itcarlow.jointproject.SplashScene;
import ie.itcarlow.jointproject.GameScene;
import ie.itcarlow.jointproject.UsernameMenuScene;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

public class SceneManager {


	//Scenes
	
	private BaseScene splashScene;
	private BaseScene menuScene;
	private BaseScene usernameMenuScene;
	private BaseScene gameScene;
	private BaseScene loadingScene;
	
	//Variables	
	private static final SceneManager INSTANCE = new SceneManager();
	
	private SceneType currentSceneType = SceneType.SCENE_SPLASH;
	
	private BaseScene currentScene;
	
	private Engine engine = ResourcesManager.getInstance().engine;
	
	public enum SceneType{
		SCENE_SPLASH,
		SCENE_MENU,
		SCENE_GAME,
		SCENE_LOADING,
		SCENE_USERNAME_MENU,
	}
	
	public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback){
		ResourcesManager.getInstance().loadSplashScreen();
		splashScene = new SplashScene();
		currentScene= splashScene;
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
	}
	
	public void disposeSplashScene(){
		ResourcesManager.getInstance().unloadSplashScreen();
		splashScene.disposeScene();
		splashScene = null;
	}
	
	public void createMenuScene(){
		ResourcesManager.getInstance().loadMenuResources();
		menuScene = new MainMenuScene();
		loadingScene = new LoadingScene();
		SceneManager.getInstance().setScene(menuScene);
		disposeSplashScene();
	}
	public void createUsernameScene(){
		ResourcesManager.getInstance().loadUsernameMenuResources();
		usernameMenuScene = new UsernameMenuScene();
		loadingScene = new LoadingScene();
		SceneManager.getInstance().setScene(usernameMenuScene);
		ResourcesManager.getInstance().unloadMenuTextures();
	}
	
	public void loadGameScene(final Engine mEngine){
		setScene(loadingScene);
		ResourcesManager.getInstance().unloadMenuTextures();
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
			
            public void onTimePassed(final TimerHandler pTimerHandler) {
            	
            	mEngine.unregisterUpdateHandler(pTimerHandler);
            	ResourcesManager.getInstance().loadGameResources();
        		gameScene = new GameScene();
        		setScene(gameScene);
            }
		}));
	}
	public void loadUsernameMenuScene(final Engine mEngine){
		setScene(loadingScene);
		
		ResourcesManager.getInstance().unloadMenuTextures();
		
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback(){
			
			public void onTimePassed (final TimerHandler pTimerHandler){
			ResourcesManager.getInstance().loadUsernameMenuTextures();
			usernameMenuScene = new UsernameMenuScene();
			setScene(usernameMenuScene);
			}
		}));
	}
	
	public void loadMenuScene(final Engine mEngine){
		setScene(loadingScene);
		gameScene.disposeScene();
		ResourcesManager.getInstance().unloadGameTextures();
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback(){
			
			public void onTimePassed(final TimerHandler pTimerHandler){
			ResourcesManager.getInstance().loadMenuTextures();
			setScene(menuScene);
			}
		}));
	}
	public void setScene(BaseScene scene){
		engine.setScene(scene);
		currentScene = scene;
		currentSceneType = scene.getSceneType();
	}
	
	public void setScene(SceneType sceneType){
		switch (sceneType)
		{
			case SCENE_MENU:
				setScene(menuScene);
				break;
			case SCENE_GAME:
				setScene(gameScene);
				break;
			case SCENE_SPLASH:
				setScene(splashScene);
				break;
			case SCENE_LOADING:
				setScene(loadingScene);
				break;
			case SCENE_USERNAME_MENU:
				setScene(usernameMenuScene);
				break;
			default:
				break;
		}
	}
	
	public static SceneManager getInstance(){
		return INSTANCE;
	}
	
	public SceneType getCurrentSceneType(){
		return currentSceneType;
	}
	
	public BaseScene getCurrentScene(){
		return currentScene;
	}
}


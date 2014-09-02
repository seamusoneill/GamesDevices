package ie.itcarlow.jumpinggame;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import ie.itcarlow.jumpinggame.BaseScene;
import ie.itcarlow.jumpinggame.SplashScene;
import ie.itcarlow.jumpinggame.MainMenuScene;
import ie.itcarlow.jumpinggame.LoadingScene;
import ie.itcarlow.jumpinggame.GameScene;

public class SceneManager {
	
	//Scenes
	
	private BaseScene splashScene;
	private BaseScene menuScene;
	private BaseScene optionsMenuScene;
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
		SCENE_OPTIONS_MENU,
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
	
	public void createOptionsScene(){
		ResourcesManager.getInstance().loadOptionsMenuResources();
		optionsMenuScene = new OptionsMenuScene();
		loadingScene = new LoadingScene();
		SceneManager.getInstance().setScene(optionsMenuScene);
		ResourcesManager.getInstance().unloadMenuTextures();
	}
	
	public void loadGameScene(final Engine mEngine, final int levelID){
		setScene(loadingScene);
		ResourcesManager.getInstance().unloadMenuTextures();
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
			
            public void onTimePassed(final TimerHandler pTimerHandler) {
            	
            	mEngine.unregisterUpdateHandler(pTimerHandler);
            	ResourcesManager.getInstance().loadGameResources();
        		gameScene = new GameScene(levelID);
        		setScene(gameScene);
            }
		}));
	}
	
	public void loadOptionsMenuScene(final Engine mEngine){
		setScene(loadingScene);
		
		ResourcesManager.getInstance().unloadMenuTextures();
		
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback(){
			
			public void onTimePassed (final TimerHandler pTimerHandler){
			ResourcesManager.getInstance().loadOptionsMenuTextures();
			optionsMenuScene = new OptionsMenuScene();
			setScene(optionsMenuScene);
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
			case SCENE_OPTIONS_MENU:
				setScene(optionsMenuScene);
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

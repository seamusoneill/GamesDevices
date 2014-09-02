package ie.itcarlow.jointproject;

import ie.itcarlow.jointproject.BaseScene;
import ie.itcarlow.jointproject.ResourcesManager;
import ie.itcarlow.jointproject.SceneManager;
import ie.itcarlow.jointproject.SceneManager.SceneType;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

public class MainMenuScene extends BaseScene implements MenuScene.IOnMenuItemClickListener {
	
	private MenuScene menuChildScene;
	private final int MENU_PLAY = 0;
	private final int MENU_USERNAME = 1;
	private final int MENU_QUIT = 2;
	
	@Override
	public void createScene() {
		 createBackground();
		 createMenuChildScene();
	}

	@Override
	public void onBackKeyPressed() {
		System.exit(0);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_MENU;
	}

	@Override
	public void disposeScene() {
		ResourcesManager.getInstance().unloadMenuTextures();
	}


	private void createBackground(){
		attachChild(new Sprite(400,240, resourcesManager.menu_background_region, vbom)
		{
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera)
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		});
	}
	
	private void createMenuChildScene(){
		
		menuChildScene = new MenuScene(camera);
		menuChildScene.setPosition(0,0);
		
		final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourcesManager.play_region,vbom),1.2f,1);
		final IMenuItem usernameMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_USERNAME, resourcesManager.username_region,vbom),1.2f,1);
		final IMenuItem quitMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_QUIT, resourcesManager.quit_region,vbom), 1.2f,1);
		
		menuChildScene.addMenuItem(playMenuItem);
		menuChildScene.addMenuItem(usernameMenuItem);
		menuChildScene.addMenuItem(quitMenuItem);
		
		menuChildScene.buildAnimations();
		menuChildScene.setBackgroundEnabled(false);
		
		playMenuItem.setPosition(playMenuItem.getX(),playMenuItem.getY() + 50);
		usernameMenuItem.setPosition(usernameMenuItem.getX(), usernameMenuItem.getY());
		quitMenuItem.setPosition(quitMenuItem.getX(), quitMenuItem.getY() - 50);
		
		menuChildScene.setOnMenuItemClickListener(this);
		
		setChildScene(menuChildScene);
	
	}
	
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItmLocalX, float pMenuItemLocalY){
		switch(pMenuItem.getID())
		{
		case MENU_PLAY:
			SceneManager.getInstance().loadGameScene(engine);
			return true;
		case MENU_USERNAME:
			SceneManager.getInstance().createUsernameScene();
			return true;
		case MENU_QUIT:
		{
		    System.exit(0);	
			return true;
		}
		default:
			return false;
		}
	}

}

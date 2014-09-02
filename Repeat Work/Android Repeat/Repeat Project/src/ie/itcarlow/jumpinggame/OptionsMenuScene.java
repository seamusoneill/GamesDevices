package ie.itcarlow.jumpinggame;


import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import ie.itcarlow.jumpinggame.BaseScene;
import ie.itcarlow.jumpinggame.SceneManager.SceneType;

public class OptionsMenuScene extends BaseScene implements MenuScene.IOnMenuItemClickListener {

	private MenuScene optionsMenuChildScene;
	private final int CHANGE_BACKGROUND = 0;
	private final int RETURN = 1;
	
	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch(pMenuItem.getID())
		{
		case CHANGE_BACKGROUND:
			if(resourcesManager.selectBackground == 1){
			resourcesManager.selectBackground++;
			}
			else if(resourcesManager.selectBackground == 2){
				resourcesManager.selectBackground--;
			}
			createMenuChildScene(); //Recreates the menu... probably not the most efficient way of updating menu items.
			return true;
		case RETURN:
		    SceneManager.getInstance().loadMenuScene(engine);
		    disposeScene();
			return true;

		default:
			return false;
		}
	}

	@Override
	public void createScene() {
		 createBackground();
		 createMenuChildScene();
	}

	private void createBackground() {
		attachChild(new Sprite(400,240, resourcesManager.options_menu_background_region, vbom)
		{
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera)
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		});
	}

	private void createMenuChildScene() {
		optionsMenuChildScene = new MenuScene(camera);
		optionsMenuChildScene.setPosition(0,0);
		
		final IMenuItem changeBackgroundItem = new ScaleMenuItemDecorator(new TextMenuItem(CHANGE_BACKGROUND, resourcesManager.font, "Change Background:" + resourcesManager.selectBackground, vbom),1.2f,1);
		final IMenuItem returnItem = new ScaleMenuItemDecorator(new TextMenuItem(RETURN, resourcesManager.font,"Return",vbom),1.2f,1);
		
		optionsMenuChildScene.addMenuItem(changeBackgroundItem);
		optionsMenuChildScene.addMenuItem(returnItem);
		
		optionsMenuChildScene.buildAnimations();
		optionsMenuChildScene.setBackgroundEnabled(false);
		
		changeBackgroundItem.setPosition(changeBackgroundItem.getX(),changeBackgroundItem.getY());
		returnItem.setPosition(returnItem.getX(),returnItem.getY() - 10);
		
		optionsMenuChildScene.setOnMenuItemClickListener(this);
		
		setChildScene(optionsMenuChildScene);
	}
	
	@Override
	public void onBackKeyPressed() {
	    SceneManager.getInstance().loadMenuScene(engine);
	    disposeScene();
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_OPTIONS_MENU;
	}

	@Override
	public void disposeScene() {
		ResourcesManager.getInstance().unloadOptionsMenuTextures();
		this.detachSelf();
		this.dispose();
	}
}



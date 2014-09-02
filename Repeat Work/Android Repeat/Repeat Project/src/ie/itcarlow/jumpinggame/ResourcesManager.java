package ie.itcarlow.jumpinggame;


import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.graphics.Color;

import ie.itcarlow.jumpinggame.GameActivity;

public class ResourcesManager {
	
	//Variables
	
	private static final ResourcesManager INSTANCE = new ResourcesManager();
	
	public Engine engine;
	public GameActivity activity;
	public BoundCamera camera;
	public VertexBufferObjectManager vbom;
	
	public Font font; //Sans Serif
	
	//Splash Screen Textures
	public ITextureRegion splash_region;
	private BitmapTextureAtlas splashTextureAtlas;
	
	//Menu Textures
	public ITextureRegion menu_background_region;
	public ITextureRegion play_region;
	public ITextureRegion options_region;
	public ITextureRegion quit_region;
	private BuildableBitmapTextureAtlas menuTextureAtlas;
	
	//Options Menu Textures
	public ITextureRegion options_menu_background_region;
	private BuildableBitmapTextureAtlas optionsMenuTextureAtlas;
	
	//Game Textures
	public BuildableBitmapTextureAtlas gameTextureAtlas;
	public ITextureRegion game_background_region;
	public int selectBackground = 1;
	public ITextureRegion platform_region;
	public ITextureRegion goal_region;
	public ITiledTextureRegion player_region; //Tiled to allow for animation
	
	//Game Audio
	public static Music backgroundTheme;
	public Sound flipEffect;
	
	public void loadMenuResources() {
		loadMenuGraphics();
		loadMenuAudio();
		loadMenuFonts();
	}

	public void loadGameResources() {
		loadGameGraphics();
		LoadGameFonts();
		loadGameAudio();
	}
	
	public void loadOptionsMenuResources(){
		loadOptionsMenuGraphics();
		loadOptionsMenuFonts();
	}

	
	private void loadOptionsMenuFonts() {
		// TODO Auto-generated method stub
		
	}

	//TODO Consider creating the options menu graphics in loadMenuGraphics
	private void loadOptionsMenuGraphics() {
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/"); // TODO remove this line; Probably not necessary since you can only get to the options menu through the menu
		optionsMenuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,TextureOptions.BILINEAR);
		options_menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsMenuTextureAtlas, activity,"options_menu_background.png");
	
		try{
			this.optionsMenuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0,1,0));
			this.optionsMenuTextureAtlas.load();
		}
		catch(final TextureAtlasBuilderException e){
			Debug.e(e);
		}
	}

	//Creates and loads the menu textures
	private void loadMenuGraphics() {
	
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,TextureOptions.BILINEAR);
		menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity,"menu_background.png");
		play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas,activity,"play.png");
		options_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "options.png"); 
		quit_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas,activity,"quit.png");
		
		try{
			this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0,1,0));
			this.menuTextureAtlas.load();
		}
		catch(final TextureAtlasBuilderException e){
			Debug.e(e);
		}
	}
	
	//This does the same as loadMenuGraphics but without recreating the textures, they only have to be reloaded. This is to save time.
	public void loadMenuTextures() {
		menuTextureAtlas.load();
	}

	public void loadOptionsMenuTextures() {
		optionsMenuTextureAtlas.load();
	}
	
	//Unloads menu textures to free up memory.
	public void unloadMenuTextures(){
		menuTextureAtlas.unload();
	}
	
	public void unloadOptionsMenuTextures(){
		optionsMenuTextureAtlas.unload();
	}
	
	private void loadMenuAudio() {
		// TODO Auto-generated method stub		
	}
	
	private void loadGameGraphics() {
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		
		game_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas,activity,"background" + selectBackground + ".png");
		platform_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas,activity,"platform1.png");
		goal_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,"goal.png");
		player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas,activity,"player.png",3,1); //3,2 refers to the number of rows and columns of the sprite sheet
		try{
			this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0,1,0)); //TODO find out what black pawn is 
			gameTextureAtlas.load();
		}
		catch (final TextureAtlasBuilderException e){
			Debug.e(e);
		}
	}
	
	public void unloadGameTextures(){
		
	}
	
	private void LoadGameFonts() {
		// TODO Auto-generated method stub	
	}
	
	;
	private void loadGameAudio() {
		MusicFactory.setAssetBasePath("audio/");
		SoundFactory.setAssetBasePath("audio/");
		try 
		{
			if(backgroundTheme == null)
			{
				backgroundTheme = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity, "song.m4a");
			}
			flipEffect = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "flip.wav");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void loadMenuFonts(){
		FontFactory.setAssetBasePath("font/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
	    font.load();
	}
	
	//Loads the splash screen texture
	public void loadSplashScreen(){

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.png",0, 0);
		splashTextureAtlas.load();
	}
	
	public void unloadSplashScreen(){
		
		splashTextureAtlas.unload();
		splash_region = null;
	}

	//We use this method at beginning of game loading, to prepare Resources Manager properly,
    //setting all needed parameters, so we can later access them from different classes
	public static void prepareManager(Engine engine, GameActivity activity, BoundCamera camera, VertexBufferObjectManager vbom){
		
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
	}
	
	public static ResourcesManager getInstance()
	{
		return INSTANCE;
	}

	
}

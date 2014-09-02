package ie.itcarlow.jointproject;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
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

import ie.itcarlow.jointproject.GameActivity;

public class ResourcesManager {
	
	private static final ResourcesManager INSTANCE = new ResourcesManager();
	
	//Variable
	public Engine engine;
	public GameActivity activity;
	public Camera camera;
	public VertexBufferObjectManager vbom;
	
	public Font font; //Sans Serif
	
	//Splash Screen Textures
	public ITextureRegion splash_region;
	private BitmapTextureAtlas splashTextureAtlas;
	
	//Menu Textures
	public ITextureRegion menu_background_region;
	public ITextureRegion play_region;
	public ITextureRegion username_region;
	public ITextureRegion quit_region;
	private BuildableBitmapTextureAtlas menuTextureAtlas;
	
	//Set username screen textures
	public ITextureRegion username_menu_background_region;
	private BuildableBitmapTextureAtlas usernameMenuTextureAtlas;
	
	//Game Textures
	public BuildableBitmapTextureAtlas gameTextureAtlas;
	public ITextureRegion game_background_region;
	public ITextureRegion obstacle_region;
	public ITiledTextureRegion player1_region; //Tiled to allow for animation
	public ITiledTextureRegion player2_region;
	
	public void loadMenuResources() {
		loadMenuGraphics();
		loadMenuAudio();
		loadMenuFonts();
	}

	private void loadMenuGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,TextureOptions.BILINEAR);
		menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity,"menu_background.png");
		play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas,activity,"play.png");
		username_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "username.png"); 
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
	
	public void unloadMenuTextures() {
		menuTextureAtlas.unload();
	}
	
	private void loadMenuFonts() {
		FontFactory.setAssetBasePath("font/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
	    font.load();
	}

	private void loadMenuAudio() {
		// TODO Auto-generated method stub
		
	}
	
	public void loadUsernameMenuResources() {
		loadUsernameMenuGraphics();
		loadUsernameMenuFonts();
	}
	
	public void loadUsernameMenuTextures() {
		usernameMenuTextureAtlas.load();
	}	
	
	public void unloadUsernameMenuTextures() {
		usernameMenuTextureAtlas.unload();
	}
	private void loadUsernameMenuFonts() {
		// TODO Auto-generated method stub
		
	}

	private void loadUsernameMenuGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		usernameMenuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,TextureOptions.BILINEAR);
		username_menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(usernameMenuTextureAtlas, activity,"username_menu_background.png");
	
		try{
			this.usernameMenuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0,1,0));
			this.usernameMenuTextureAtlas.load();
		}
		catch(final TextureAtlasBuilderException e){
			Debug.e(e);
		}
		
	}

	public void loadGameResources() {
		loadGameGraphics();
		LoadGameFonts();
		loadGameAudio();
	}
	
	private void loadGameGraphics() {
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		
		game_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas,activity,"background.png");
		obstacle_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,"obstacle.png");
		player1_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas,activity,"player1.png",3,1); //3,2 refers to the number of rows and columns of the sprite sheet
		player2_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas,activity,"player2.png",3,1); //3,2 refers to the number of rows and columns of the sprite sheet
		
		try{
			this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0,1,0)); //TODO find out what black pawn is 
			gameTextureAtlas.load();
		}
		catch (final TextureAtlasBuilderException e){
			Debug.e(e);
		}
	}

	private void LoadGameFonts() {
		FontFactory.setAssetBasePath("font/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
	    font.load();
	}

	private void loadGameAudio() {
		// TODO Auto-generated method stub
		
	}
	
	public void unloadGameTextures() {
		// TODO Auto-generated method stub
		
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
	public static void prepareManager(Engine engine, GameActivity activity, Camera camera, VertexBufferObjectManager vbom){
		
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

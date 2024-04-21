package com.BotS.BaseGame;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.graphics.Color;

public class ResourcesManager {
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private static final ResourcesManager INSTANCE = new ResourcesManager();

	public Engine engine;
	public GameActivity activity;
	public BoundCamera camera;
	public VertexBufferObjectManager vbom;
	public Font font;
	public Font largeFont;
	public TextureManager textureManager;

	// ---------------------------------------------
	// TEXTURES & TEXTURE REGIONS
	// ---------------------------------------------
	public ITextureRegion splash_region;
	public ITextureRegion fireBall_red_l_region;
	public ITextureRegion fireBall_red_r_region;
	public ITextureRegion fireBall_blue_l_region;
	public ITextureRegion fireBall_blue_r_region;
	public ITextureRegion fireBall_green_l_region;
	public ITextureRegion fireBall_green_r_region;
	public ITextureRegion fireBall_purple_l_region;
	public ITextureRegion fireBall_purple_r_region;

	private BuildableBitmapTextureAtlas menuTextureAtlas;
	private BitmapTextureAtlas splashTextureAtlas;

	// Game Texture
	public BuildableBitmapTextureAtlas gameTextureAtlas;
	public BuildableBitmapTextureAtlas gameTextureAtlas2;
	// public BuildableBitmapTextureAtlas gameCloudTextureAtlas;

	// Game Texture Regions
	public ITextureRegion sunRay_region;
	public ITextureRegion sunRayEasterEggSmile_region, sunRayEasterEggFrown_region, sunRayEasterEggEyes_region;
	public ITextureRegion lightning_region;
	public ITextureRegion castleShield_region1;
	public ITextureRegion castleShield_region2;
	public ITextureRegion castleOverlay_region1;
	public ITextureRegion castleOverlay_region2;
	public ITextureRegion iceberg_region;
	public ITiledTextureRegion boat_l_region;
	public ITiledTextureRegion boat_r_region;
	public ITiledTextureRegion bird_region;
	public ITextureRegion sun_region;
	public ITextureRegion axis_red_region;
	public ITextureRegion axis_green_region;
	public ITextureRegion handle_red_region;
	public ITextureRegion handle_green_region;
	public ITextureRegion arrow_region;
	public ITextureRegion heart_region;
	public ITextureRegion empty_heart_region;
	public ITextureRegion castle1_region;
	public ITextureRegion castle2_region;
	public ITiledTextureRegion butterfly_region;
	public ITextureRegion boatShield_region1;
	public ITextureRegion boatShield_region2;
	public ITextureRegion rainbow_region;
	public ITextureRegion snow_region;
	public ITextureRegion leaf_region;
	public ITextureRegion petal_region;
	public ITextureRegion fire_red_region;
	public ITextureRegion fire_blue_region;
	public ITextureRegion fire_green_region;
	public ITextureRegion fire_purple_region;
	public ITextureRegion sorcerer_red_region;
	public ITextureRegion sorcerer_red_cane_region;
	public ITextureRegion sorcerer_purple_region;
	public ITextureRegion sorcerer_purple_cane_region;
	public ITextureRegion rocks_region;
	public ITextureRegion background_region;

	public ITextureRegion cloud_spring_0_region;
	public ITextureRegion cloud_spring_1_region;
	public ITextureRegion cloud_spring_2_region;
	public ITextureRegion cloud_summer_0_region;
	public ITextureRegion cloud_summer_1_region;
	public ITextureRegion cloud_summer_2_region;
	public ITextureRegion cloud_summer_3_region;
	public ITextureRegion cloud_fall_0_region;
	public ITextureRegion cloud_fall_1_region;
	public ITextureRegion cloud_fall_2_region;
	public ITextureRegion cloud_winter_0_region;
	public ITextureRegion cloud_winter_1_region;
	public ITextureRegion cloud_winter_2_region;

	public ITextureRegion specialFireballTutorial, basicTutorial, seasonTutorial, about;

	// ---------------------------------------------
	// MUSIC AND SOUND EFFECTS
	// ---------------------------------------------

	// Fireball related sounds
	public Music BackgroundMusic;
	public Sound FireballHitShield;
	public Sound explodeSound;
	public Sound ShootFireball;
	public Sound FireballHitWater;
	public Sound BirdHit;
	// Boat related sounds
	public Sound BoatHitCastle;
	// Special magic related power
	public Sound SpringShieldPlaced;
	public Sound SummerSunRay;
	public Sound SummerSunRayExp;
	public Sound FallLightning;
	public Sound WinterIcebergPlaced;
	public Sound Meow;

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	public void loadGameResources() {
		engine.registerUpdateHandler(new FPSLogger());
		loadFonts();
		loadGameGraphics();
		loadGameFonts();
		loadGameAudio();
	}

	public void unloadGameTextures() {
		// TODO (Since we did not create any textures for game scene yet)
	}

	private void loadGameGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 4048, 4048,
				TextureOptions.BILINEAR);

		sunRay_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, activity, "SunRay.png");
		sunRayEasterEggSmile_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas,
				activity, "cat_smile.png");
		sunRayEasterEggFrown_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas,
				activity, "cat_frown.png");
		sunRayEasterEggEyes_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"cat_frown_eyesOnly.png");
		lightning_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"lightning.png");
		castleShield_region1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"castleShield1.png");
		castleShield_region2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"castleShield2.png");
		castleOverlay_region1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"castle1_red.png");
		castleOverlay_region2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"castle2_red.png");
		boat_l_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity,
				"boat_l.png", 8, 3);
		boat_r_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity,
				"boat_r.png", 8, 3);
		bird_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity,
				"Bird.png", 2, 1);
		butterfly_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity,
				"butterfly.png", 4, 1);
		fireBall_red_r_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"fireball_red_r.png");
		fireBall_red_l_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"fireball_red_l.png");
		fireBall_blue_r_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"fireball_blue_r.png");
		fireBall_blue_l_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"fireball_blue_l.png");
		fireBall_green_r_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"fireball_green_r.png");
		fireBall_green_l_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"fireball_green_l.png");
		fireBall_purple_r_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"fireball_purple_r.png");
		fireBall_purple_l_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"fireball_purple_l.png");
		sorcerer_red_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"sorcerer_red.png");
		sorcerer_purple_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"sorcerer_purple.png");
		boatShield_region1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"BoatShield1.png");
		boatShield_region2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"BoatShield2.png");
		snow_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"snow_small.png");
		rainbow_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"rainbow.png");
		leaf_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "leaf.png");
		petal_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "petal.png");
		fire_red_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"fire_red.png");
		fire_blue_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"fire_blue.png");
		fire_green_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"fire_green.png");
		fire_purple_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"fire_purple.png");
		sun_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, activity, "sun_small.png");
		iceberg_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"iceberg.png");
		axis_red_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"axis_red.png");
		axis_green_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"axis_green.png");
		handle_red_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"handle_red.png");
		handle_green_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"handle_green.png");
		arrow_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "arrow.png");
		heart_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "heart.png");
		empty_heart_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"empty_heart.png");
		rocks_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "rocks.png");
		castle1_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"castle1.png");
		castle2_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"castle2.png");

		// gameCloudTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 4048, 4048,
		// BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR);

		cloud_spring_0_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"cloud-spring1.png");
		cloud_spring_1_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"cloud-spring2.png");
		cloud_spring_2_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"cloud-spring3.png");

		cloud_summer_0_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"cloud-summer1.png");
		cloud_summer_1_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"cloud-summer2.png");
		cloud_summer_2_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"cloud-summer3.png");
		cloud_summer_3_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"cloud-summer4.png");

		cloud_fall_0_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"cloud-fall1.png");
		cloud_fall_1_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"cloud-fall2.png");
		cloud_fall_2_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"cloud-fall3.png");

		cloud_winter_0_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"cloud-winter1.png");
		cloud_winter_1_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"cloud-winter2.png");
		cloud_winter_2_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"cloud-winter3.png");

		specialFireballTutorial = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"SpecialFirballTutorial.png");
		basicTutorial = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"BasicTutorial.png");
		seasonTutorial = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"SeasonTutorial.png");
		about = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "about.png");

		gameTextureAtlas2 = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2320, 180,
				BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR);
		background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas2, activity,
				"background_small.png");

		try {
			gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0,
					1, 2));
			gameTextureAtlas.load();

			// gameCloudTextureAtlas
			// .build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			// gameCloudTextureAtlas.load();

			gameTextureAtlas2.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0,
					1, 0));
			gameTextureAtlas2.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	private void loadGameFonts() {

	}

	private void loadFonts() {
		FontFactory.setAssetBasePath("font/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		final ITexture mainFontTextureLarge = new BitmapTextureAtlas(activity.getTextureManager(), 512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(),
				"font.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
		largeFont = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTextureLarge,
				activity.getAssets(), "font.ttf", 100, true, Color.WHITE, 2, Color.BLACK);
		font.load();
		largeFont.load();
	}

	public void unloadMenuTextures() {
		menuTextureAtlas.unload();
	}

	public void loadMenuTextures() {
		menuTextureAtlas.load();
	}

	private void loadGameAudio() {
		SoundFactory.setAssetBasePath("mfx/");
		MusicFactory.setAssetBasePath("mfx/");
		try {
			// Sounds that are usable
			BackgroundMusic = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity,
					"Blackmoor_Tides.mp3");
			BackgroundMusic.setVolume(0.25f);
			BackgroundMusic.setLooping(true);
			ShootFireball = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity,
					"Flame Arrow-short.mp3");
			ShootFireball.setVolume(1.0f);

			explodeSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "Explosion-Short.mp3");
			explodeSound.setVolume(0.3f);

			FireballHitWater = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "FireWater.mp3");
			FireballHitWater.setVolume(0.15f);

			BoatHitCastle = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "WoodCrack.mp3");
			BoatHitCastle.setVolume(0.6f);

			BirdHit = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "birds001.mp3");
			BirdHit.setVolume(0.3f);

			FireballHitShield = SoundFactory
					.createSoundFromAsset(engine.getSoundManager(), activity, "shieldSound.mp3");
			FireballHitShield.setVolume(0.5f);

			FallLightning = SoundFactory
					.createSoundFromAsset(engine.getSoundManager(), activity, "lightnighStrike.mp3");
			FallLightning.setVolume(1.0f);

			SummerSunRay = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "sunrayHeating.mp3");
			SummerSunRay.setVolume(1.0f);

			SummerSunRayExp = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity,
					"Barrel Exploding-SoundBible.com-1134967902.mp3");
			SummerSunRayExp.setVolume(1.0f);

			WinterIcebergPlaced = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "Iceberg.mp3");
			WinterIcebergPlaced.setVolume(1.0f);

			SpringShieldPlaced = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "shieldSet.mp3");
			SpringShieldPlaced.setVolume(1.0f);

			Meow = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "cat.mp3");
			Meow.setVolume(0.25f);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadSplashScreen() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1280, 800, TextureOptions.BILINEAR);
		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity,
				"splash.png", 0, 0);
		splashTextureAtlas.load();
	}

	public void unloadSplashScreen() {
		splashTextureAtlas.unload();
		splash_region = null;
	}

	/**
	 * @param engine
	 * @param activity
	 * @param camera
	 * @param vbom
	 * <br>
	 * <br>
	 *            We use this method at beginning of game loading, to prepare Resources Manager properly, setting all
	 *            needed parameters, so we can latter access them from different classes (eg. scenes)
	 * @param textureManager
	 */
	public static void prepareManager(Engine engine, GameActivity activity, BoundCamera camera,
			VertexBufferObjectManager vbom, TextureManager textureManager) {
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
		getInstance().textureManager = textureManager;
	}

	// ---------------------------------------------
	// GETTERS AND SETTERS
	// ---------------------------------------------

	public static ResourcesManager getInstance() {
		return INSTANCE;
	}

}

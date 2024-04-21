package com.BotS.BaseGame;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.SpriteGroup;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.list.SmartList;
import org.andengine.util.debug.Debug;

import com.BotS.BaseGame.Player.Team;
import com.BotS.BaseGame.SceneManager.SceneType;
import com.BotS.Physics.FixedStepPhysicsWorld;
import com.BotS.Physics.PhysicsConstants;
import com.BotS.Physics.PhysicsFactory;
import com.BotS.Physics.Vector2;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.ParticleGroup;
import com.google.fpl.liquidfun.ParticleSystem;

public class GameScene extends BaseScene implements IOnSceneTouchListener, PhysicsConstants, GameConstants {

	// layers define the depth of the object
	private List<Entity> layers;
	private IEntity gameHUD;
	protected FixedStepPhysicsWorld physicsWorld;
	private MagicBird magicBird;
	protected Player leftPlayer, rightPlayer;
	protected Castle leftCastle, rightCastle;
	public ArrayList<Lightning> lightnings;
	protected IcebergSpawner icebergSpawn;
	protected LightningSpawner lightningSpawn;
	private CollisionHandler collisionHandler;
	private BoatSpawner boatSpawner;
	private Background background;
	private Menu menu;
	private State state;
	private int currFrames;
	boolean musicPlaying = false;
	public RockAndHeartsSpriteBatch rockAndHeartsSpriteBatch;

	public enum State {
		START_MENU, PLAY, PAUSED, END;
	}

	public enum Layer {
		BACKGROUND(0), CLOUDS(1), BACKGROUND_ITEMS(2), SORCERER(3), CASTLE(4), BOAT(5), FIREBALL_TAIL(6), FIREBALL(7), WATER(
				8), ROCK(9), MENU(10);

		public int id;

		private Layer(int pId) {
			id = pId;
		}
	}

	public BoatSpawner getBoatSpawner() {

		return boatSpawner;
	}

	public Player getLeftPlayer() {

		return leftPlayer;
	}

	public Player getRightPlayer() {

		return rightPlayer;
	}

	public State getState() {
		return state;
	}

	public int getCurrFrames() {
		return currFrames;
	}

	@Override
	public void createScene() {
		createLayers();
		leftPlayer = new Player(Team.LEFT, 150, (int) (RESOLUTION.y * 0.8), this);
		rightPlayer = new Player(Team.RIGHT, (int) (RESOLUTION.x * 0.88), (int) (RESOLUTION.y * 0.8), this);
		leftPlayer.setOpponent(rightPlayer);
		rightPlayer.setOpponent(leftPlayer);

		createHUD();
		createPhysics();
		createLevel();
		createMenu();

		setOnSceneTouchListener(this);
		collisionHandler = new CollisionHandler(this);
		int boatOffset = 250;
		boatSpawner = new BoatSpawner(this, new Vector2<Integer>(LEFT_BOUND + boatOffset, 250), new Vector2<Integer>(
				RIGHT_BOUND - boatOffset, 250));
		state = State.START_MENU; // should be at the end of create
	}

	private void createMenu() {
		menu = new Menu(this);
		getLayer(Layer.MENU).attachChild(menu);
	}

	private void createLayers() {
		layers = new ArrayList<Entity>();
		for (int i = 0; i < Layer.values().length; i++) {
			Entity e;
			if (i == Layer.ROCK.id) {
				rockAndHeartsSpriteBatch = new RockAndHeartsSpriteBatch(this);
				e = rockAndHeartsSpriteBatch;
			} else if (i == Layer.BOAT.id) {
				e = new CenteredSpriteGroup(ResourcesManager.getInstance().gameTextureAtlas,
						MAX_BOATS_PER_TEAM * 4 + 4, vbom);
			} else if (i == Layer.FIREBALL.id) {
				e = new CenteredSpriteGroup(ResourcesManager.getInstance().gameTextureAtlas, MAX_BOATS_PER_TEAM * 4,
						vbom);
			} else if (i == Layer.CLOUDS.id) {
				e = new CenteredSpriteGroup(ResourcesManager.getInstance().gameTextureAtlas, 20, vbom);
			} else if (i == Layer.CASTLE.id) {
				e = new CenteredSpriteGroup(ResourcesManager.getInstance().gameTextureAtlas, 2, vbom);
			} else if (i == Layer.SORCERER.id) {
				e = new CenteredSpriteGroup(ResourcesManager.getInstance().gameTextureAtlas, 2, vbom);
			} else {
				e = new Entity();
			}
			layers.add(e);
			attachChild(e);
		}
	}

	private static class CenteredSpriteGroup extends SpriteGroup {
		public CenteredSpriteGroup(final ITexture pTexture, final int pCapacity,
				final VertexBufferObjectManager pVertexBufferObjectManager) {
			super(pTexture, pCapacity, pVertexBufferObjectManager);
		}

		@Override
		protected void addWithPackedColor(ITextureRegion pTextureRegion, float pX, float pY, float pWidth,
				float pHeight, float pColorABGRPackedInt) {
			super.addWithPackedColor(pTextureRegion, pX - pWidth / 2, pY - pHeight / 2, pWidth, pHeight,
					pColorABGRPackedInt);
		}

		@Override
		protected boolean onUpdateSpriteBatch() {
			return false;
		}

		@Override
		protected void onManagedUpdate(float pSecondsElapsed) {
			super.onManagedUpdate(pSecondsElapsed);
			final SmartList<IEntity> children = mChildren;
			if (children != null) {
				final int childCount = children.size();
				for (int i = 0; i < childCount; i++) {
					this.drawWithoutChecks((Sprite) children.get(i));
				}
				submit();
			}
		};

	}

	public Entity getLayer(Layer layer) {
		return layers.get(layer.id);
	}

	@Override
	public void onBackKeyPressed() {
		if (state == State.PLAY) {
			pauseGame();
		}
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene() {
		camera.setHUD(null);
		camera.setChaseEntity(null);
		ResourcesManager.getInstance().BackgroundMusic.stop();
	}

	private void createHUD() {
		gameHUD = getLayer(Layer.FIREBALL);

		gameHUD.attachChild(leftPlayer.getTokenLevelOne1());
		gameHUD.attachChild(rightPlayer.getTokenLevelOne1());
		registerTouchArea(leftPlayer.getTokenLevelOne1());
		registerTouchArea(rightPlayer.getTokenLevelOne1());

		gameHUD.attachChild(leftPlayer.getTokenLevelOne2());
		gameHUD.attachChild(rightPlayer.getTokenLevelOne2());
		registerTouchArea(leftPlayer.getTokenLevelOne2());
		registerTouchArea(rightPlayer.getTokenLevelOne2());

		gameHUD.attachChild(leftPlayer.getTokenLevelOne3());
		gameHUD.attachChild(rightPlayer.getTokenLevelOne3());
		registerTouchArea(leftPlayer.getTokenLevelOne3());
		registerTouchArea(rightPlayer.getTokenLevelOne3());

		gameHUD.attachChild(leftPlayer.getTokenLevelTwo1());
		gameHUD.attachChild(rightPlayer.getTokenLevelTwo1());
		registerTouchArea(leftPlayer.getTokenLevelTwo1());
		registerTouchArea(rightPlayer.getTokenLevelTwo1());

		gameHUD.attachChild(leftPlayer.getTokenLevelTwo2());
		gameHUD.attachChild(rightPlayer.getTokenLevelTwo2());
		registerTouchArea(leftPlayer.getTokenLevelTwo2());
		registerTouchArea(rightPlayer.getTokenLevelTwo2());

		gameHUD.attachChild(leftPlayer.getTokenLevelThree());
		gameHUD.attachChild(rightPlayer.getTokenLevelThree());
		registerTouchArea(leftPlayer.getTokenLevelThree());
		registerTouchArea(rightPlayer.getTokenLevelThree());

	}

	private void createPhysics() {
		physicsWorld = new FixedStepPhysicsWorld(60, 3, new Vector2<Float>(0.0f, -17.0f), new Vector2<Float>(
				PLANET_CENTER.x / PIXEL_TO_METER_RATIO_DEFAULT, PLANET_CENTER.y / PIXEL_TO_METER_RATIO_DEFAULT), false);
		registerUpdateHandler(physicsWorld);
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.getX() < RESOLUTION.x / 2) {
			leftPlayer.getShooter().onScreenAreaTouched(pSceneTouchEvent);
		} else {
			rightPlayer.getShooter().onScreenAreaTouched(pSceneTouchEvent);
		}
		return false;
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		if (menu.getFinalSoundSelectionIndex() == 2 && !musicPlaying) {
			ResourcesManager.getInstance().BackgroundMusic.play();
			musicPlaying = true;
		} else if (menu.getFinalSoundSelectionIndex() == 1 && musicPlaying) {
			ResourcesManager.getInstance().BackgroundMusic.pause();
			musicPlaying = false;
		}
		if (state == State.PAUSED) {
			menu.onUpdate(pSecondsElapsed);
			return;
		}
		super.onManagedUpdate(pSecondsElapsed);
		currFrames++;

		background.onUpdate();

		if (state == State.PLAY) {
			checkIfGameEnded();
		}
		collisionHandler.onUpdate();

		if (state == State.PLAY) {
			boatSpawner.onUpdate();
			// maybe move this code to a BirdSpawner class
			if (currFrames - magicBird.getLastFrameHit() > BIRDTIME) {
				magicBird.appears();
			}

			checkTokens(leftPlayer);
			checkTokens(rightPlayer);

		}

	}

	public void unregisterIndicators(Player player) {

		unregisterTouchArea(player.getTokenLevelOne1());
		unregisterTouchArea(player.getTokenLevelOne2());
		unregisterTouchArea(player.getTokenLevelOne3());
		unregisterTouchArea(player.getTokenLevelTwo1());
		unregisterTouchArea(player.getTokenLevelTwo2());
		unregisterTouchArea(player.getTokenLevelThree());

	}

	public void registerIndicators(Player player) {

		registerTouchArea(player.getTokenLevelOne1());
		registerTouchArea(player.getTokenLevelOne2());
		registerTouchArea(player.getTokenLevelOne3());
		registerTouchArea(player.getTokenLevelTwo1());
		registerTouchArea(player.getTokenLevelTwo2());
		registerTouchArea(player.getTokenLevelThree());

	}

	public void checkTokens(Player player) {

		int count = player.getTokenLevelOneCount();

		if (count == 0) {
			player.getTokenLevelOne1().setVisible(false);
			player.getTokenLevelOne2().setVisible(false);
			player.getTokenLevelOne3().setVisible(false);
			player.getTokenLevelTwo1().setVisible(false);
			player.getTokenLevelTwo2().setVisible(false);
			player.getTokenLevelThree().setVisible(false);

		}

		if (count == 1) {

			player.getTokenLevelOne1().setVisible(true);
			player.getTokenLevelOne2().setVisible(false);
			player.getTokenLevelOne3().setVisible(false);
			player.getTokenLevelTwo1().setVisible(false);
			player.getTokenLevelTwo2().setVisible(false);
			player.getTokenLevelThree().setVisible(false);
		}

		if (count == 2) {
			player.getTokenLevelOne1().setVisible(true);
			player.getTokenLevelOne2().setVisible(true);
			player.getTokenLevelOne3().setVisible(false);
			player.getTokenLevelTwo1().setVisible(true);
			player.getTokenLevelTwo2().setVisible(false);
			player.getTokenLevelThree().setVisible(false);
		}

		if (count == 3) {
			player.getTokenLevelOne1().setVisible(true);
			player.getTokenLevelOne2().setVisible(true);
			player.getTokenLevelOne3().setVisible(true);
			player.getTokenLevelTwo1().setVisible(true);
			player.getTokenLevelTwo2().setVisible(true);
			player.getTokenLevelThree().setVisible(true);
		}

	}

	private void checkIfGameEnded() {
		if (leftPlayer.getLife() <= 0 || rightPlayer.getLife() <= 0) {
			endGame();
		}
	}

	public Season getSeasonAtPosition(float pX, float pY) {
		return background.getSeasonAtPosition(pX, pY);
	}

	public Sprite getSun() {
		return background.getSun();
	}

	public SunRayEasterEgg getSunEasterEgg() {
		return background.getSunEasterEgg();
	}

	public SunraySpawner getSunraySpawner() {
		return background.getSunraySpawner();
	}

	private void createLevel() {
		createBackground();
		createCastles();
		createRocks();
		createBoundaries();
		creatBird();
		createWater();
		createShooters();
		createIceberg();
		createLightning();
	}

	private void createBackground() {
		background = new Background(this, vbom);
		background.attachEntities(this);
	}

	private void createCastles() {
		float yPos = 290.0f;
		Sprite castleShieldLeft = new Sprite(60.0f, yPos, ResourcesManager.getInstance().castleShield_region1, vbom);
		getLayer(Layer.WATER).attachChild(castleShieldLeft);
		Sprite castleOverlayLeft = new Sprite(60.0f, yPos, ResourcesManager.getInstance().castleOverlay_region1, vbom);
		getLayer(Layer.WATER).attachChild(castleOverlayLeft);
		leftCastle = new Castle(60.0f, yPos, ResourcesManager.getInstance().castle1_region, vbom, physicsWorld,
				leftPlayer, castleShieldLeft, castleOverlayLeft);

		Sprite castleShieldRight = new Sprite(1230.0f - 6.0f, yPos,
				ResourcesManager.getInstance().castleShield_region2, vbom);
		getLayer(Layer.WATER).attachChild(castleShieldRight);
		Sprite castleOverlayRight = new Sprite(1230.0f, yPos, ResourcesManager.getInstance().castleOverlay_region2,
				vbom);
		getLayer(Layer.WATER).attachChild(castleOverlayRight);
		rightCastle = new Castle(1230.0f, yPos, ResourcesManager.getInstance().castle2_region, vbom, physicsWorld,
				rightPlayer, castleShieldRight, castleOverlayRight);
		getLayer(Layer.CASTLE).attachChild(leftCastle);
		getLayer(Layer.CASTLE).attachChild(rightCastle);

	}

	private void createRocks() {
		// Sprite rocksSprite = new Sprite(RESOLUTION.x / 2, RESOLUTION.y / 2,
		// ResourcesManager.getInstance().rocks_region, vbom);
		Sprite rocksSprite = new Sprite(0, 0, ResourcesManager.getInstance().rocks_region, vbom);
		rockAndHeartsSpriteBatch.setRocks(rocksSprite);
	}

	private void createBoundaries() {
		int boxMaxHeight = 3000;

		new CastleBoundary(leftPlayer, physicsWorld, LEFT_BOUND, boxMaxHeight / 2, 10, boxMaxHeight);
		new CastleBoundary(rightPlayer, physicsWorld, RIGHT_BOUND, boxMaxHeight / 2, 10, boxMaxHeight);
		new Boundary(physicsWorld, PLANET_CENTER.x, PLANET_CENTER.y, PLANET_RADIUS, PLANET_COLLISION_CATEGORY);
		new Boundary(physicsWorld, PLANET_CENTER.x, boxMaxHeight, RIGHT_BOUND - LEFT_BOUND, 10,
				BOUNDARY_COLLISION_CATEGORY);

		List<Vector2<Float>> leftRampVertices = new ArrayList<Vector2<Float>>();
		leftRampVertices.add(new Vector2<Float>((float) LEFT_BOUND, 250.0f));
		leftRampVertices.add(new Vector2<Float>((float) LEFT_TOP_CORNER, 183.0f));
		leftRampVertices.add(new Vector2<Float>(350.0f, -140.0f));
		leftRampVertices.add(new Vector2<Float>((float) LEFT_BOUND, -140.0f));

		List<Vector2<Float>> rightRampVertices = new ArrayList<Vector2<Float>>(); // make mirrored
		for (Vector2<Float> v : leftRampVertices) {
			rightRampVertices.add(new Vector2<Float>(RESOLUTION.x - v.x, v.y));
		}

		new CastleBoundary(leftPlayer, physicsWorld, leftRampVertices);
		new CastleBoundary(rightPlayer, physicsWorld, rightRampVertices);

	}

	private void creatBird() {
		magicBird = new MagicBird(800, 750, vbom, camera, physicsWorld);
		getLayer(Layer.BOAT).attachChild(magicBird);
	}

	private void createShooters() {
		Shooter shooter1 = new Shooter(175, 322, this, leftPlayer);
		shooter1.attach(this);
		Shooter shooter2 = new Shooter(1110, 327, this, rightPlayer);
		shooter2.attach(this);
	}

	private void createWater() {
		final FixtureDef defaultFixtureDef = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);
		final ParticleGroup particleGroup = PhysicsFactory.createParticleBox(physicsWorld, 640, 150, 800, 300, 0,
				BodyType.staticBody, defaultFixtureDef);
		particleGroup.delete(); // since it is not save anywhere
		ParticleSystem particleSystem = physicsWorld.getParticleSystem();
		int particleCount = particleSystem.getParticleCount();
		Debug.d("Particle count = " + particleCount);

		WaterEntity water = new WaterEntity(vbom, particleSystem);
		Entity layer = getLayer(Layer.WATER);
		layer.attachChild(water);
		layer.attachChild(new Sprite((int) camera.getCenterX(), (int) camera.getCenterY(), water.getTexture(), vbom,
				SpriteWaterShader.getInstance()));

		for (int i = 0; i < STABILIZATION_STEPS; i++) {
			// stabilize water
			physicsWorld.doStep();
		}
	}

	private void createIceberg() {
		icebergSpawn = new IcebergSpawner(this, vbom, physicsWorld);
		// Iceberg iceberg = new Iceberg(X, Y, this, lifeTime);
		// getLayer(Layer.FIREBALL).attachChild(iceberg);

	}

	void createLightning() {
		// Lightning lightning = new Lightning(X, Y, vbom, camera, physicsWorld);

		lightningSpawn = new LightningSpawner(this, vbom, physicsWorld);
		// getLayer(Layer.FIREBALL).attachChild(lightning);
	}

	public void clearScene() {
		engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				clearTouchAreas();
				clearUpdateHandlers();
				System.gc();
			}
		});
	}

	public void play() {

		leftPlayer.reset();
		rightPlayer.reset();
		state = State.PLAY;
	}

	private void endGame() {

		state = State.END;
		Boolean leftWon = null;
		if (rightPlayer.getLife() == 0 && leftPlayer.getLife() != 0) {
			rightCastle.setDead();
			leftCastle.setReset();
			leftWon = true;
			leftPlayer.increaseRoundsWon();
		} else if (rightPlayer.getLife() != 0 && leftPlayer.getLife() == 0) {
			leftWon = false;
			leftCastle.setDead();
			rightCastle.setReset();
			rightPlayer.increaseRoundsWon();
		}
		menu.showEnd(leftWon);
		for (Boat boat : leftPlayer.getActiveBoats()) {
			boat.setDeadState();
		}
		for (Boat boat : rightPlayer.getActiveBoats()) {
			boat.setDeadState();
		}

	}

	private void pauseGame() {

		state = State.PAUSED;
		menu.showPaused();
	}

	public void continueGame() {

		state = State.PLAY;
	}

	public Menu getMenu() {
		return menu;
	}

}

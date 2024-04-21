package com.BotS.BaseGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;

import com.BotS.BaseGame.GameScene.Layer;
import com.BotS.Physics.Vector2;
import com.BotS.Physics.Vector2.Vector2f;

public class Background implements GameConstants {

	private static final float SCROLL_SPEED = 0.5f;
	private static final float CLOUD_SCROLL_SPEED = SCROLL_SPEED * 1.3f;
	private double SUN_ANGULAR_SPEED;
	private float SUN_INITIAL_THETA;
	private float SUN_FINAL_THETA;
	private double SUN_DIST_FROM_CENTER;
	private static final float SCALE = 4.0f;
	private static final int BACKGROUND_WIDTH = (int) (2320 * SCALE);
	private static final int HALF_BACKGROUND_WIDTH = BACKGROUND_WIDTH / 2;
	private static final float LINE_OFFSET = RESOLUTION.y * (RESOLUTION.x / 2) / -PLANET_CENTER.y;
	public static final int SEASON_WIDTH = 2000;
	private static final int TEXT_PADDING = 30;
	private static final Vector2<Integer> TEXT_DEFAULT_POS = new Vector2<Integer>(TEXT_PADDING, RESOLUTION.y - 20);

	private Line divider;
	private Sprite back;
	private Sprite sun;
	protected SunraySpawner raySpawn;
	public SunRayEasterEgg easterEgg;
	private List<Sprite> springClouds = new ArrayList<Sprite>();
	private List<Sprite> summerClouds = new ArrayList<Sprite>();
	private List<Sprite> fallClouds = new ArrayList<Sprite>();
	private List<Sprite> winterClouds = new ArrayList<Sprite>();
	private List<Butterfly> butterflies = new ArrayList<Butterfly>();
	private Set<Sprite> movingClouds = new HashSet<Sprite>();
	private Set<Sprite> cloudsThatWillStop = new HashSet<Sprite>();
	private ParticleSnow snowEmitter;
	private ParticleLeaves leavesEmitter;
	private ParticlePetal petalEmitter;
	private long lastCloudFrame = -100000;
	private long curFrame = 0;
	private Random random = new Random();
	private float curSunTheta;
	private SeasonText currentSeasonText;
	private SeasonText nextSeasonText;
	private GameScene gameScene;
	private boolean gotEasterEggActive = false;

	public Background(GameScene pGameScene, VertexBufferObjectManager vbom) {
		gameScene = pGameScene;
		addBackground(vbom);
		addSun(vbom);
		addButterflies(vbom);
		addSnowEmitter(vbom);
		addLeavesEmitter(vbom);
		addPetalEmitter(vbom);
		addSpringClouds(vbom);
		addSummerClouds(vbom);
		addFallClouds(vbom);
		addWinterClouds(vbom);
		addDivisor(vbom);
		addText(vbom);

		spawnCloud(springClouds, true);
	}

	private void addText(VertexBufferObjectManager vbom) {
		currentSeasonText = new SeasonText(TEXT_DEFAULT_POS.x, TEXT_DEFAULT_POS.y, ResourcesManager.getInstance().font,
				"Season, this is the text area for season", new TextOptions(HorizontalAlign.LEFT), vbom);
		currentSeasonText.setAnchorCenter(0, 1);
		currentSeasonText.setText(Season.SPRING.getText());

		nextSeasonText = new SeasonText(0, 0, ResourcesManager.getInstance().font,
				"Season, this is the text area for season", new TextOptions(HorizontalAlign.LEFT), vbom);
		nextSeasonText.setAnchorCenter(0, 1);
		nextSeasonText.setVisible(false);
	}

	private class SeasonText extends Text {
		public SeasonText(final float pX, final float pY, final IFont pFont, final CharSequence pText,
				final TextOptions pTextOptions, final VertexBufferObjectManager pVertexBufferObjectManager) {
			super(pX, pY, pFont, pText, pTextOptions, pVertexBufferObjectManager);
		}

		private Season season;

		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
			if (pSceneTouchEvent.isActionUp()) {
				gotEasterEggActive = true;
				gameScene.unregisterTouchArea(currentSeasonText);
				gameScene.unregisterTouchArea(nextSeasonText);
				currentSeasonText.updateText();
				nextSeasonText.updateText();

				return true;
			}
			return false;
		}

		private void updateText() {
			if (gotEasterEggActive && season == Season.FALL) {
				setText(season.getText() + "... brace yourself!");
			} else if (gotEasterEggActive && season == Season.WINTER) {
				setText(season.getText() + " is coming");
			} else {
				setText(season.getText());
			}
			if (!gotEasterEggActive && (season == Season.FALL || season == Season.WINTER)) {
				gameScene.registerTouchArea(this);
			}
		}
	}

	private void addDivisor(VertexBufferObjectManager vbom) {
		divider = new Line(0.0f, 0.0f, 0.0f, 0.0f, vbom);
		divider.setLineWidth(10.0f);
		divider.setVisible(false);
		divider.setColor(Color.BLACK);
		divider.setAlpha(0.1f);
	}

	private void addBackground(VertexBufferObjectManager vbom) {
		back = new Sprite(HALF_BACKGROUND_WIDTH, RESOLUTION.y / 2.0f, ResourcesManager.getInstance().background_region,
				vbom, DrawType.DYNAMIC);
		back.setScale(SCALE);
	}

	public Sprite getSun() {
		return sun;
	}

	public SunRayEasterEgg getSunEasterEgg() {
		return easterEgg;
	}

	public SunraySpawner getSunraySpawner() {
		return raySpawn;
	}

	private void addSun(VertexBufferObjectManager vbom) {
		sun = new Sprite(0, 0, ResourcesManager.getInstance().sun_region, vbom, DrawType.DYNAMIC);
		sun.setVisible(false);
		double initialY = 500.0;
		SUN_INITIAL_THETA = (float) Math.atan2(initialY - PLANET_CENTER.y, RESOLUTION.x / 2);
		SUN_FINAL_THETA = (float) Math.atan2(initialY - PLANET_CENTER.y, -50.0 - sun.getWidth() - RESOLUTION.x / 2);
		double theta = SUN_FINAL_THETA - SUN_INITIAL_THETA;
		SUN_ANGULAR_SPEED = theta * SCROLL_SPEED / (RESOLUTION.x + SEASON_WIDTH);
		double xDist = RESOLUTION.x - PLANET_CENTER.x;
		double yDist = initialY - PLANET_CENTER.y;
		SUN_DIST_FROM_CENTER = Math.sqrt(xDist * xDist + yDist * yDist);

		raySpawn = new SunraySpawner(gameScene);
		easterEgg = new SunRayEasterEgg(5000, 5000, sun, gameScene);
		gameScene.registerTouchArea(easterEgg);
	}

	private void addSummerClouds(VertexBufferObjectManager vbom) {
		summerClouds
				.add(new Sprite(0, 0, ResourcesManager.getInstance().cloud_summer_0_region, vbom, DrawType.DYNAMIC));
		summerClouds
				.add(new Sprite(0, 0, ResourcesManager.getInstance().cloud_summer_1_region, vbom, DrawType.DYNAMIC));
		summerClouds
				.add(new Sprite(0, 0, ResourcesManager.getInstance().cloud_summer_2_region, vbom, DrawType.DYNAMIC));
		summerClouds
				.add(new Sprite(0, 0, ResourcesManager.getInstance().cloud_summer_3_region, vbom, DrawType.DYNAMIC));
		for (Sprite cloud : summerClouds) {
			cloud.setVisible(false);
		}
	}

	private void addSpringClouds(VertexBufferObjectManager vbom) {
		springClouds
				.add(new Sprite(0, 0, ResourcesManager.getInstance().cloud_spring_0_region, vbom, DrawType.DYNAMIC));
		springClouds
				.add(new Sprite(0, 0, ResourcesManager.getInstance().cloud_spring_1_region, vbom, DrawType.DYNAMIC));
		springClouds
				.add(new Sprite(0, 0, ResourcesManager.getInstance().cloud_spring_2_region, vbom, DrawType.DYNAMIC));
		for (Sprite cloud : springClouds) {
			cloud.setVisible(false);
		}
	}

	private void addFallClouds(VertexBufferObjectManager vbom) {
		fallClouds.add(new Sprite(0, 0, ResourcesManager.getInstance().cloud_fall_0_region, vbom, DrawType.DYNAMIC));
		fallClouds.add(new Sprite(0, 0, ResourcesManager.getInstance().cloud_fall_1_region, vbom, DrawType.DYNAMIC));
		fallClouds.add(new Sprite(0, 0, ResourcesManager.getInstance().cloud_fall_2_region, vbom, DrawType.DYNAMIC));
		for (Sprite cloud : fallClouds) {
			cloud.setVisible(false);
		}
	}

	private void addWinterClouds(VertexBufferObjectManager vbom) {
		winterClouds
				.add(new Sprite(0, 0, ResourcesManager.getInstance().cloud_winter_0_region, vbom, DrawType.DYNAMIC));
		winterClouds
				.add(new Sprite(0, 0, ResourcesManager.getInstance().cloud_winter_1_region, vbom, DrawType.DYNAMIC));
		winterClouds
				.add(new Sprite(0, 0, ResourcesManager.getInstance().cloud_winter_2_region, vbom, DrawType.DYNAMIC));
		for (Sprite cloud : winterClouds) {
			cloud.setVisible(false);
		}
	}

	private void addButterflies(VertexBufferObjectManager vbom) {

		butterflies.add(new Butterfly(RESOLUTION.x, RESOLUTION.y * 0.4f, vbom, 1));
		butterflies.add(new Butterfly(RESOLUTION.x * 1.1f, RESOLUTION.y * 0.5f, vbom, 1));
		butterflies.add(new Butterfly(RESOLUTION.x * 1.2f, RESOLUTION.y * 0.55f, vbom, 1));

		for (Butterfly b : butterflies) {
			b.setVisible(false);
		}

	}

	private void addSnowEmitter(VertexBufferObjectManager vbom) {
		snowEmitter = new ParticleSnow(RESOLUTION.x / 2, RESOLUTION.y, RESOLUTION.x, 1, vbom);
		snowEmitter.setParticlesSpawnEnabled(false);
	}

	private void addLeavesEmitter(VertexBufferObjectManager vbom) {
		leavesEmitter = new ParticleLeaves(RESOLUTION.x, RESOLUTION.y * 0.5f, 1f, RESOLUTION.y * 0.3f, vbom);
		leavesEmitter.setParticlesSpawnEnabled(false);
	}

	private void addPetalEmitter(VertexBufferObjectManager vbom) {
		petalEmitter = new ParticlePetal(RESOLUTION.x, RESOLUTION.y * 0.4f, 1f, RESOLUTION.y * 0.15f, vbom);
		petalEmitter.setParticlesSpawnEnabled(false);
	}

	public void attachEntities(GameScene gameScene) {
		Entity layer = gameScene.getLayer(Layer.BACKGROUND);
		layer.attachChild(back);
		layer.attachChild(divider);
		layer.attachChild(sun);
		easterEgg.attachTo(layer);

		Entity sunClouds = gameScene.getLayer(Layer.CLOUDS);
		for (Butterfly b : butterflies) {
			sunClouds.attachChild(b);
		}
		for (Sprite cloud : springClouds) {
			sunClouds.attachChild(cloud);
		}
		for (Sprite cloud : summerClouds) {
			sunClouds.attachChild(cloud);
		}
		for (Sprite cloud : fallClouds) {
			sunClouds.attachChild(cloud);
		}
		for (Sprite cloud : winterClouds) {
			sunClouds.attachChild(cloud);
		}
		Entity itemLayer = gameScene.getLayer(Layer.BACKGROUND_ITEMS);
		itemLayer.attachChild(leavesEmitter);
		itemLayer.attachChild(petalEmitter);
		itemLayer.attachChild(snowEmitter);
		itemLayer.attachChild(currentSeasonText);
		itemLayer.attachChild(nextSeasonText);
	}

	protected void onUpdate() {
		++curFrame;
		moveBackground();
		moveDivider();
		moveSprites();
		spawnSprites();
		updatePetalEmitter();
		updateLeavesEmitter();
		// updateButterflies();
		raySpawn.onUpdate();
	}

	private float moveBackground() {
		float newX = back.getX() - SCROLL_SPEED;
		float diff = newX + HALF_BACKGROUND_WIDTH - RESOLUTION.x;
		if (diff < 0) {
			newX = HALF_BACKGROUND_WIDTH - diff;
		}
		back.setX(newX);
		return newX;
	}

	private void moveDivider() {
		float modLeft = (HALF_BACKGROUND_WIDTH - back.getX()) % SEASON_WIDTH;
		if (modLeft < LINE_OFFSET) {
			divider.setVisible(true);
			divider.setPosition(PLANET_CENTER.x, PLANET_CENTER.y, -modLeft, RESOLUTION.y);
		} else if (modLeft + RESOLUTION.x > SEASON_WIDTH - LINE_OFFSET) {
			divider.setVisible(true);
			divider.setPosition(PLANET_CENTER.x, PLANET_CENTER.y, SEASON_WIDTH - modLeft, RESOLUTION.y);
		} else {
			divider.setVisible(false);
		}
	}

	private void moveSprites() {
		moveClouds();
		moveSun();
		moveSnowEmitter();
		moveText();
	}

	private void moveText() {
		if (nextSeasonText.isVisible()) {
			float newX = nextSeasonText.getX() - SCROLL_SPEED;
			if (newX < currentSeasonText.getX() + currentSeasonText.getWidth() + 2 * TEXT_PADDING) {
				currentSeasonText.setX(newX - (currentSeasonText.getWidth() + 2 * TEXT_PADDING));
			}
			if (newX - TEXT_DEFAULT_POS.x < 0) {
				SeasonText tmp = currentSeasonText;
				currentSeasonText = nextSeasonText;
				currentSeasonText.setPosition(TEXT_DEFAULT_POS.x, TEXT_DEFAULT_POS.y);
				nextSeasonText = tmp;
				nextSeasonText.setVisible(false);
			} else {
				nextSeasonText.setX(newX);
			}
		}
	}

	private void moveSnowEmitter() {
		if (snowEmitter.isParticlesSpawnEnabled()) {
			Season nextSeason = getSeasonOnBorder(false);
			Season currentSeason = getSeasonOnBorder(true);
			if (nextSeason != currentSeason) {
				float newX = snowEmitter.getParticleEmitter().getCenterX() - SCROLL_SPEED;
				if (newX + SEASON_WIDTH / 2 < 0) {
					snowEmitter.setParticlesSpawnEnabled(false);
				} else {
					snowEmitter.getParticleEmitter().setCenterX(newX);
				}
			}
		}
	}

	private void updatePetalEmitter() {
		if (petalEmitter.isParticlesSpawnEnabled()) {
			Season nextSeason = getSeasonOnBorder(false);
			Season currentSeason = getSeasonOnBorder(true);
			if (nextSeason != currentSeason && nextSeason != Season.SPRING) {
				petalEmitter.setParticlesSpawnEnabled(false);
			}
		}
	}

	private void updateLeavesEmitter() {
		if (leavesEmitter.isParticlesSpawnEnabled()) {
			Season nextSeason = getSeasonOnBorder(false);
			Season currentSeason = getSeasonOnBorder(true);
			if (nextSeason != currentSeason && nextSeason != Season.FALL) {

				leavesEmitter.setParticlesSpawnEnabled(false);
			}
		}
	}

	private void spawnButterflies() {

		Season nextSeason = getSeasonOnBorder(false);
		Season currentSeason = getSeasonOnBorder(true);

		if (nextSeason != currentSeason) {
			for (Butterfly b : butterflies) {
				b.setVisible(true);
			}
		}

	}

	private void moveSun() {
		if (sun.isVisible()) {
			curSunTheta += SUN_ANGULAR_SPEED;
			if (curSunTheta > SUN_FINAL_THETA) {
				sun.setVisible(false);
			} else {
				updateSunPos();
			}
		}
	}

	private void moveClouds() {
		for (Sprite cloud : movingClouds) {
			float newX = cloud.getX() - CLOUD_SCROLL_SPEED;
			if (newX + cloud.getWidth() < 0.0) {
				cloudsThatWillStop.add(cloud);
				cloud.setVisible(false);
			} else {
				cloud.setX(newX);
			}
		}
		if (cloudsThatWillStop.size() > 0) {
			movingClouds.removeAll(cloudsThatWillStop);
			cloudsThatWillStop.clear();
		}
	}

	private void updateSunPos() {
		sun.setPosition((float) (Math.cos(curSunTheta) * SUN_DIST_FROM_CENTER + PLANET_CENTER.x) + sun.getWidth() / 2,
				(float) (Math.sin(curSunTheta) * SUN_DIST_FROM_CENTER + PLANET_CENTER.y));
		// sun.setRotation((float) ((Math.PI / 2.0 - curSunTheta) * 180.0 / Math.PI)); no rotation... performance
	}

	public Season getSeasonOnBorder(boolean isLeftBorder) {
		float backGroundOffset = HALF_BACKGROUND_WIDTH - back.getX();
		int position = (int) (backGroundOffset + (isLeftBorder ? 0 : RESOLUTION.x));
		int positionMod = position / SEASON_WIDTH % 4;
		return Season.getSeason(positionMod);
	}

	private void spawnSprites() {
		Season nextSeason = getSeasonOnBorder(false);
		Season currentSeason = getSeasonOnBorder(true);
		if (nextSeason == Season.SUMMER) {
			spawnSun();
			spawnButterflies();
		}
		if (nextSeason == Season.WINTER) {
			spawnSnow();
		}
		if (nextSeason == Season.FALL) {
			spawnLeaves();
		}
		if (nextSeason != currentSeason) {
			spawnNextSeasonText(nextSeason);
		}
		if (curFrame - lastCloudFrame < 5 * 60) {
			return;
		}
		if (nextSeason == Season.SPRING) {
			spawnCloud(springClouds, false);
			spawnPetal();
		} else if (nextSeason == Season.SUMMER) {
			spawnCloud(summerClouds, false);
		} else if (nextSeason == Season.FALL) {
			spawnCloud(fallClouds, false);
		} else if (nextSeason == Season.WINTER) {
			spawnCloud(winterClouds, false);
		}
	}

	private void spawnNextSeasonText(Season nextSeason) {
		if (nextSeasonText.isVisible()) {
			return;
		}
		nextSeasonText.setPosition(RESOLUTION.x + TEXT_PADDING, TEXT_DEFAULT_POS.y);
		if (nextSeason == Season.SUMMER) {
			gotEasterEggActive = false;
		}
		nextSeasonText.season = nextSeason;
		nextSeasonText.updateText();
		nextSeasonText.setVisible(true);

	}

	private void spawnSun() {
		if (sun.isVisible()) {
			return;
		}
		curSunTheta = SUN_INITIAL_THETA;
		updateSunPos();
		sun.setVisible(true);

	}

	private void spawnSnow() {
		if (snowEmitter.isParticlesSpawnEnabled()) {
			return;
		}
		snowEmitter.getParticleEmitter().setCenterX(RESOLUTION.x + RESOLUTION.x / 2);
		snowEmitter.setParticlesSpawnEnabled(true);

	}

	private void spawnLeaves() {
		if (leavesEmitter.isParticlesSpawnEnabled()) {
			return;
		}
		leavesEmitter.setParticlesSpawnEnabled(true);

	}

	private void spawnPetal() {
		if (petalEmitter.isParticlesSpawnEnabled()) {
			return;
		}
		// petalEmitter.getParticleEmitter().setCenterX(RESOLUTION.x + RESOLUTION.x / 2);
		petalEmitter.setParticlesSpawnEnabled(true);

	}

	private void spawnCloud(List<Sprite> cloudList, boolean initial) {
		int availableClouds = 0;
		for (Sprite cloud : cloudList) {
			if (!cloud.isVisible()) {
				++availableClouds;
			}
		}
		if (availableClouds == 0) {
			return;
		}

		int pickedCloud = random.nextInt(availableClouds);

		int curAvailableCloud = 0;
		for (int i = 0; i < cloudList.size(); i++) {
			Sprite cloud = cloudList.get(i);
			if (cloud.isVisible()) {
				continue;
			}
			if (curAvailableCloud == pickedCloud) {
				if (initial) {
					cloud.setPosition((RESOLUTION.x + cloud.getWidth()) / 2, 500);
				} else {
					cloud.setPosition(RESOLUTION.x + cloud.getWidth() / 2, 500);
					lastCloudFrame = (long) (curFrame + cloud.getWidth() / CLOUD_SCROLL_SPEED);
				}
				cloud.setVisible(true);
				movingClouds.add(cloud);
				break;
			}
			++curAvailableCloud;
		}
	}

	public Season getSeasonAtPosition(float px, float py) {
		float backGroundOffset = HALF_BACKGROUND_WIDTH - back.getX();
		float position = px + backGroundOffset;
		int positionMod = (int) (position % SEASON_WIDTH);
		int positionDiv = (int) (position / SEASON_WIDTH);
		if (positionMod < LINE_OFFSET && px < PLANET_CENTER.x) {
			Vector2f divisorTan = new Vector2f(RESOLUTION.y - PLANET_CENTER.y, -(positionDiv * SEASON_WIDTH
					- backGroundOffset - PLANET_CENTER.x));
			Vector2f point = new Vector2f(px - PLANET_CENTER.x, py - PLANET_CENTER.y);
			if (divisorTan.dot(point) < 0) {
				positionDiv = positionDiv == 0 ? 3 : positionDiv - 1;
			}
		} else if (positionMod > SEASON_WIDTH - LINE_OFFSET && px >= PLANET_CENTER.x) {
			Vector2f divisorTan = new Vector2f(RESOLUTION.y - PLANET_CENTER.y, -((positionDiv + 1) * SEASON_WIDTH
					- backGroundOffset - PLANET_CENTER.x));
			Vector2f point = new Vector2f(px - PLANET_CENTER.x, py - PLANET_CENTER.y);
			if (divisorTan.dot(point) > 0) {
				positionDiv += 1;
			}
		}
		return Season.getSeason(positionDiv % 4);

	}
}

package com.BotS.BaseGame;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;

import com.BotS.BaseGame.GameScene.State;

public class Menu extends Entity implements GameConstants {

	private GameScene mGameScene;
	private MenuCover cover;
	private StatisticsCover leftStat, rightStat;
	private MainButton startButton;
	private MainButton endButton;
	private MainButton pauseButton;
	private BoatSpeedButton speed1;
	private BoatSpeedButton speed2;
	private TutorialButton tutButton;
	private AboutButton abtButton;
	private TutorialSwitchButton tutNext, tutBack;
	private TutorialCancelButton tutCancel;
	private AboutCancelButton abtCancel;
	private SoundVibrationButton soundOn, soundOff;
	private ResetButton resetButton;
	private Text speedText, overAllWins, result;
	private Text winLoseLeft, EnShipDestLeft, FrShipDestLeft, FBshotsLeft, SpPUsedLeft, accuracyLeft;
	private Text winLoseRight, EnShipDestRight, FrShipDestRight, FBshotsRight, SpPUsedRight, accuracyRight;
	private int finalSpeedIndex = 2, finalsoundSelectionIndex = 2, finalTutorialSelectionIndex = 0;
	private Text textMain, soundOnOff, resetScoreText;
	private long lastStateChange = 0;
	private long MIN_END_SCREEN_WAIT = 1000;
	private boolean flagDisableGameScene = false;
	private Sprite specialFirballTutorial, basicTutorial, seasonTutorial, about;

	public enum MainType {
		START, PAUSE, END
	};

	public Menu(GameScene pGameScene) {
		mGameScene = pGameScene;
		cover = new MenuCover(0, 0, RESOLUTION.x, RESOLUTION.y, mGameScene.vbom);
		attachChild(cover);
		leftStat = new StatisticsCover(25, 0.35f * RESOLUTION.y, 400, 350, mGameScene.vbom);
		attachChild(leftStat);
		rightStat = new StatisticsCover(RESOLUTION.x - 425, 0.35f * RESOLUTION.y, 400, 350, mGameScene.vbom);
		attachChild(rightStat);

		final float mainButtonWidth = RESOLUTION.x * 0.3f;
		final float mainButtonHeight = RESOLUTION.y * 0.10f;

		startButton = new MainButton(RESOLUTION.x * 0.5f, RESOLUTION.y * 0.4f, mainButtonWidth, mainButtonHeight,
				mGameScene.vbom, MainType.START, true);
		attachChild(startButton);

		endButton = new MainButton(RESOLUTION.x * 0.5f, RESOLUTION.y * 0.4f, mainButtonWidth, mainButtonHeight,
				mGameScene.vbom, MainType.END, false);
		attachChild(endButton);
		endButton.setVisible(false);

		pauseButton = new MainButton(RESOLUTION.x * 0.5f, RESOLUTION.y * 0.4f, mainButtonWidth, mainButtonHeight,
				mGameScene.vbom, MainType.PAUSE, false);
		attachChild(pauseButton);
		pauseButton.setVisible(false);

		overAllWins = new Text(RESOLUTION.x * 0.5f, RESOLUTION.y * 0.35f + 300, ResourcesManager.getInstance().font,
				"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
						HorizontalAlign.CENTER), mGameScene.vbom);
		overAllWins.setAnchorCenter(0.5f, 0.5f);
		overAllWins.setText("Overall wins:");
		overAllWins.setScale(1.0f);
		attachChild(overAllWins);

		result = new Text(RESOLUTION.x * 0.5f, RESOLUTION.y * 0.4f + 150, ResourcesManager.getInstance().largeFont,
				"0123456789- ", new TextOptions(HorizontalAlign.CENTER), mGameScene.vbom);
		result.setAnchorCenter(0.5f, 0.5f);
		result.setText(mGameScene.leftPlayer.getRoundsWon() + " - " + mGameScene.rightPlayer.getRoundsWon());
		result.setScale(1.0f);
		attachChild(result);

		int leftStatX = 50;

		winLoseLeft = new Text(225, 0.4f * RESOLUTION.y + 250, ResourcesManager.getInstance().font,
				"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
						HorizontalAlign.CENTER), mGameScene.vbom);
		winLoseLeft.setAnchorCenter(0.5f, 0.5f);
		winLoseLeft.setText("Winner!");
		winLoseLeft.setScale(1.0f);
		attachChild(winLoseLeft);

		EnShipDestLeft = new Text(leftStatX, 0.4f * RESOLUTION.y + 200, ResourcesManager.getInstance().font,
				"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
						HorizontalAlign.CENTER), mGameScene.vbom);
		EnShipDestLeft.setAnchorCenter(0f, 0.5f);
		EnShipDestLeft.setText("Enememy ships destroyed: " + mGameScene.leftPlayer.getDestEnemies());
		EnShipDestLeft.setScale(0.5f);
		attachChild(EnShipDestLeft);

		FrShipDestLeft = new Text(leftStatX, 0.4f * RESOLUTION.y + 150, ResourcesManager.getInstance().font,
				"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
						HorizontalAlign.CENTER), mGameScene.vbom);
		FrShipDestLeft.setAnchorCenter(0f, 0.5f);
		FrShipDestLeft.setText("Friendly ships destroyed: " + mGameScene.leftPlayer.getDestFriendly());
		FrShipDestLeft.setScale(0.5f);
		attachChild(FrShipDestLeft);

		FBshotsLeft = new Text(leftStatX, 0.4f * RESOLUTION.y + 100, ResourcesManager.getInstance().font,
				"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
						HorizontalAlign.CENTER), mGameScene.vbom);
		FBshotsLeft.setAnchorCenter(0f, 0.5f);
		FBshotsLeft.setText("Fireballs shot: " + mGameScene.leftPlayer.getShotsFired());
		FBshotsLeft.setScale(0.5f);
		attachChild(FBshotsLeft);

		SpPUsedLeft = new Text(leftStatX, 0.4f * RESOLUTION.y + 50, ResourcesManager.getInstance().font,
				"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
						HorizontalAlign.CENTER), mGameScene.vbom);
		SpPUsedLeft.setAnchorCenter(0f, 0.5f);
		SpPUsedLeft.setText("Special powers used: " + mGameScene.leftPlayer.getSpecialsUsed());
		SpPUsedLeft.setScale(0.5f);
		attachChild(SpPUsedLeft);

		accuracyLeft = new Text(leftStatX, 0.4f * RESOLUTION.y, ResourcesManager.getInstance().font,
				"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
						HorizontalAlign.CENTER), mGameScene.vbom);
		accuracyLeft.setAnchorCenter(0, 0.5f);
		float acc = 0;
		if (mGameScene.leftPlayer.getFireballsOnTarget() != 0) {
			acc = 100f * mGameScene.leftPlayer.getFireballsOnTarget() / mGameScene.leftPlayer.getShotsFired();
		}
		accuracyLeft.setText("Accuracy: " + String.format("%.1f", acc) + "%");
		accuracyLeft.setScale(0.5f);
		attachChild(accuracyLeft);

		int rightStatX = RESOLUTION.x - 400;

		winLoseRight = new Text(RESOLUTION.x - 225, 0.4f * RESOLUTION.y + 250, ResourcesManager.getInstance().font,
				"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
						HorizontalAlign.CENTER), mGameScene.vbom);
		winLoseRight.setAnchorCenter(0.5f, 0.5f);
		winLoseRight.setText("Winner!");
		winLoseRight.setScale(1.0f);
		attachChild(winLoseRight);

		EnShipDestRight = new Text(rightStatX, 0.4f * RESOLUTION.y + 200, ResourcesManager.getInstance().font,
				"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
						HorizontalAlign.CENTER), mGameScene.vbom);
		EnShipDestRight.setAnchorCenter(0f, 0.5f);
		EnShipDestRight.setText("Enememy ships destroyed: " + mGameScene.rightPlayer.getDestEnemies());
		EnShipDestRight.setScale(0.5f);
		attachChild(EnShipDestRight);

		FrShipDestRight = new Text(rightStatX, 0.4f * RESOLUTION.y + 150, ResourcesManager.getInstance().font,
				"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
						HorizontalAlign.CENTER), mGameScene.vbom);
		FrShipDestRight.setAnchorCenter(0f, 0.5f);
		FrShipDestRight.setText("Friendly ships destroyed: " + mGameScene.rightPlayer.getDestFriendly());
		FrShipDestRight.setScale(0.5f);
		attachChild(FrShipDestRight);

		FBshotsRight = new Text(rightStatX, 0.4f * RESOLUTION.y + 100, ResourcesManager.getInstance().font,
				"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
						HorizontalAlign.CENTER), mGameScene.vbom);
		FBshotsRight.setAnchorCenter(0f, 0.5f);
		FBshotsRight.setText("Fireballs shot: " + mGameScene.rightPlayer.getShotsFired());
		FBshotsRight.setScale(0.5f);
		attachChild(FBshotsRight);

		SpPUsedRight = new Text(rightStatX, 0.4f * RESOLUTION.y + 50, ResourcesManager.getInstance().font,
				"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
						HorizontalAlign.CENTER), mGameScene.vbom);
		SpPUsedRight.setAnchorCenter(0f, 0.5f);
		SpPUsedRight.setText("Special powers used: " + mGameScene.rightPlayer.getSpecialsUsed());
		SpPUsedRight.setScale(0.5f);
		attachChild(SpPUsedRight);

		accuracyRight = new Text(rightStatX, 0.4f * RESOLUTION.y, ResourcesManager.getInstance().font,
				"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
						HorizontalAlign.CENTER), mGameScene.vbom);
		accuracyRight.setAnchorCenter(0, 0.5f);
		acc = 0;
		if (mGameScene.rightPlayer.getFireballsOnTarget() != 0) {
			acc = 100f * mGameScene.rightPlayer.getFireballsOnTarget() / mGameScene.rightPlayer.getShotsFired();
		}
		accuracyRight.setText("Accuracy: " + String.format("%.1f", acc) + "%");
		accuracyRight.setScale(0.5f);
		attachChild(accuracyRight);

		final float speedButtonWidth = RESOLUTION.x * 0.08f;
		final float speedButtonHeight = RESOLUTION.y * 0.05f;

		speedText = new Text(RESOLUTION.x * 0.4f, RESOLUTION.y * 0.4f - 100, ResourcesManager.getInstance().font,
				"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
						HorizontalAlign.CENTER), mGameScene.vbom);
		speedText.setText("Ship Speed:");
		speedText.setAnchorCenter(1.0f, 0.5f);
		speedText.setScale(0.4f);
		attachChild(speedText);

		speed1 = new BoatSpeedButton(RESOLUTION.x * 0.45f, RESOLUTION.y * 0.4f - 100, speedButtonWidth,
				speedButtonHeight, mGameScene.vbom, 1);
		attachChild(speed1);
		speed2 = new BoatSpeedButton(RESOLUTION.x * 0.55f, RESOLUTION.y * 0.4f - 100, speedButtonWidth,
				speedButtonHeight, mGameScene.vbom, 2);
		attachChild(speed2);

		soundOnOff = new Text(RESOLUTION.x * 0.4f, RESOLUTION.y * 0.4f - 150, ResourcesManager.getInstance().font,
				"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
						HorizontalAlign.CENTER), mGameScene.vbom);
		soundOnOff.setText("Sound and Vibration:");
		soundOnOff.setAnchorCenter(1.0f, 0.5f);
		soundOnOff.setScale(0.4f);
		attachChild(soundOnOff);

		soundOff = new SoundVibrationButton(RESOLUTION.x * 0.45f, RESOLUTION.y * 0.4f - 150, speedButtonWidth,
				speedButtonHeight, mGameScene.vbom, 1);
		attachChild(soundOff);
		soundOn = new SoundVibrationButton(RESOLUTION.x * 0.55f, RESOLUTION.y * 0.4f - 150, speedButtonWidth,
				speedButtonHeight, mGameScene.vbom, 2);
		attachChild(soundOn);

		resetScoreText = new Text(RESOLUTION.x * 0.4f, RESOLUTION.y * 0.4f - 200, ResourcesManager.getInstance().font,
				"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
						HorizontalAlign.CENTER), mGameScene.vbom);
		resetScoreText.setText("Reset overall score:");
		resetScoreText.setAnchorCenter(1.0f, 0.5f);
		resetScoreText.setScale(0.4f);
		attachChild(resetScoreText);

		resetButton = new ResetButton(RESOLUTION.x * 0.5f, RESOLUTION.y * 0.4f - 200, speedButtonWidth,
				speedButtonHeight, mGameScene.vbom);
		attachChild(resetButton);
		hideStatistics();

		specialFirballTutorial = new Sprite(0, 0, ResourcesManager.getInstance().specialFireballTutorial,
				mGameScene.vbom);
		specialFirballTutorial.setAnchorCenter(0f, 0f);
		specialFirballTutorial.setVisible(false);
		attachChild(specialFirballTutorial);

		basicTutorial = new Sprite(0, 0, ResourcesManager.getInstance().basicTutorial, mGameScene.vbom);
		basicTutorial.setAnchorCenter(0f, 0f);
		basicTutorial.setVisible(false);
		attachChild(basicTutorial);

		seasonTutorial = new Sprite(0, 0, ResourcesManager.getInstance().seasonTutorial, mGameScene.vbom);
		seasonTutorial.setAnchorCenter(0f, 0f);
		seasonTutorial.setVisible(false);
		attachChild(seasonTutorial);

		about = new Sprite(0, 0, ResourcesManager.getInstance().about, mGameScene.vbom);
		about.setAnchorCenter(0f, 0f);
		about.setVisible(false);
		attachChild(about);

		tutButton = new TutorialButton(RESOLUTION.x * 0.8f, RESOLUTION.y * 0.4f - 100, RESOLUTION.x * 0.16f,
				speedButtonHeight, mGameScene.vbom);
		attachChild(tutButton);

		abtButton = new AboutButton(RESOLUTION.x * 0.8f, RESOLUTION.y * 0.4f - 200, RESOLUTION.x * 0.16f,
				speedButtonHeight, mGameScene.vbom);
		attachChild(abtButton);

		tutNext = new TutorialSwitchButton(RESOLUTION.x * 0.8f, RESOLUTION.y * 0.4f - 200, RESOLUTION.x * 0.08f,
				speedButtonHeight, mGameScene.vbom, 2);
		attachChild(tutNext);
		tutNext.setVisible(false);

		tutBack = new TutorialSwitchButton(RESOLUTION.x * 0.2f, RESOLUTION.y * 0.4f - 200, RESOLUTION.x * 0.08f,
				speedButtonHeight, mGameScene.vbom, 1);
		attachChild(tutBack);
		tutBack.setVisible(false);

		tutCancel = new TutorialCancelButton(RESOLUTION.x * 0.5f, RESOLUTION.y * 0.4f - 200, RESOLUTION.x * 0.16f,
				speedButtonHeight, mGameScene.vbom);
		attachChild(tutCancel);
		tutCancel.setVisible(false);

		abtCancel = new AboutCancelButton(RESOLUTION.x - 150, RESOLUTION.y * 0.4f - 250, RESOLUTION.x * 0.16f,
				speedButtonHeight, mGameScene.vbom);
		attachChild(abtCancel);
		abtCancel.setVisible(false);

	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {

		if (isVisible() && flagDisableGameScene == false && mGameScene.getState() == State.END) {

			mGameScene.getLeftPlayer().getFireball().resetToStart();
			mGameScene.getRightPlayer().getFireball().resetToStart();
			mGameScene.getLeftPlayer().getFireball().setflagSpell(false);
			mGameScene.getRightPlayer().getFireball().setflagSpell(false);
			// mGameScene.getLeftPlayer().getShooter().resetHandlePosition();
			// mGameScene.getRightPlayer().getShooter().resetHandlePosition();
			mGameScene.unregisterTouchArea(mGameScene.getLeftPlayer().getShooter().getHandle());
			mGameScene.unregisterTouchArea(mGameScene.getRightPlayer().getShooter().getHandle());
			mGameScene.unregisterTouchArea(mGameScene.getSunEasterEgg());
			mGameScene.getLeftPlayer().setTokenLevelOneCount(0);
			mGameScene.getRightPlayer().setTokenLevelOneCount(0);
			mGameScene.checkTokens(mGameScene.getLeftPlayer());
			mGameScene.checkTokens(mGameScene.getRightPlayer());
			flagDisableGameScene = true;

		}

		if (isVisible() && flagDisableGameScene == false && mGameScene.getState() == State.PAUSED) {

			mGameScene.unregisterIndicators(mGameScene.getLeftPlayer());
			mGameScene.unregisterIndicators(mGameScene.getRightPlayer());
			mGameScene.unregisterTouchArea(mGameScene.getSunEasterEgg());
			mGameScene.unregisterTouchArea(mGameScene.getLeftPlayer().getShooter().getHandle());
			mGameScene.unregisterTouchArea(mGameScene.getRightPlayer().getShooter().getHandle());
			flagDisableGameScene = true;

		}

		if (isVisible() && flagDisableGameScene == false && startButton.isVisible()) {

			mGameScene.unregisterTouchArea(mGameScene.getLeftPlayer().getShooter().getHandle());
			mGameScene.unregisterTouchArea(mGameScene.getRightPlayer().getShooter().getHandle());
			mGameScene.unregisterTouchArea(mGameScene.getSunEasterEgg());
			flagDisableGameScene = true;

		}

		if (!isVisible() && flagDisableGameScene == true) {

			mGameScene.registerTouchArea(mGameScene.getLeftPlayer().getShooter().getHandle());
			mGameScene.registerTouchArea(mGameScene.getRightPlayer().getShooter().getHandle());
			mGameScene.registerTouchArea(mGameScene.getSunEasterEgg());
			flagDisableGameScene = false;

		}

	}

	private class MenuCover extends Rectangle {
		public MenuCover(final float pX, final float pY, final float pWidth, final float pHeight,
				final VertexBufferObjectManager pVertexBufferObjectManager) {
			super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
			setAnchorCenter(0, 0);
			setColor(Color.BLACK);
			setAlpha(0.3f);
		}

	}

	private class StatisticsCover extends Button {
		public StatisticsCover(final float pX, final float pY, final float pWidth, final float pHeight,
				final VertexBufferObjectManager pVertexBufferObjectManager) {
			super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
			setAnchorCenter(0, 0);
			setColor(Color.BLACK);
			setAlpha(0.3f);
			showOutline();
			setOutlineWidth(5f);
		}

	}

	public void setBoatsSpeed() {

		mGameScene.unregisterTouchArea(speed1);
		mGameScene.unregisterTouchArea(speed2);
		speed1.setVisible(false);
		speed2.setVisible(false);

		float wind = 0.3f;
		if (finalSpeedIndex == 1) {
			wind = 0.15f;
		}

		mGameScene.getBoatSpawner().setInitialWindForce(wind);

		for (Boat boat : mGameScene.getLeftPlayer().getInactiveBoats()) {
			boat.setWindForce(wind);
		}
		for (Boat boat : mGameScene.getRightPlayer().getInactiveBoats()) {
			boat.setWindForce(wind);
		}

	}

	public long getFinalSoundSelectionIndex() {
		return finalsoundSelectionIndex;
	}

	private class MainButton extends Button {

		public MainButton(final float pX, final float pY, final float pWidth, final float pHeight,
				final VertexBufferObjectManager pVertexBufferObjectManager, MainType pMainType, boolean registerTouch) {
			super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
			textMain = new Text(pWidth * 0.5f, pHeight * 0.5f, ResourcesManager.getInstance().font,
					"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
							HorizontalAlign.CENTER), mGameScene.vbom);

			if (pMainType == MainType.START) {
				textMain.setText("Play");
			}
			if (pMainType == MainType.END) {
				textMain.setText("Play Again");
			}
			if (pMainType == MainType.PAUSE) {
				textMain.setText("Continue");
			}
			attachChild(textMain);

			if (registerTouch) {
				mGameScene.registerTouchArea(this);
			}

			setOutlineAlpha(0.15f);

		}

		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
			if (pSceneTouchEvent.isActionUp()) {

				if (mGameScene.getState() == State.START_MENU) {
					setBoatsSpeed();
					speedText.setVisible(false);
					mGameScene.unregisterTouchArea(this);
					mGameScene.unregisterTouchArea(resetButton);
					mGameScene.unregisterTouchArea(soundOn);
					mGameScene.unregisterTouchArea(soundOff);
					mGameScene.unregisterTouchArea(tutButton);
					mGameScene.unregisterTouchArea(abtButton);
					Menu.this.setVisible(false);
					mGameScene.play();
				} else if (mGameScene.getState() == State.END
						&& System.currentTimeMillis() - lastStateChange > MIN_END_SCREEN_WAIT) {
					setBoatsSpeed();
					speedText.setVisible(false);
					mGameScene.unregisterTouchArea(this);
					mGameScene.unregisterTouchArea(resetButton);
					mGameScene.unregisterTouchArea(soundOn);
					mGameScene.unregisterTouchArea(soundOff);
					mGameScene.unregisterTouchArea(abtButton);
					mGameScene.unregisterTouchArea(tutButton);
					Menu.this.setVisible(false);
					mGameScene.play();
				} else if (mGameScene.getState() == State.PAUSED
						&& System.currentTimeMillis() - lastStateChange > MIN_END_SCREEN_WAIT / 2) {
					mGameScene.registerIndicators(mGameScene.getLeftPlayer());
					mGameScene.registerIndicators(mGameScene.getRightPlayer());
					mGameScene.unregisterTouchArea(this);
					mGameScene.unregisterTouchArea(resetButton);
					mGameScene.unregisterTouchArea(soundOn);
					mGameScene.unregisterTouchArea(soundOff);
					mGameScene.unregisterTouchArea(abtButton);
					mGameScene.unregisterTouchArea(tutButton);
					Menu.this.setVisible(false);
					mGameScene.continueGame();
				}
			}
			return true;
		}
	}

	private class BoatSpeedButton extends Button {

		private float speedIndex;

		public BoatSpeedButton(final float pX, final float pY, final float pWidth, final float pHeight,
				final VertexBufferObjectManager pVertexBufferObjectManager, int speedIndex) {
			super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);

			setColor(Color.BLACK);

			mGameScene.registerTouchArea(this);

			if (speedIndex == 1) {
				setAlpha(0.2f);
				Text text1 = new Text(pWidth * 0.5f, pHeight * 0.5f, ResourcesManager.getInstance().font,
						"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
								HorizontalAlign.CENTER), mGameScene.vbom);
				text1.setScale(0.4f);
				attachChild(text1);
				text1.setText("Slow");
				hideOutline();
			}

			if (speedIndex == 2) {
				setAlpha(0.2f);
				Text text2 = new Text(pWidth * 0.5f, pHeight * 0.5f, ResourcesManager.getInstance().font,
						"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
								HorizontalAlign.CENTER), mGameScene.vbom);
				text2.setScale(0.4f);
				attachChild(text2);
				text2.setText("Fast");
				showOutline();
			}

			setOutlineAlpha(0.5f);

			this.speedIndex = speedIndex;
		}

		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
			if (pSceneTouchEvent.isActionUp()) {

				if (speedIndex == 1) {

					speed2.hideOutline();
					speed1.showOutline();
					finalSpeedIndex = 1;

				}

				if (speedIndex == 2) {

					speed1.hideOutline();
					speed2.showOutline();
					finalSpeedIndex = 2;

				}

			}
			return true;
		}
	}

	private class TutorialButton extends Button {

		public TutorialButton(final float pX, final float pY, final float pWidth, final float pHeight,
				final VertexBufferObjectManager pVertexBufferObjectManager) {
			super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);

			setColor(Color.BLACK);

			mGameScene.registerTouchArea(this);

			setAlpha(0.2f);
			Text text1 = new Text(pWidth * 0.5f, pHeight * 0.5f, ResourcesManager.getInstance().font,
					"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
							HorizontalAlign.CENTER), mGameScene.vbom);
			text1.setScale(0.4f);
			attachChild(text1);
			text1.setText("How to play");
			hideOutline();
		}

		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
			if (pSceneTouchEvent.isActionUp()) {
				showTutorial();
			}
			return true;
		}
	}

	private class AboutButton extends Button {

		public AboutButton(final float pX, final float pY, final float pWidth, final float pHeight,
				final VertexBufferObjectManager pVertexBufferObjectManager) {
			super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);

			setColor(Color.BLACK);

			mGameScene.registerTouchArea(this);

			setAlpha(0.2f);
			Text text1 = new Text(pWidth * 0.5f, pHeight * 0.5f, ResourcesManager.getInstance().font,
					"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
							HorizontalAlign.CENTER), mGameScene.vbom);
			text1.setScale(0.4f);
			attachChild(text1);
			text1.setText("Credits");
			hideOutline();
		}

		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
			if (pSceneTouchEvent.isActionUp()) {
				showAbout();
			}
			return true;
		}
	}

	private class AboutCancelButton extends Button {

		public AboutCancelButton(final float pX, final float pY, final float pWidth, final float pHeight,
				final VertexBufferObjectManager pVertexBufferObjectManager) {
			super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);

			setColor(Color.BLACK);

			// mGameScene.registerTouchArea(this);

			setAlpha(0.2f);
			Text text1 = new Text(pWidth * 0.5f, pHeight * 0.5f, ResourcesManager.getInstance().font,
					"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
							HorizontalAlign.CENTER), mGameScene.vbom);
			text1.setScale(0.4f);
			attachChild(text1);
			text1.setText("Back to menu");
			showOutline();
		}

		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
			if (pSceneTouchEvent.isActionUp()) {
				hideAbout();
			}
			return true;
		}
	}

	private class ResetButton extends Button {

		public ResetButton(final float pX, final float pY, final float pWidth, final float pHeight,
				final VertexBufferObjectManager pVertexBufferObjectManager) {
			super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);

			setColor(Color.BLACK);

			mGameScene.registerTouchArea(this);

			setAlpha(0.2f);
			Text text1 = new Text(pWidth * 0.5f, pHeight * 0.5f, ResourcesManager.getInstance().font,
					"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
							HorizontalAlign.CENTER), mGameScene.vbom);
			text1.setScale(0.4f);
			attachChild(text1);
			text1.setText("Reset");
			hideOutline();
		}

		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
			if (pSceneTouchEvent.isActionUp()) {
				mGameScene.rightPlayer.resetRoundsWon();
				mGameScene.leftPlayer.resetRoundsWon();
				updateStatistics();
			}
			return true;
		}
	}

	private class TutorialCancelButton extends Button {

		public TutorialCancelButton(final float pX, final float pY, final float pWidth, final float pHeight,
				final VertexBufferObjectManager pVertexBufferObjectManager) {
			super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);

			setColor(Color.BLACK);

			// mGameScene.registerTouchArea(this);

			setAlpha(0.2f);
			Text text1 = new Text(pWidth * 0.5f, pHeight * 0.5f, ResourcesManager.getInstance().font,
					"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
							HorizontalAlign.CENTER), mGameScene.vbom);
			text1.setScale(0.4f);
			attachChild(text1);
			text1.setText("Back to menu");
			showOutline();
		}

		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
			if (pSceneTouchEvent.isActionUp()) {
				hideTutorial();
			}
			return true;
		}
	}

	private class SoundVibrationButton extends Button {

		private float SoundselectionIndex;

		public SoundVibrationButton(final float pX, final float pY, final float pWidth, final float pHeight,
				final VertexBufferObjectManager pVertexBufferObjectManager, int SoundselectionIndex) {
			super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);

			setColor(Color.BLACK);

			mGameScene.registerTouchArea(this);

			if (SoundselectionIndex == 1) {
				setAlpha(0.2f);
				Text text1 = new Text(pWidth * 0.5f, pHeight * 0.5f, ResourcesManager.getInstance().font,
						"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
								HorizontalAlign.CENTER), mGameScene.vbom);
				text1.setScale(0.4f);
				attachChild(text1);
				text1.setText("Off");
				hideOutline();
			}

			if (SoundselectionIndex == 2) {
				setAlpha(0.2f);
				Text text2 = new Text(pWidth * 0.5f, pHeight * 0.5f, ResourcesManager.getInstance().font,
						"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
								HorizontalAlign.CENTER), mGameScene.vbom);
				text2.setScale(0.4f);
				attachChild(text2);
				text2.setText("On");
				showOutline();
			}

			setOutlineAlpha(0.5f);

			this.SoundselectionIndex = SoundselectionIndex;
		}

		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
			if (pSceneTouchEvent.isActionUp()) {

				if (SoundselectionIndex == 1) {

					soundOn.hideOutline();
					soundOff.showOutline();
					finalsoundSelectionIndex = 1;
					ResourcesManager.getInstance().engine.getSoundManager().setMasterVolume(0f);

				}

				if (SoundselectionIndex == 2) {

					soundOff.hideOutline();
					soundOn.showOutline();
					finalsoundSelectionIndex = 2;
					ResourcesManager.getInstance().engine.getSoundManager().setMasterVolume(1f);

				}

			}
			return true;
		}
	}

	private class TutorialSwitchButton extends Button {

		private float nextBackIndex;

		public TutorialSwitchButton(final float pX, final float pY, final float pWidth, final float pHeight,
				final VertexBufferObjectManager pVertexBufferObjectManager, int a_nextBackIndex) {
			super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);

			nextBackIndex = a_nextBackIndex;
			setColor(Color.BLACK);

			// mGameScene.registerTouchArea(this);

			if (nextBackIndex == 1) {
				setAlpha(0.2f);
				Text text1 = new Text(pWidth * 0.5f, pHeight * 0.5f, ResourcesManager.getInstance().font,
						"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
								HorizontalAlign.CENTER), mGameScene.vbom);
				text1.setScale(0.4f);
				attachChild(text1);
				text1.setText("Back");
				showOutline();
			}

			if (nextBackIndex == 2) {
				setAlpha(0.2f);
				Text text2 = new Text(pWidth * 0.5f, pHeight * 0.5f, ResourcesManager.getInstance().font,
						"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", new TextOptions(
								HorizontalAlign.CENTER), mGameScene.vbom);
				text2.setScale(0.4f);
				attachChild(text2);
				text2.setText("Next");
				showOutline();
			}

			setOutlineAlpha(0.5f);
		}

		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
			if (pSceneTouchEvent.isActionUp()) {

				if (nextBackIndex == 1) {
					finalTutorialSelectionIndex--;
					if (finalTutorialSelectionIndex < 0) {
						finalTutorialSelectionIndex = 3 - finalTutorialSelectionIndex;
					}
				}

				if (nextBackIndex == 2) {
					finalTutorialSelectionIndex++;
					finalTutorialSelectionIndex = finalTutorialSelectionIndex % 3;
				}

				changeTutorial();
			}
			return true;
		}
	}

	private void hideStatistics() {
		leftStat.setVisible(false);
		rightStat.setVisible(false);
		winLoseLeft.setVisible(false);
		EnShipDestLeft.setVisible(false);
		FrShipDestLeft.setVisible(false);
		FBshotsLeft.setVisible(false);
		SpPUsedLeft.setVisible(false);
		accuracyLeft.setVisible(false);
		winLoseRight.setVisible(false);
		EnShipDestRight.setVisible(false);
		FrShipDestRight.setVisible(false);
		FBshotsRight.setVisible(false);
		SpPUsedRight.setVisible(false);
		accuracyRight.setVisible(false);
	}

	private void showStatistics() {
		leftStat.setVisible(true);
		rightStat.setVisible(true);
		winLoseLeft.setVisible(true);
		EnShipDestLeft.setVisible(true);
		FrShipDestLeft.setVisible(true);
		FBshotsLeft.setVisible(true);
		SpPUsedLeft.setVisible(true);
		accuracyLeft.setVisible(true);
		winLoseRight.setVisible(true);
		EnShipDestRight.setVisible(true);
		FrShipDestRight.setVisible(true);
		FBshotsRight.setVisible(true);
		SpPUsedRight.setVisible(true);
		accuracyRight.setVisible(true);
	}

	public void updateStatistics() {
		result.setText(mGameScene.leftPlayer.getRoundsWon() + " - " + mGameScene.rightPlayer.getRoundsWon());
		EnShipDestLeft.setText("Enememy ships destroyed: " + mGameScene.leftPlayer.getDestEnemies());
		FrShipDestLeft.setText("Friendly ships destroyed: " + mGameScene.leftPlayer.getDestFriendly());
		FBshotsLeft.setText("Fireballs shot: " + mGameScene.leftPlayer.getShotsFired());
		SpPUsedLeft.setText("Special powers used: " + mGameScene.leftPlayer.getSpecialsUsed());
		float acc = 0;
		if (mGameScene.leftPlayer.getFireballsOnTarget() != 0) {
			acc = 100f * mGameScene.leftPlayer.getFireballsOnTarget() / mGameScene.leftPlayer.getShotsFired();
		}
		accuracyLeft.setText("Accuracy: " + String.format("%.1f", acc) + "%");
		EnShipDestRight.setText("Enememy ships destroyed: " + mGameScene.rightPlayer.getDestEnemies());
		FrShipDestRight.setText("Friendly ships destroyed: " + mGameScene.rightPlayer.getDestFriendly());
		FBshotsRight.setText("Fireballs shot: " + mGameScene.rightPlayer.getShotsFired());
		SpPUsedRight.setText("Special powers used: " + mGameScene.rightPlayer.getSpecialsUsed());

		acc = 0;
		if (mGameScene.rightPlayer.getFireballsOnTarget() != 0) {
			acc = 100f * mGameScene.rightPlayer.getFireballsOnTarget() / mGameScene.rightPlayer.getShotsFired();
		}
		accuracyRight.setText("Accuracy: " + String.format("%.1f", acc) + "%");
	}

	public void changeTutorial() {
		if (finalTutorialSelectionIndex == 0) {
			basicTutorial.setVisible(true);
			seasonTutorial.setVisible(false);
			specialFirballTutorial.setVisible(false);
		} else if (finalTutorialSelectionIndex == 1) {
			basicTutorial.setVisible(false);
			seasonTutorial.setVisible(false);
			specialFirballTutorial.setVisible(true);
		} else if (finalTutorialSelectionIndex == 2) {
			basicTutorial.setVisible(false);
			seasonTutorial.setVisible(true);
			specialFirballTutorial.setVisible(false);
		}
	}

	public void showTutorial() {
		finalTutorialSelectionIndex = 0;
		changeTutorial();
		tutCancel.setVisible(true);
		tutNext.setVisible(true);
		tutBack.setVisible(true);
		mGameScene.registerTouchArea(tutCancel);
		mGameScene.registerTouchArea(tutNext);
		mGameScene.registerTouchArea(tutBack);

		mGameScene.unregisterTouchArea(startButton);
		mGameScene.unregisterTouchArea(endButton);
		mGameScene.unregisterTouchArea(pauseButton);
		mGameScene.unregisterTouchArea(speed1);
		mGameScene.unregisterTouchArea(speed2);
		mGameScene.unregisterTouchArea(resetButton);
		mGameScene.unregisterTouchArea(soundOn);
		mGameScene.unregisterTouchArea(soundOff);
		mGameScene.unregisterTouchArea(tutButton);
		mGameScene.unregisterTouchArea(abtButton);
		tutButton.setVisible(false);
		abtButton.setVisible(false);
	}

	public void hideTutorial() {
		basicTutorial.setVisible(false);
		seasonTutorial.setVisible(false);
		specialFirballTutorial.setVisible(false);
		tutCancel.setVisible(false);
		tutNext.setVisible(false);
		tutBack.setVisible(false);
		mGameScene.unregisterTouchArea(tutCancel);
		mGameScene.unregisterTouchArea(tutNext);
		mGameScene.unregisterTouchArea(tutBack);
		if (mGameScene.getState() == State.START_MENU) {
			mGameScene.registerTouchArea(startButton);
			mGameScene.registerTouchArea(resetButton);
			mGameScene.registerTouchArea(soundOn);
			mGameScene.registerTouchArea(soundOff);
			mGameScene.registerTouchArea(tutButton);
			mGameScene.registerTouchArea(abtButton);
			mGameScene.registerTouchArea(speed1);
			mGameScene.registerTouchArea(speed2);

		} else if (mGameScene.getState() == State.END) {
			startButton.setVisible(false);
			pauseButton.setVisible(false);
			mGameScene.registerTouchArea(endButton);
			endButton.setVisible(true);
			speedText.setVisible(true);
			mGameScene.registerTouchArea(speed1);
			speed1.setVisible(true);
			mGameScene.registerTouchArea(speed2);
			speed2.setVisible(true);
			setVisible(true);
			mGameScene.registerTouchArea(resetButton);
			mGameScene.registerTouchArea(soundOn);
			mGameScene.registerTouchArea(soundOff);
			mGameScene.registerTouchArea(tutButton);
			mGameScene.registerTouchArea(abtButton);
			updateStatistics();
			showStatistics();

		} else if (mGameScene.getState() == State.PAUSED) {
			showPaused();
		}
		tutButton.setVisible(true);
		abtButton.setVisible(true);
	}

	public void showAbout() {

		about.setVisible(true);
		abtCancel.setVisible(true);
		mGameScene.registerTouchArea(abtCancel);
		mGameScene.unregisterTouchArea(startButton);
		mGameScene.unregisterTouchArea(endButton);
		mGameScene.unregisterTouchArea(pauseButton);
		mGameScene.unregisterTouchArea(speed1);
		mGameScene.unregisterTouchArea(speed2);
		mGameScene.unregisterTouchArea(resetButton);
		mGameScene.unregisterTouchArea(soundOn);
		mGameScene.unregisterTouchArea(soundOff);
		mGameScene.unregisterTouchArea(abtButton);
		mGameScene.unregisterTouchArea(tutButton);
		tutButton.setVisible(false);
		abtButton.setVisible(false);
	}

	public void hideAbout() {
		about.setVisible(false);
		abtCancel.setVisible(false);
		mGameScene.unregisterTouchArea(abtCancel);

		if (mGameScene.getState() == State.START_MENU) {
			mGameScene.registerTouchArea(startButton);
			mGameScene.registerTouchArea(resetButton);
			mGameScene.registerTouchArea(soundOn);
			mGameScene.registerTouchArea(soundOff);
			mGameScene.registerTouchArea(abtButton);
			mGameScene.registerTouchArea(tutButton);
			mGameScene.registerTouchArea(speed1);
			mGameScene.registerTouchArea(speed2);

		} else if (mGameScene.getState() == State.END) {
			startButton.setVisible(false);
			pauseButton.setVisible(false);
			mGameScene.registerTouchArea(endButton);
			endButton.setVisible(true);
			speedText.setVisible(true);
			mGameScene.registerTouchArea(speed1);
			speed1.setVisible(true);
			mGameScene.registerTouchArea(speed2);
			speed2.setVisible(true);
			setVisible(true);
			mGameScene.registerTouchArea(resetButton);
			mGameScene.registerTouchArea(soundOn);
			mGameScene.registerTouchArea(soundOff);
			mGameScene.registerTouchArea(abtButton);
			mGameScene.registerTouchArea(tutButton);
			updateStatistics();
			showStatistics();

		} else if (mGameScene.getState() == State.PAUSED) {
			showPaused();
		}
		abtButton.setVisible(true);
		tutButton.setVisible(true);
	}

	public void showEnd(Boolean leftPlayerWinner) {
		lastStateChange = System.currentTimeMillis();

		startButton.setVisible(false);
		pauseButton.setVisible(false);
		mGameScene.registerTouchArea(endButton);
		endButton.setVisible(true);
		speedText.setVisible(true);
		mGameScene.registerTouchArea(speed1);
		speed1.setVisible(true);
		mGameScene.registerTouchArea(speed2);
		speed2.setVisible(true);
		setVisible(true);
		mGameScene.registerTouchArea(resetButton);
		mGameScene.registerTouchArea(soundOn);
		mGameScene.registerTouchArea(soundOff);
		mGameScene.registerTouchArea(tutButton);
		tutButton.setVisible(true);
		mGameScene.registerTouchArea(abtButton);
		abtButton.setVisible(true);
		updateStatistics();
		if (leftPlayerWinner == null) {
			winLoseLeft.setText("Draw");
			winLoseRight.setText("Draw");
		} else if (leftPlayerWinner == Boolean.TRUE) {
			winLoseLeft.setText("Winner!");
			winLoseRight.setText("Loser");
		} else {
			winLoseLeft.setText("Loser");
			winLoseRight.setText("Winner!");
		}
		showStatistics();

	}

	public void showPaused() {
		lastStateChange = System.currentTimeMillis();
		startButton.setVisible(false);
		endButton.setVisible(false);
		mGameScene.registerTouchArea(pauseButton);
		pauseButton.setVisible(true);
		mGameScene.registerTouchArea(resetButton);
		mGameScene.registerTouchArea(soundOn);
		mGameScene.registerTouchArea(soundOff);
		mGameScene.registerTouchArea(tutButton);
		tutButton.setVisible(true);
		mGameScene.registerTouchArea(abtButton);
		abtButton.setVisible(true);
		setVisible(true);
		updateStatistics();
		winLoseLeft.setText("");
		winLoseRight.setText("");
		showStatistics();
	}
}

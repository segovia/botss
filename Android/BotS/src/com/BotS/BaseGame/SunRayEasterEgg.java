package com.BotS.BaseGame;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

public class SunRayEasterEgg extends Sprite implements GameConstants {

	private static final long blinkFrames = 10;
	private Sprite sun, frownFace, eyes;
	private boolean SunTouched;
	private int raysReleasedCount = 0;
	private long closedEyesOnStep = 0;
	private long curStep = 0;

	public SunRayEasterEgg(float pX, float pY, Sprite a_sun, GameScene gameScene) {

		super(pX, pY, ResourcesManager.getInstance().sunRayEasterEggSmile_region, gameScene.vbom);
		sun = a_sun;
		setVisible(false);
		frownFace = new Sprite(pX, pY, ResourcesManager.getInstance().sunRayEasterEggFrown_region, gameScene.vbom);
		eyes = new Sprite(pX, pY, ResourcesManager.getInstance().sunRayEasterEggEyes_region, gameScene.vbom);
		eyes.setAnchorCenter(0.4f, 0.1f);
		eyes.setVisible(false);
		frownFace.setAnchorCenter(0.4f, 0.1f);
		frownFace.setVisible(false);
		setAnchorCenter(0.4f, 0.1f);
		setX(sun.getX());
		setY(sun.getY());
		SunTouched = false;
	}

	public void attachTo(Entity layer) {
		layer.attachChild(this);
		layer.attachChild(frownFace);
		layer.attachChild(eyes);
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		closeEyes();
		return true;
	}

	private void openEyes() {
		setVisible(true);
		frownFace.setVisible(false);
		SunTouched = true;
	}

	private void closeEyes() {
		setVisible(false);
		frownFace.setVisible(true);
		if (curStep - closedEyesOnStep > blinkFrames) {
			ResourcesManager.getInstance().Meow.play();
		}
		closedEyesOnStep = curStep;
		SunTouched = true;
	}

	public boolean isSunTouched() {
		return SunTouched;
	}

	public void releaseRay(float angle) {
		setVisible(false);
		frownFace.setVisible(false);
		eyes.setVisible(true);
		eyes.setAnchorCenter(0.45f, 0.6f);
		eyes.setRotation(angle);
		eyes.setFlippedVertical(true);
		++raysReleasedCount;
	}

	public void resetSun() {
		setVisible(SunTouched);
		frownFace.setVisible(false);
		eyes.setVisible(false);
		eyes.setRotation(0.0f);
		eyes.setFlippedVertical(false);
		if (raysReleasedCount > 0) {
			--raysReleasedCount;
		}
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		++curStep;
		if (!sun.isVisible()) {
			setVisible(false);
			SunTouched = false;
		} else {
			if (SunTouched && raysReleasedCount == 0 && curStep - closedEyesOnStep > blinkFrames) {
				openEyes();
			}
			updatePosition();
		}

	}

	private void updatePosition() {
		setX(sun.getX());
		setY(sun.getY());
		frownFace.setX(sun.getX());
		frownFace.setY(sun.getY());
		eyes.setX(sun.getX());
		eyes.setY(sun.getY());
	}
}

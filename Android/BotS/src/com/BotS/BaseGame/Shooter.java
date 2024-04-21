package com.BotS.BaseGame;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.BotS.BaseGame.GameScene.Layer;
import com.BotS.BaseGame.Player.Team;
import com.BotS.Physics.Vector2;
import com.BotS.Physics.Vector2.Vector2f;

public class Shooter implements GameConstants {

	private static final float AIM_RADIUS = 100.0f;
	private static final float AIM_RADIUS_SQ = AIM_RADIUS * AIM_RADIUS;
	private Vector2<Integer> initialPos;
	private Vector2f fireDistToAxis;
	private Sprite axis;
	private Handle handle;
	private Fireball fireball;
	public State state;
	private Sprite sorcerer;
	private Line line;
	private Sprite arrow;

	public enum State {
		IDLE, DRAGGING, FOLLOWING;
	};

	public Shooter(int pX, int pY, GameScene gameScene, Player player) {
		initialPos = new Vector2<Integer>(pX, pY);
		Vector2<Integer> sorcererPos = new Vector2<Integer>(120, 295);
		line = new Line(-100, -100, -100, -100, gameScene.vbom);
		line.setLineWidth(6);
		line.setAlpha(0.5f);
		arrow = new Sprite(-100, -100, ResourcesManager.getInstance().arrow_region, gameScene.vbom);
		arrow.setAlpha(0.5f);
		if (player.getTeam() == Team.LEFT) {
			axis = new Sprite(pX, pY, ResourcesManager.getInstance().axis_green_region, gameScene.vbom);
			handle = new Handle(pX, pY, ResourcesManager.getInstance().handle_green_region, gameScene.vbom);
			sorcerer = new Sprite(sorcererPos.x, sorcererPos.y, ResourcesManager.getInstance().sorcerer_purple_region,
					gameScene.vbom);
			sorcerer.setScaleX(-1.0f);
		} else {
			axis = new Sprite(pX, pY, ResourcesManager.getInstance().axis_red_region, gameScene.vbom);
			handle = new Handle(pX, pY, ResourcesManager.getInstance().handle_red_region, gameScene.vbom);
			sorcerer = new Sprite(RESOLUTION.x - sorcererPos.x + 5, sorcererPos.y + 5,
					ResourcesManager.getInstance().sorcerer_red_region, gameScene.vbom);
		}

		Sprite fireballSpellLevel1Sprite = new Sprite(pX, pY, ResourcesManager.getInstance().fireBall_blue_r_region,
				gameScene.vbom);
		Sprite fireballSpellLevel2Sprite = new Sprite(pX, pY, ResourcesManager.getInstance().fireBall_green_r_region,
				gameScene.vbom);
		Sprite fireballSpellLevel3Sprite = new Sprite(pX, pY, ResourcesManager.getInstance().fireBall_purple_r_region,
				gameScene.vbom);
		fireball = new Fireball(pX, pY, gameScene.vbom, gameScene.physicsWorld, player, fireballSpellLevel1Sprite,
				fireballSpellLevel2Sprite, fireballSpellLevel3Sprite);
		state = State.IDLE;
		player.setFireball(fireball);
		player.setShooter(this);
		gameScene.registerTouchArea(handle);
	}

	public Handle getHandle() {

		return handle;

	}

	public Fireball getFireball() {
		return fireball;
	}

	public void attach(GameScene gamescene) {
		Entity tailLayer = gamescene.getLayer(Layer.FIREBALL_TAIL);
		tailLayer.attachChild(line);
		tailLayer.attachChild(fireball.getTail1());
		tailLayer.attachChild(fireball.getTail2());
		tailLayer.attachChild(fireball.getTail3());
		tailLayer.attachChild(fireball.getTail4());

		Entity fireballLayer = gamescene.getLayer(Layer.FIREBALL);
		fireballLayer.attachChild(arrow);
		fireballLayer.attachChild(axis);
		fireballLayer.attachChild(handle);
		fireballLayer.attachChild(fireball);
		fireballLayer.attachChild(fireball.fireballSpellLevel1);
		fireballLayer.attachChild(fireball.fireballSpellLevel2);
		fireballLayer.attachChild(fireball.fireballSpellLevel3);
		gamescene.getLayer(Layer.SORCERER).attachChild(sorcerer);

	}

	private class Handle extends Sprite {
		public Handle(final float pX, final float pY, final ITextureRegion pTextureRegion,
				final VertexBufferObjectManager pVertexBufferObjectManager) {
			super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		}

		@Override
		protected void onManagedUpdate(float pSecondsElapsed) {
			Shooter.this.onManagedUpdate();
		}

		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
			if (pSceneTouchEvent.isActionDown() || pSceneTouchEvent.isActionMove()) {
				state = State.DRAGGING;
				fireball.resetToStart();
			}
			return false;
		}
	}

	public void onManagedUpdate() {
		if (fireball.getState() == Fireball.State.FIRED && state == State.FOLLOWING) {
			if (vectorToAxis().dot(fireDistToAxis) < 0) {
				state = State.IDLE;
				setHandlePostion(initialPos.x, initialPos.y);
				return;
			}
			setHandlePostion(fireball);
		}
	}

	private void setHandlePostion(Entity entity) {
		setHandlePostion(entity.getX(), entity.getY());
	}

	private void setHandlePostion(float px, float py) {
		handle.setPosition(px, py);
		float xDist = axis.getX() - px;
		float yDist = axis.getY() - py;
		if (xDist == 0 && yDist == 0) {
			line.setVisible(false);
			arrow.setVisible(false);
		} else {
			line.setVisible(true);
			arrow.setVisible(true);
			float tipx = xDist * 2 + px;
			float tipy = yDist * 2 + py;
			line.setPosition(px, py, tipx, tipy);
			arrow.setPosition(tipx, tipy);
			arrow.setRotation((float) Math.toDegrees(Math.atan2(xDist, yDist)));
		}
	}

	public void resetHandlePosition() {

		setHandlePostion(initialPos.x, initialPos.y);

	}

	private Vector2f vectorToAxis() {
		float distX = axis.getX() - handle.getX();
		float distY = axis.getY() - handle.getY();
		return new Vector2f(distX, distY);
	}

	public void onScreenAreaTouched(TouchEvent pSceneTouchEvent) {
		if (state != State.DRAGGING) {
			return;
		}
		if (pSceneTouchEvent.isActionUp()) {
			if (fireball.getState() == Fireball.State.READY) {
				state = State.FOLLOWING;
				float scaleFactor = 0.3f;
				fireDistToAxis = vectorToAxis();
				fireball.fire(fireDistToAxis.multiply(scaleFactor));
			} else {
				state = State.IDLE;
			}
			return;
		}
		float x = pSceneTouchEvent.getX();
		float y = pSceneTouchEvent.getY();
		Vector2f vecToAxis = new Vector2f(axis.getX() - x, axis.getY() - y);
		float distSquared = vecToAxis.lengthSquared();
		if (distSquared > AIM_RADIUS_SQ) {
			Vector2f result = vecToAxis.multiply((float) (AIM_RADIUS / Math.sqrt(distSquared)));
			x = axis.getX() - result.x;
			y = axis.getY() - result.y;
		}
		setHandlePostion(x, y);
		fireball.setPosition(x, y);
	}
}

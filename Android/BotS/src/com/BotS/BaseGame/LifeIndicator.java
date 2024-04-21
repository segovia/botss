package com.BotS.BaseGame;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.BotS.BaseGame.Player.Team;

public class LifeIndicator implements GameConstants {

	public List<Sprite> hearts = new ArrayList<Sprite>();
	public List<Sprite> emptyHearts = new ArrayList<Sprite>();
	boolean changed = true;

	public LifeIndicator(Player player, int maxLives, VertexBufferObjectManager vbom) {
		int width = (int) ResourcesManager.getInstance().heart_region.getWidth();
		int height = (int) ResourcesManager.getInstance().heart_region.getHeight();
		int offset = 30 - width / 2;
		int space = 40;
		if (player.getTeam() == Team.RIGHT) {
			offset = RESOLUTION.x - offset - width;
			space *= -1;
		}
		for (int i = 0; i < maxLives; i++) {
			hearts.add(new Sprite(offset + i * space, 50 - height / 2, ResourcesManager.getInstance().heart_region,
					vbom));
			emptyHearts.add(new Sprite(offset + i * space, 50 - height / 2,
					ResourcesManager.getInstance().empty_heart_region, vbom));
		}
	}

	public void resetChanged() {
		changed = false;
	}

	public boolean hasChanged() {
		return changed;
	}

	public void attach(GameScene gameScene) {
		gameScene.rockAndHeartsSpriteBatch.addLifeIndicator(this);
	}

	public void setLife(int life) {
		for (int i = 0; i < hearts.size(); i++) {
			hearts.get(i).setVisible(i < life);
		}
		changed = true;
	}
}

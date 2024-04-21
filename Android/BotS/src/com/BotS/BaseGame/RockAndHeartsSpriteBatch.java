package com.BotS.BaseGame;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.DynamicSpriteBatch;

public class RockAndHeartsSpriteBatch extends DynamicSpriteBatch {

	public RockAndHeartsSpriteBatch(GameScene gameScene) {
		super(ResourcesManager.getInstance().gameTextureAtlas, 30, gameScene.vbom);
	}

	private Sprite rocks;
	private List<LifeIndicator> lifeIndicators = new ArrayList<LifeIndicator>();

	public void addLifeIndicator(LifeIndicator lifeIndicator) {
		lifeIndicators.add(lifeIndicator);
	}

	public void setRocks(Sprite rocks) {
		this.rocks = rocks;
	}

	@Override
	protected boolean onUpdateSpriteBatch() {
		boolean submit = false;
		for (LifeIndicator lifeIndicator : lifeIndicators) {
			if (lifeIndicator.hasChanged()) {
				submit = true;
			}
		}
		if (!submit) {
			return false;
		}
		draw(rocks);
		for (LifeIndicator lifeIndicator : lifeIndicators) {
			for (Sprite emptyHeart : lifeIndicator.emptyHearts) {
				this.drawWithoutChecks(emptyHeart);
			}
			for (Sprite heart : lifeIndicator.hearts) {
				this.drawWithoutChecks(heart);
			}
		}
		return true;
	}
}

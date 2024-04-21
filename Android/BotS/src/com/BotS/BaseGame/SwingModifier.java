package com.BotS.BaseGame;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.SingleValueSpanEntityModifier;

public class SwingModifier extends SingleValueSpanEntityModifier {

	private float InitialY;

	private float minMagnitude;
	private float maxMagnitude;

	public SwingModifier(float pDuration, float pFromValue, float pToValue, float pFromMagnitude, float pToMagnitude) {
		super(pDuration, pFromValue, pToValue);
		minMagnitude = pFromMagnitude;
		maxMagnitude = pToMagnitude;
	}

	@Override
	protected void onSetInitialValue(IEntity pItem, float pValue) {
		InitialY = pItem.getY();

	}

	@Override
	protected void onSetValue(IEntity pItem, float pPercentageDone, float pValue) {
		float currentMagnitude = minMagnitude + (maxMagnitude - minMagnitude) * pPercentageDone;
		float currentSinValue = (float) Math.sin(pValue);
		pItem.setY(InitialY + currentMagnitude * currentSinValue);
	}

	@Override
	public SwingModifier deepCopy() throws DeepCopyNotSupportedException {
		throw new DeepCopyNotSupportedException();
	}

}

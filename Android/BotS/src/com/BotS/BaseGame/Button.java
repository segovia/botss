package com.BotS.BaseGame;

import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

public class Button extends Rectangle {

	private Line topLine;
	private Line leftLine;
	private Line rightLine;
	private Line bottomLine;

	public Button(final float pX, final float pY, final float pWidth, final float pHeight,
			final VertexBufferObjectManager pVertexBufferObjectManager) {

		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);

		topLine = new Line(0, pHeight, pWidth, pHeight, ResourcesManager.getInstance().vbom);
		leftLine = new Line(0, 0, 0, pHeight, ResourcesManager.getInstance().vbom);
		rightLine = new Line(pWidth, pHeight, pWidth, 0, ResourcesManager.getInstance().vbom);
		bottomLine = new Line(pWidth, 0, 0, 0, ResourcesManager.getInstance().vbom);

		this.setColor(Color.BLACK);
		setAlpha(0.15f);
		topLine.setLineWidth(2.5f);
		leftLine.setLineWidth(2.5f);
		rightLine.setLineWidth(2.5f);
		bottomLine.setLineWidth(2.5f);

		topLine.setAlpha(0.15f);
		leftLine.setAlpha(0.15f);
		rightLine.setAlpha(0.15f);
		bottomLine.setAlpha(0.15f);

		attachChild(topLine);
		attachChild(leftLine);
		attachChild(rightLine);
		attachChild(bottomLine);

	}

	public void hideOutline() {

		topLine.setVisible(false);
		leftLine.setVisible(false);
		rightLine.setVisible(false);
		bottomLine.setVisible(false);

	}

	public void showOutline() {

		topLine.setVisible(true);
		leftLine.setVisible(true);
		rightLine.setVisible(true);
		bottomLine.setVisible(true);

	}

	public void setOutlineWidth(float width) {
		topLine.setLineWidth(width);
		leftLine.setLineWidth(width);
		rightLine.setLineWidth(width);
		bottomLine.setLineWidth(width);
	}

	public void setOutlineAlpha(float alpha) {

		topLine.setAlpha(alpha);
		leftLine.setAlpha(alpha);
		rightLine.setAlpha(alpha);
		bottomLine.setAlpha(alpha);

	}

}

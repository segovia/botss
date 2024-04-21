package com.BotS.BaseGame;

import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.shader.exception.ShaderProgramLinkException;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

import android.opengl.GLES20;

import com.BotS.Physics.PhysicsConstants;

public class WaterShader extends ShaderProgram implements PhysicsConstants, GameConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	private static WaterShader INSTANCE;
	//@formatter:off
	public static final String VERTEXSHADER =
			"uniform mat4 " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + ";\n" +
			"attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
			"attribute vec4 " + ATTRIBUTE_VELOCITY + ";\n" +
			"varying vec2 v_velocity;\n" +
			"varying float distFromGround;\n" +
			"void main() {\n" +
			"   gl_PointSize = 20.0;\n" +
			"   vec2 original_position = " + ShaderProgramConstants.ATTRIBUTE_POSITION + ".xy * vec2(" + PIXEL_TO_METER_RATIO_DEFAULT + ", " + PIXEL_TO_METER_RATIO_DEFAULT + ");\n" +
			"   distFromGround = distance(original_position, vec2(" + PLANET_CENTER.x + "," + PLANET_CENTER.y + ")) - " + PLANET_RADIUS + ";\n" +
			"	gl_Position = " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + " * (" + ShaderProgramConstants.ATTRIBUTE_POSITION + " * vec4(" + PIXEL_TO_METER_RATIO_DEFAULT + ", " + PIXEL_TO_METER_RATIO_DEFAULT + ", 1.0, 1.0));\n" +
			"	v_velocity = " + ATTRIBUTE_VELOCITY + ".xy;\n" +
			"}";

	public static final String FRAGMENTSHADER =
			"precision lowp float;\n" +
			"varying vec2 v_velocity;\n" +
			"varying float distFromGround;\n" +
			"void main() {\n" +
			"	vec2 diff = gl_PointCoord.xy - vec2(0.5);\n" +
			"	if (dot(diff,diff) > 0.25) {\n" +
			"		gl_FragColor = vec4(0.0);\n" +
			"	} else {\n" +
			"		float size = 1.0 - (distFromGround/720.0)*0.7;\n" +
			"		float velocityMod = length(v_velocity);\n" +
			"		vec2 sigma = vec2(0.3);\n" +
			"		vec2 uvPos = diff;\n" +
			"       if (velocityMod > 0.05) {\n"+
			"			vec2 velocityNorm = v_velocity/velocityMod;\n" +
			"			velocityMod = min(1.0, 0.2 * velocityMod);\n" +
			"			vec2 tanVelocityNorm = vec2(-velocityNorm.y, velocityNorm.x);\n" +
			"			uvPos = vec2(dot(diff, velocityNorm), dot(diff, tanVelocityNorm));\n" +
			"			sigma = 0.3 * size * vec2(1.0, 1.0 - velocityMod*0.5);\n" +
			"		}\n" +
			"		float intensity = exp(-0.5 * (uvPos.x*uvPos.x/(sigma.x*sigma.x) + uvPos.y*uvPos.y/(sigma.y*sigma.y)));\n" +
			"		gl_FragColor = vec4(size, size, size, intensity);\n" +
			"	}\n" +
			"}\n";
	
	//@formatter:on
	// ===========================================================
	// Fields
	// ===========================================================

	public static int sUniformModelViewPositionMatrixLocation = ShaderProgramConstants.LOCATION_INVALID;

	// ===========================================================
	// Constructors
	// ===========================================================

	private WaterShader() {
		super(WaterShader.VERTEXSHADER, WaterShader.FRAGMENTSHADER);
	}

	public static WaterShader getInstance() {
		if (WaterShader.INSTANCE == null) {
			WaterShader.INSTANCE = new WaterShader();
		}
		return WaterShader.INSTANCE;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void link(final GLState pGLState) throws ShaderProgramLinkException {
		GLES20.glBindAttribLocation(mProgramID, ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION,
				ShaderProgramConstants.ATTRIBUTE_POSITION);
		GLES20.glBindAttribLocation(mProgramID, ATTRIBUTE_VELOCITY_LOCATION, ATTRIBUTE_VELOCITY);

		super.link(pGLState);

		WaterShader.sUniformModelViewPositionMatrixLocation = getUniformLocation(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX);
	}

	@Override
	public void bind(final GLState pGLState, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		GLES20.glDisableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION);
		GLES20.glDisableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION);
		GLES20.glEnableVertexAttribArray(ATTRIBUTE_VELOCITY_LOCATION);

		super.bind(pGLState, pVertexBufferObjectAttributes);

		GLES20.glUniformMatrix4fv(WaterShader.sUniformModelViewPositionMatrixLocation, 1, false,
				pGLState.getModelViewProjectionGLMatrix(), 0);
	}

	@Override
	public void unbind(final GLState pGLState) {
		GLES20.glDisableVertexAttribArray(ATTRIBUTE_VELOCITY_LOCATION);
		GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION);
		GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION);

		super.unbind(pGLState);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}

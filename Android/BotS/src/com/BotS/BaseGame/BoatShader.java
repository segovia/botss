package com.BotS.BaseGame;

import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.shader.exception.ShaderProgramLinkException;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

import android.opengl.GLES20;

public class BoatShader extends ShaderProgram {
	// ===========================================================
	// Constants
	// ===========================================================

	private static BoatShader INSTANCE;

	//@formatter:off
	public static final String VERTEXSHADER =
			"uniform mat4 " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + ";\n" +
			"attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
			"attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_COLOR + ";\n" +
			"attribute vec2 " + ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES + ";\n" +
			"varying vec4 " + ShaderProgramConstants.VARYING_COLOR + ";\n" +
			"varying vec2 " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +
			"void main() {\n" +
			"	" + ShaderProgramConstants.VARYING_COLOR + " = " + ShaderProgramConstants.ATTRIBUTE_COLOR + ";\n" +
			"	" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + " = " + ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES + ";\n" +
			"	gl_Position = " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + " * " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
			"}";

	public static final String FRAGMENTSHADER =
			"precision lowp float;\n" +
			"uniform sampler2D " + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ";\n" +
			"uniform vec2 u_texRegionMin;\n" +
			"uniform vec2 u_texRegionMax;\n" +
			"uniform float u_time;\n" +
			"varying lowp vec4 " + ShaderProgramConstants.VARYING_COLOR + ";\n" +
			"varying mediump vec2 " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +
			"void main() {\n" +
//			"	gl_FragColor = " + ShaderProgramConstants.VARYING_COLOR + " * texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2(0.2));\n" +
			"	gl_FragColor = " + ShaderProgramConstants.VARYING_COLOR + " * texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ");\n" +
			"	vec2 normTexCoord = (v_textureCoordinates - u_texRegionMin)/(u_texRegionMax-u_texRegionMin);\n" +
			"	vec2 distFromCenter = normTexCoord - vec2(0.5);\n" +
			"	float distSq = dot(distFromCenter, distFromCenter);\n" +
			"	float max = 0.15 + 0.10 * (abs(u_time-500.0) / 500.0);\n" +
			"	if (distSq < max) {\n" +
			"		distSq = 0.7*distSq/max;\n" +
			"		gl_FragColor.g += distSq;\n" +
			"		gl_FragColor.b += distSq;\n" +
			"		gl_FragColor.a += distSq;\n" +
			"	}\n" +
			"}";
	//@formatter:on
	// ===========================================================
	// Fields
	// ===========================================================

	public static int sUniformModelViewPositionMatrixLocation = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformTexture0Location = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformTexRegionMinLocation = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformTexRegionMaxLocation = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformTimeLocation = ShaderProgramConstants.LOCATION_INVALID;

	// ===========================================================
	// Constructors
	// ===========================================================

	private BoatShader() {
		super(BoatShader.VERTEXSHADER, BoatShader.FRAGMENTSHADER);
	}

	public static BoatShader getInstance() {
		if (BoatShader.INSTANCE == null) {
			BoatShader.INSTANCE = new BoatShader();
		}
		return BoatShader.INSTANCE;
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
		GLES20.glBindAttribLocation(mProgramID, ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION,
				ShaderProgramConstants.ATTRIBUTE_COLOR);
		GLES20.glBindAttribLocation(mProgramID, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION,
				ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES);

		super.link(pGLState);

		BoatShader.sUniformModelViewPositionMatrixLocation = getUniformLocation(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX);
		BoatShader.sUniformTexture0Location = getUniformLocation(ShaderProgramConstants.UNIFORM_TEXTURE_0);
		BoatShader.sUniformTexRegionMinLocation = getUniformLocation("u_texRegionMin");
		BoatShader.sUniformTexRegionMaxLocation = getUniformLocation("u_texRegionMax");
		BoatShader.sUniformTimeLocation = getUniformLocation("u_time");

	}

	@Override
	public void bind(final GLState pGLState, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super.bind(pGLState, pVertexBufferObjectAttributes);

		GLES20.glUniformMatrix4fv(BoatShader.sUniformModelViewPositionMatrixLocation, 1, false,
				pGLState.getModelViewProjectionGLMatrix(), 0);
		GLES20.glUniform1i(BoatShader.sUniformTexture0Location, 0);

		// ((HighPerformanceTiledSpriteVertexBufferObject)pVertexBufferObjectAttributes).

	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}

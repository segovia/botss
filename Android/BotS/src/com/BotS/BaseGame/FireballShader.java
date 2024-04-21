package com.BotS.BaseGame;

import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.shader.exception.ShaderProgramLinkException;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

import android.opengl.GLES20;

import com.BotS.Physics.PhysicsConstants;

public class FireballShader extends ShaderProgram implements PhysicsConstants, GameConstants {

	private static FireballShader INSTANCE;

	public static final String VERTEXSHADER = "uniform mat4 "
			+ ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + ";\n" + "attribute vec4 "
			+ ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" + "attribute vec4 "
			+ ShaderProgramConstants.ATTRIBUTE_COLOR + ";\n" + "attribute vec2 "
			+ ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES + ";\n" + "varying vec4 "
			+ ShaderProgramConstants.VARYING_COLOR + ";\n" + "varying vec2 "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" + "void main() {\n" + "	"
			+ ShaderProgramConstants.VARYING_COLOR + " = " + ShaderProgramConstants.ATTRIBUTE_COLOR + ";\n" + "	"
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + " = "
			+ ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES + ";\n" + "	gl_Position = "
			+ ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + " * "
			+ ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" + "}";

	public static final String FRAGMENTSHADER = "precision lowp float;\n" + "uniform sampler2D "
			+ ShaderProgramConstants.UNIFORM_TEXTURE_0 + ";\n" + "uniform vec2 u_texRegionMin;\n"
			+ "uniform vec2 u_texRegionMax;\n" + "uniform float u_time;\n" + "varying lowp vec4 "
			+ ShaderProgramConstants.VARYING_COLOR + ";\n" + "varying mediump vec2 "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" 
			+ "void main() {\n" 
			//+ "	gl_FragColor = "
			//+ ShaderProgramConstants.VARYING_COLOR + " * texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", "
			//+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ");\n" 
			+ "	vec2 normTexCoord = (v_textureCoordinates - u_texRegionMin)/(u_texRegionMax-u_texRegionMin);\n"
			+ "	vec2 distFromCenter = normTexCoord - vec2(0.5);\n"
			+ "	float distSq = dot(distFromCenter, distFromCenter);\n"
			+ " float max =  0.10 * (abs(u_time-500.0) / 500.0);\n" 
			+ " const float blurSize = 0.0007;\n" +
			"	vec4 sum = vec4(0.0);	\n" +
			"	float t = 0.1;	\n" +
			//"if(texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2(" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x + blurSize, " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y)).w > t) {" +

			"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2(" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x - blurSize, " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y - blurSize)) * 0.1; \n" +
			"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2(" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x - blurSize, " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y)) * 0.1; \n" +
			"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2(" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x - blurSize, " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y + blurSize)) * 0.1; \n" +
			"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2(" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x , " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y - blurSize)) * 0.1; \n" +
			"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2(" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x, " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y)) * 0.20; \n" +
			"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2(" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x, " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y + blurSize)) * 0.1; \n" +
			"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2(" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x + blurSize, " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y - blurSize)) * 0.1;	\n" +
			"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2(" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x + blurSize, " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y)) * 0.1;	\n" +
			"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2(" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x + blurSize, " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y + blurSize)) * 0.1;	\n" +
			//"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2(" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x, " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y - 4.0*blurSize)) * 0.05; \n" +
			//"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2(" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x, " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y - 3.0*blurSize)) * 0.09; \n" +
			//"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2(" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x, " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y - 2.0*blurSize)) * 0.12; \n" +
			//"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2(" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x, " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y - blurSize)) * 0.15; \n" +
			//"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2(" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x, " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y + blurSize)) * 0.15; \n" +
			//"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2(" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x, " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y + 2.0*blurSize)) * 0.12;	\n" +
			//"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2(" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x, " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y + 3.0*blurSize)) * 0.09;	\n" +
			//"	sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2(" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x, " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y + 4.0*blurSize)) * 0.05;	\n" +
			// "}\n" +
			"	gl_FragColor = sum * 4.0;	\n" 
			//+ "	gl_FragColor.r = gl_FragColor.b;\n"
			//+ "	gl_FragColor.b = gl_FragColor.b ;\n"
			//+ "	gl_FragColor.g = gl_FragColor.g ;\n"
			//+ "	gl_FragColor.a = 1.0/distSq * 100000;\n" 
			+ "}";

	public FireballShader() {
		super(FireballShader.VERTEXSHADER, FireballShader.FRAGMENTSHADER);
	}

	public static FireballShader getInstance() {
		if (FireballShader.INSTANCE == null) {
			FireballShader.INSTANCE = new FireballShader();
		}
		return FireballShader.INSTANCE;
	}

	public static int sUniformModelViewPositionMatrixLocation = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformTexture0Location = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformTexRegionMinLocation = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformTexRegionMaxLocation = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformTimeLocation = ShaderProgramConstants.LOCATION_INVALID;

	@Override
	protected void link(final GLState pGLState) throws ShaderProgramLinkException {

		GLES20.glBindAttribLocation(mProgramID, ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION,
				ShaderProgramConstants.ATTRIBUTE_POSITION);
		GLES20.glBindAttribLocation(mProgramID, ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION,
				ShaderProgramConstants.ATTRIBUTE_COLOR);
		GLES20.glBindAttribLocation(mProgramID, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION,
				ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES);

		super.link(pGLState);

		FireballShader.sUniformModelViewPositionMatrixLocation = getUniformLocation(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX);
		FireballShader.sUniformTexture0Location = getUniformLocation(ShaderProgramConstants.UNIFORM_TEXTURE_0);
//		FireballShader.sUniformTexRegionMinLocation = getUniformLocation("u_texRegionMin");
//		FireballShader.sUniformTexRegionMaxLocation = getUniformLocation("u_texRegionMax");
//		FireballShader.sUniformTimeLocation = getUniformLocation("u_time");

	}

	@Override
	public void bind(final GLState pGLState, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super.bind(pGLState, pVertexBufferObjectAttributes);

		GLES20.glUniformMatrix4fv(FireballShader.sUniformModelViewPositionMatrixLocation, 1, false,
				pGLState.getModelViewProjectionGLMatrix(), 0);
		GLES20.glUniform1i(FireballShader.sUniformTexture0Location, 0);

		// ((HighPerformanceTiledSpriteVertexBufferObject)pVertexBufferObjectAttributes).

	}

}
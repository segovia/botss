package com.BotS.BaseGame;

import com.BotS.Physics.Vector2;
import com.BotS.Physics.Vector2.Vector2f;

public interface GameConstants {
	public static final Vector2<Integer> RESOLUTION = new Vector2<Integer>(1280, 720);
	public static final int LEFT_BOUND = -280;
	public static final int RIGHT_BOUND = RESOLUTION.x - LEFT_BOUND;
	public static final int LEFT_TOP_CORNER = 140;
	public static final int RIGHT_TOP_CORNER = RESOLUTION.x - LEFT_TOP_CORNER;
	public static final Vector2f PLANET_CENTER = new Vector2f(0.5f * RESOLUTION.x, -3.0f * RESOLUTION.y);
	public static final float PLANET_RADIUS = (float) Math.sqrt(Math.pow(LEFT_TOP_CORNER + 5 - PLANET_CENTER.x, 2)
			+ Math.pow(30 - PLANET_CENTER.y, 2));

	public static final int INITIAL_LIFE = 5;
	public static final String ATTRIBUTE_VELOCITY = "a_velocity";
	public static final int ATTRIBUTE_VELOCITY_LOCATION = 1;

	// Times are in frames
	public static float BIRDTIME = 250;
	public static float BIRDSHOWTIME = 1000;

	public static float STABILIZATION_STEPS = 60;

	// Category must be only 1 bit = 1, or in other words, number to the power of 2
	public static final int DEFAULT_COLLISION_CATEGORY = 1 << 0;
	public static final int LEFT_TEAM_BOAT_COLLISION_CATEGORY = 1 << 1;
	public static final int RIGHT_TEAM_BOAT_COLLISION_CATEGORY = 1 << 2;
	public static final int BOUNDARY_COLLISION_CATEGORY = 1 << 3;
	public static final int SUNRAY_COLLISION_CATEGORY = 1 << 4;
	public static final int ICEBERG_COLLISION_CATEGORY = 1 << 5;
	public static final int LEFT_TEAM_FIREBALL_COLLISION_CATEGORY = 1 << 6;
	public static final int RIGHT_TEAM_FIREBALL_COLLISION_CATEGORY = 1 << 7;
	public static final int BIRD_COLLISION_CATEGORY = 1 << 8;
	public static final int PLANET_COLLISION_CATEGORY = 1 << 9;
	public static final int LEFT_TEAM_CASTLE_COLLISION_CATEGORY = 1 << 10;
	public static final int RIGHT_TEAM_CASTLE_COLLISION_CATEGORY = 1 << 11;

	public static final int MAX_BOATS_PER_TEAM = 30;

}

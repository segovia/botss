package com.BotS.BaseGame;

public enum Season {
	SPRING("Spring"), SUMMER("Summer"), FALL("Fall"), WINTER("Winter");

	public static Season getSeason(int id) {
		switch (id) {
		case 0:
			return SPRING;
		case 1:
			return SUMMER;
		case 2:
			return FALL;
		case 3:
			return WINTER;
		}
		throw new IllegalArgumentException("Unknown season for id: " + id);
	}

	private String mText;

	private Season(String pText) {
		mText = pText;
	}

	public String getText() {
		return mText;
	}
}

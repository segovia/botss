package com.BotS.Physics;

public class Vector2<T> {
	public T x;
	public T y;

	public Vector2(T x, T y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "MyVector2 [x=" + x + ", y=" + y + "]";
	}

	public static class Vector2f extends Vector2<Float> {

		public Vector2f(Float x, Float y) {
			super(x, y);
		}

		public Vector2f multiply(float n) {
			return new Vector2f(x * n, y * n);
		}

		public Vector2f subtract(Vector2f other) {
			return new Vector2f(x - other.x, y - other.y);
		}

		public float dot(Vector2f other) {
			return x * other.x + y * other.y;
		}

		public float lengthSquared() {
			return this.dot(this);
		}

		public float length() {
			return (float) Math.sqrt(this.lengthSquared());
		}

		public void normalize() {
			float len = this.length();
			if (len < 0.000001) {
				return;
			}
			x /= len;
			y /= len;
		}

	}
}

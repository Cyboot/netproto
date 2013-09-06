package net.tmt.common.util;

public class Vector2d {
	public double	x;
	public double	y;

	public Vector2d() {
	}

	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2d copy() {
		return new Vector2d(x, y);
	}

	public Vector2d difference(Vector2d other) {
		return new Vector2d(x - other.x, y - other.y);
	}

	public Vector2d sum(Vector2d other) {
		return new Vector2d(x + other.x, y + other.y);
	}

	public double distance(Vector2d other) {
		double dx = other.x - x;
		double dy = other.y - y;

		return Math.sqrt(dx * dx + dy * dy);
	}

	public int x() {
		return (int) x;
	}

	public int y() {
		return (int) y;
	}


	@Override
	public String toString() {
		return "(" + StringFormatter.format(x, 4, 2) + " : " + StringFormatter.format(y, 4, 2) + ")";
	}
}

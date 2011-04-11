package br.com.jera.enemies;

import br.com.jera.util.CommonMath.Vector2;

public class OffsetGenerator {

	public Vector2 getVerticalOffset(final float length) {
		offset = offset.add(new Vector2(0, length));
		return new Vector2(offset);
	}

	private Vector2 offset = new Vector2(0, 0);
}

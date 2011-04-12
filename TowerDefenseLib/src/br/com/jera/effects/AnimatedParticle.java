package br.com.jera.effects;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.graphic.Sprite;
import br.com.jera.util.CommonMath.Rectangle2D;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.CommonMath.Vector3;
import br.com.jera.util.DisplayableEntity;
import br.com.jera.util.SceneViewer;
import br.com.jera.util.SpriteResourceManager;

public class AnimatedParticle implements TemporaryEffect {

	public AnimatedParticle(long duration, float degreeAngle, int resourceId, Vector2 pos, int columns, int rows, float radius) {
		this.duration = duration;
		this.startTime = System.currentTimeMillis();
		this.resourceId = resourceId;
		this.numFrames = columns * rows;
		this.size = new Vector2(radius * 2, radius * 2);
		this.pos = pos;
		this.angle = degreeAngle;
	}

	public void draw(SceneViewer viewer, SpriteResourceManager res) {
		final long elapsed = System.currentTimeMillis() - startTime;
		if (elapsed < duration) {
			Sprite sprite = res.getSprite(resourceId);
			int frame = (int) (((((double) elapsed)) / (double) duration) * (double) numFrames);
			frame = Math.max(Math.min(frame, sprite.getNumFrames()-1), 0);
			sprite.draw(pos.sub(viewer.getOrthogonalViewerPos()), size, angle, Sprite.centerOrigin, frame, false);
		}
	}

	public Vector2 getMin(SpriteResourceManager res) {
		return pos.sub(size);
	}

	public Vector2 getMax(SpriteResourceManager res) {
		return pos.add(size);
	}

	public Vector3 getPos() {
		return new Vector3(pos, 0);
	}

	public void update(long lastFrameDeltaTimeMS, AudioPlayer audioPlayer) {
	}

	public boolean isVisible(SceneViewer viewer, Rectangle2D clientRect) {
		return clientRect.isIntersecting(new Rectangle2D(pos.sub(viewer.getOrthogonalViewerPos()), size, Sprite.centerOrigin));
	}

	public int compareTo(DisplayableEntity arg0) {
		return 0;
	}

	public boolean isOver() {
		return (System.currentTimeMillis() - startTime > duration);
	}

	private Vector2 pos;
	private final float angle;
	private final Vector2 size;
	private final long startTime;
	private final long duration;
	private final int resourceId, numFrames;
}

package br.com.jera.towerdefenselib;

import java.util.LinkedList;
import java.util.ListIterator;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.enemies.Enemy;
import br.com.jera.enemies.EnemyRoad;
import br.com.jera.graphic.GraphicDevice;
import br.com.jera.graphic.GraphicDevice.ALPHA_MODE;
import br.com.jera.graphic.Sprite;
import br.com.jera.resources.PropertyReader;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.util.Classic2DViewer;
import br.com.jera.util.CommonMath;
import br.com.jera.util.CommonMath.Rectangle2D;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.CommonMath.Vector3;
import br.com.jera.util.CommonMath.Vector4;
import br.com.jera.util.DisplayableEntity;
import br.com.jera.util.FrameTimer;
import br.com.jera.util.SceneViewer;
import br.com.jera.util.SpriteResourceManager;
import br.com.jera.weapons.HarmEffect;

public class GameCharacter implements DisplayableEntity {

	private static final int MOVING_DOWN = 0;
	private static final int MOVING_LEFT = 1;
	private static final int MOVING_RIGHT = 2;
	private static final int MOVING_UP = 3;
	private static final long defaultFrameStride = 150;
	public static final Vector2 defaultSpriteOrigin = new Vector2(0.5f, 1.0f);
	public static final Vector2 defaultShadowOrigin = new Vector2(0.5f, 0.65f);

	protected Vector2 pos = new Vector2();
	protected float speed;
	protected float hp;
	protected float initialHp = 100;
	private final Vector2 defaultFrameSize;
	private FrameTimer frameTimer = new FrameTimer();
	private long currentFrameStride = defaultFrameStride;
	private int direction = MOVING_UP;
	private int resourceId = -1;
	private Vector2 moveVector = new Vector2();
	private LinkedList<HarmEffect> harmEffects = new LinkedList<HarmEffect>();
	private ResourceIdRetriever resRet;

	public GameCharacter(int resourceId, Vector2 pos, AudioPlayer audioPlayer, ResourceIdRetriever resRet) {
		this.resourceId = resourceId;
		this.pos = pos;
		this.resRet = resRet;
		this.defaultFrameSize = PropertyReader.getDefaultEnemySize();
		setRandomDirection();
	}

	public void addHarmEffect(HarmEffect he, AudioPlayer audioPlayer, EnemyRoad road) {
		he.applyEffect(this, audioPlayer, road);
		harmEffects.add(he);
	}

	public boolean hasHarmEffect(final String name) {
		ListIterator<HarmEffect> iter = harmEffects.listIterator();
		while (iter.hasNext()) {
			if (iter.next().getHarmEffectName() == name)
				return true;
		}
		return false;
	}

	private void manageHarmEffects(final long lastFrameDeltaTimeMS, AudioPlayer audioPlayer) {
		ListIterator<HarmEffect> iter = harmEffects.listIterator();
		while (iter.hasNext()) {
			HarmEffect he = iter.next();
			if (he.isOver(lastFrameDeltaTimeMS)) {
				he.removeEffect(this, audioPlayer);
				iter.remove();
			}
		}
	}

	private void drawHarmEffects(SceneViewer viewer, SpriteResourceManager res) {
		ListIterator<HarmEffect> iter = harmEffects.listIterator();
		while (iter.hasNext()) {
			iter.next().drawHarmEffect(this, viewer, res);
		}
	}

	public boolean isDead() {
		return hp <= 0.0f;
	}

	public void addToHp(final float a) {
		hp += a;
	}

	public void addToSpeed(final float a) {
		speed += a;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(final float speed) {
		this.speed = speed;
	}

	public void setRandomDirection() {
		direction = (int) (Math.random() * 4.0) % 3;
	}

	public Vector2 get2DPos() {
		return new Vector2(pos);
	}

	public float getInitialHp() {
		return initialHp;
	}

	public Vector3 getPos() {
		return new Vector3(pos, 0);
	}

	public void move(Vector2 dir, final float speed) {
		moveVector = moveVector.add(dir.normalize().multiply(speed));
	}

	public void setFrameStride(final long stride) {
		currentFrameStride = stride;
	}

	public void setDirection(Vector2 dir) {
		setDirection(CommonMath.getAngle(dir));
	}

	public void setDirection(float angle) {
		direction = findDirection(angle);
	}

	private int findDirection(final float angle) {
		if (angle > CommonMath.QUARTER_PI + CommonMath.HALF_PI && angle < CommonMath.PI + CommonMath.QUARTER_PI) {
			return MOVING_UP;
		} else if (angle > CommonMath.QUARTER_PI + CommonMath.PI + CommonMath.HALF_PI || angle < CommonMath.QUARTER_PI) {
			return MOVING_DOWN;
		} else if (angle > CommonMath.QUARTER_PI && angle < CommonMath.HALF_PI + CommonMath.QUARTER_PI) {
			return MOVING_RIGHT;
		} else {
			return MOVING_LEFT;
		}
	}

	public void draw(SceneViewer viewer, SpriteResourceManager res) {
		Sprite sprite = res.getSprite(resourceId);

		final Vector2 absolutePos = pos.sub(viewer.getOrthogonalViewerPos());
		res.getGraphicDevice().setAlphaMode(ALPHA_MODE.MODULATE);

		if (!(this instanceof Enemy) && PropertyReader.isDrawTowerShadow()) {
			Sprite shadow = res.getSprite(resRet.getBmpShadow());
			shadow.draw(absolutePos, new Vector2(0.5f, 0.65f));
		}
		res.getGraphicDevice().setAlphaMode(ALPHA_MODE.DEFAULT);
		sprite.draw(absolutePos, 0, defaultSpriteOrigin, frameTimer.getFrame());
		drawHarmEffects(viewer, res);
	}

	public void drawHpBar(SceneViewer viewer, SpriteResourceManager res, final int barId) {
		Sprite sprite = res.getSprite(barId);
		final float bias = Math.max(0, Math.min(hp / initialHp, 1));
		final float size = bias * defaultFrameSize.x;
		if (size > 1) {
			Vector4 color = GraphicDevice.COLOR_RED.interpolate(GraphicDevice.COLOR_GREEN, bias);
			color.w = 0.7f;
			sprite.setColor(color);
			float hpBarHeight = PropertyReader.getHpBarHeight();
			Vector2 barPos = pos.sub(viewer.getOrthogonalViewerPos()).sub(
					new Vector2(defaultFrameSize.x / 2, defaultFrameSize.y + (hpBarHeight * 2)));
			sprite.draw(barPos, new Vector2(size, hpBarHeight), 0, Sprite.defaultOrigin, 0, false);
		}
	}

	public Vector2 getMin(SpriteResourceManager res) {
		Sprite sprite = res.getSprite(resourceId);
		return new Vector2(pos.sub(sprite.getBitmapSize().multiply(defaultSpriteOrigin)));
	}

	public Vector2 getMax(SpriteResourceManager res) {
		Sprite sprite = res.getSprite(resourceId);
		return new Vector2(pos.add(sprite.getBitmapSize().multiply(new Vector2(1, 1).sub(defaultSpriteOrigin))));
	}

	public void update(final long lastFrameDeltaTimeMS, AudioPlayer audioPlayer) {
		manageHarmEffects(lastFrameDeltaTimeMS, audioPlayer);
		final float angle = CommonMath.getAngle(moveVector);
		if (moveVector.length() > 0.0f) {
			direction = findDirection(angle);
			switch (direction) {
			case MOVING_UP:
				frameTimer.setFrame(12, 15, currentFrameStride);
				break;
			case MOVING_DOWN:
				frameTimer.setFrame(0, 3, currentFrameStride);
				break;
			case MOVING_LEFT:
				frameTimer.setFrame(4, 7, currentFrameStride);
				break;
			case MOVING_RIGHT:
				frameTimer.setFrame(8, 11, currentFrameStride);
				break;
			}
		} else {
			switch (direction) {
			case MOVING_UP:
				frameTimer.setFrame(12, 12, defaultFrameStride);
				break;
			case MOVING_DOWN:
				frameTimer.setFrame(0, 0, defaultFrameStride);
				break;
			case MOVING_LEFT:
				frameTimer.setFrame(4, 4, defaultFrameStride);
				break;
			case MOVING_RIGHT:
				frameTimer.setFrame(8, 8, defaultFrameStride);
				break;
			}
		}
		pos = pos.add(moveVector);

		moveVector.x = 0.0f;
		moveVector.y = 0.0f;
	}

	public int compareTo(DisplayableEntity another) {
		return new Float(Math.signum(pos.y - another.getPos().y)).intValue();
	}

	public boolean isVisible(SceneViewer viewer, Rectangle2D clientRect) {
		assert (viewer instanceof Classic2DViewer);
		final Vector2 absolutePos = ((Classic2DViewer) viewer).computeAbsolutePosition(pos);
		return clientRect.isIntersecting(new Rectangle2D(absolutePos, defaultFrameSize, defaultSpriteOrigin));
	}

	public void setPos(Vector2 pos) {
		this.pos = pos;
	}
}

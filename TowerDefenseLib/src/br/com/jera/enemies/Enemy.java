package br.com.jera.enemies;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.towerdefenselib.GameCharacter;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.SceneViewer;
import br.com.jera.util.SpriteResourceManager;
import br.com.jera.util.SpriteTileMap.Tile;

public class Enemy extends GameCharacter {

	public Enemy(EnemyProfile profile, EnemyRoad road, Vector2 offset, AudioPlayer audioPlayer, ResourceIdRetriever resRet) {
		super(profile.getResourceId(resRet), road.getRoadPath().get(0).getTileCenter().add(offset), audioPlayer, resRet);
		assert (road.getRoadPath().size() > 1);
		this.road = road;
		this.resRet = resRet;
		super.speed = profile.getSpeed();
		super.hp = super.initialHp = profile.getHp();
		super.setFrameStride(profile.getFrameStride());
	}

	public void update(final long lastFrameDeltaTimeMS, AudioPlayer audioPlayer) {
		final float animBias = (float) ((double) lastFrameDeltaTimeMS / 1000.0);

		if (nextTile < road.getRoadPath().size()) {
			Tile next = road.getRoadPath().get(nextTile);
			final Vector2 direction = next.getTileCenter().sub(super.pos);
			super.move(direction, speed * animBias);

			if (super.pos.distance(next.getTileCenter()) < speed) {
				nextTile++;
			}
		}
		super.update(lastFrameDeltaTimeMS, audioPlayer);
	}

	@Override
	public void draw(SceneViewer viewer, SpriteResourceManager res) {
		super.draw(viewer, res);
		super.drawHpBar(viewer, res, resRet.getBmpProgessBar());
	}

	public boolean hasReachedLastNode() {
		return (nextTile == road.getRoadPath().size());
	}

	public void addToHp(float v) {
		this.hp += v;
	}

	public float getHp() {
		return hp;
	}

	public float getNormalizedHp() {
		return (this.getInitialHp() + this.getDamageReceived()) / this.getInitialHp();
	}
	
	public void setNetLaunched(boolean netLaunched) {
		this.netLaunched = netLaunched;
	}

	public boolean isNetLaunched() {
		return netLaunched;
	}

	private boolean netLaunched;
	
	private float damageReceived = 0;
	private EnemyRoad road;
	private int nextTile = 0;
	private ResourceIdRetriever resRet;
	
	public int getNextTile() {
		return this.nextTile;
	}

	public void setDamageReceived(float damageReceived) {
		this.damageReceived = damageReceived;
	}

	public float getDamageReceived() {
		return damageReceived;
	}


	// TODO criar profiles de zumbis em arquivo resource, não no código
	static public class Zombie01 extends EnemyProfile {
		public Zombie01(float level) {
			super(level);
		}

		@Override
		public float getSpeed() {
			return 10.0f;
		}

		@Override
		public float getHp() {
			return 100 * super.level;
		}

		@Override
		public int getResourceId(ResourceIdRetriever resRet) {
			return resRet.getBmpEnemy01();
		}

		@Override
		public long getFrameStride() {
			return 160;
		}
	}

	static public class Zombie02 extends EnemyProfile {
		public Zombie02(float level) {
			super(level);
		}

		@Override
		public float getSpeed() {
			return 15.0f;
		}

		@Override
		public float getHp() {
			return 180 * super.level;
		}

		@Override
		public int getResourceId(ResourceIdRetriever resRet) {
			return resRet.getBmpEnemy02();
		}

		@Override
		public long getFrameStride() {
			return 140;
		}
	}

	static public class Zombie03 extends EnemyProfile {
		public Zombie03(float level) {
			super(level);
		}

		@Override
		public float getSpeed() {
			return 18.0f;
		}

		@Override
		public float getHp() {
			return 250 * super.level;
		}

		@Override
		public int getResourceId(ResourceIdRetriever resRet) {
			return resRet.getBmpEnemy03();
		}

		@Override
		public long getFrameStride() {
			return 120;
		}
	}

	static public class Zombie04 extends EnemyProfile {
		public Zombie04(float level) {
			super(level);
		}

		@Override
		public float getSpeed() {
			return 17.0f;
		}

		@Override
		public float getHp() {
			return 300 * super.level;
		}

		@Override
		public int getResourceId(ResourceIdRetriever resRet) {
			return resRet.getBmpEnemy04();
		}

		@Override
		public long getFrameStride() {
			return 130;
		}
	}
}

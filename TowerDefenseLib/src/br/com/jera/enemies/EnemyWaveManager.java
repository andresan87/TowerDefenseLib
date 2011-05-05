package br.com.jera.enemies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.ListIterator;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.effects.AnimatedParticle;
import br.com.jera.effects.EffectManager;
import br.com.jera.graphic.Sprite;
import br.com.jera.resources.PropertyReader;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.towerdefenselib.OutputData;
import br.com.jera.towerdefenselib.Player;
import br.com.jera.towerdefenselib.SortedDisplayableEntityList;
import br.com.jera.util.BitmapFont;
import br.com.jera.util.Classic2DViewer;
import br.com.jera.util.CommonMath.Rectangle2D;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.SpriteResourceManager;

public class EnemyWaveManager implements OutputData.Data {

	public EnemyWaveManager(EnemyRoad road, long startTime, ResourceIdRetriever resRet) {
		this.road = road;
		this.resRet = resRet;
	}

	public void addWave(ArrayList<Enemy> enemies, final long time, Integer soundFx) {
		timeOffset += time;
		waves.put(new Long(timeOffset), new EnemyWave(enemies, soundFx));
	}

	public void update(final long lastFrameDeltaTimeMS, EffectManager effectManager, Player player, AudioPlayer audioPlayer) {
		checkNextWave(lastFrameDeltaTimeMS, audioPlayer);

		ListIterator<Enemy> iter = enemies.listIterator();
		while (iter.hasNext()) {
			Enemy enemy = iter.next();
			enemy.update(lastFrameDeltaTimeMS, audioPlayer);
			if (enemy.hasReachedLastNode()) {
				reachedLastNode = true;
			}
			if (enemy.isDead()) {
				final Vector2 effectPos = enemy.get2DPos().add(new Vector2(0, PropertyReader.getHitEffectHeightOffset()));
				effectManager.addEffect(new AnimatedParticle(600, -90.0f, resRet.getBmpDeathAnim(), effectPos, 6, 1, 72.0f));
				effectManager.addEffect(new AnimatedParticle(600, 90.0f, resRet.getBmpDeathAnim(), effectPos, 6, 1, 72.0f));
				player.addMoney((int) Math.min(enemy.getInitialHp() / 8, MAXIMUM_MONEY_PER_ENEMY));
				audioPlayer.play(resRet.getSfxEnemyDeath());
				killedEnemies++;
				iter.remove();
			}
		}
	}

	public int getKilledEnemies() {
		return killedEnemies;
	}

	public void draw(Classic2DViewer viewer, SortedDisplayableEntityList displayList, Rectangle2D clientRect) {
		ListIterator<Enemy> iter = enemies.listIterator();
		while (iter.hasNext()) {
			displayList.sortAdd(viewer, iter.next(), clientRect);
		}
	}

	private void checkNextWave(final long lastFrameDeltaTimeMS, AudioPlayer audioPlayer) {
		elapsedTime += lastFrameDeltaTimeMS;
		Collection<Long> c = waves.keySet();
		Iterator<Long> iter = c.iterator();

		if (iter.hasNext()) {
			final Long nextTime = iter.next();
			nextWaveTime = nextTime;
			if (elapsedTime > nextTime) {
				EnemyWave wave = waves.get(nextTime);
				if (wave.getSoundFx() != null) {
					audioPlayer.play(wave.getSoundFx());
				}

				for (int t = 0; t < wave.getEnemies().size(); t++) {
					enemies.add(wave.getEnemies().get(t));
				}
				waves.remove(nextTime);
				currentWave++;
			}
		}
	}

	public void forceNextWave() {
		final long nextWaveTime = getNextWaveRemainingTime();
		Collection<Long> c = waves.keySet();
		Iterator<Long> iter = c.iterator();
		LinkedHashMap<Long, EnemyWave> newWaves = new LinkedHashMap<Long, EnemyWave>();
		while (iter.hasNext()) {
			final long currentKey = iter.next();
			newWaves.put(currentKey - nextWaveTime + 1500, waves.get(currentKey));
		}
		waves = newWaves;
	}

	public Vector2 drawData(Vector2 pos, SpriteResourceManager res, BitmapFont font) {
		Sprite sprite = res.getSprite(resRet.getBmpNextWaveSymbol());
		sprite.draw(pos, Sprite.defaultOrigin);
		final Vector2 frameSize = sprite.getFrameSize();
		font.draw(new Vector2(frameSize.x, 0).add(pos), new Long(getNextWaveRemainingTime() / 1000).toString());
		return new Vector2(0, frameSize.y);
	}

	public EnemyRoad getRoad() {
		return road;
	}

	public boolean hasReachedLastNode() {
		return reachedLastNode;
	}

	public LinkedList<Enemy> getEnemies() {
		return enemies;
	}

	public boolean hasWaves(float level) {
		return !waves.isEmpty();
	}

	public long getNextWaveRemainingTime() {
		return nextWaveTime - elapsedTime;
	}

	public int getCurrentWave() {
		return currentWave;
	}

	private int currentWave = 0;
	private int killedEnemies = 0;
	private static final int MAXIMUM_MONEY_PER_ENEMY = 35;
	private long nextWaveTime = 0;
	private boolean reachedLastNode = false;
	private LinkedList<Enemy> enemies = new LinkedList<Enemy>();
	private LinkedHashMap<Long, EnemyWave> waves = new LinkedHashMap<Long, EnemyWave>();
	private ResourceIdRetriever resRet;
	private EnemyRoad road;
	private long elapsedTime = 0;
	private long timeOffset = 0;
}

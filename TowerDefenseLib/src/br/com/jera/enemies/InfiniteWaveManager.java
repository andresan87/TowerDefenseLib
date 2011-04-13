package br.com.jera.enemies;

import java.util.ArrayList;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.graphic.Sprite;
import br.com.jera.resources.PropertyReader;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.towerdefenselib.OutputData;
import br.com.jera.util.BitmapFont;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.SpriteResourceManager;

public class InfiniteWaveManager implements OutputData.Data {

	private float level = 0.5f;
	private int waveChain = 0;
	private EnemyWaveManager waveManager;
	private EnemyRoad road;
	private int currentWave = 1;
	private ResourceIdRetriever resRet;
	
	public InfiniteWaveManager(EnemyWaveManager waveManager, EnemyRoad road, ResourceIdRetriever resRet) {
		this.waveManager = waveManager;
		this.resRet = resRet;
		this.road = road;
	}
	
	public int getWaveChain() {
		return waveChain;
	}

	public void updateWaveManager(AudioPlayer audioPlayer, EnemyWaveManager waveManager, float offset) {
		Vector2 offsetDir = PropertyReader.getEnemyStartOffset();
		currentWave = waveManager.getCurrentWave();
		if (!waveManager.hasWaves(level)) {
			level *= 2.0f;
			waveChain++;

			// TODO criar waves em arquivo resource, não em código
			if (level <= 1.0f) {
				{
					OffsetGenerator og = new OffsetGenerator();
					ArrayList<Enemy> wave = new ArrayList<Enemy>();
					wave.add(new Enemy(new Enemy.Zombie01(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
					waveManager.addWave(wave, 5000);
				}
				{
					OffsetGenerator og = new OffsetGenerator();
					ArrayList<Enemy> wave = new ArrayList<Enemy>();
					wave.add(new Enemy(new Enemy.Zombie01(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
					wave.add(new Enemy(new Enemy.Zombie01(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
					waveManager.addWave(wave, 20000);
				}
				{
					OffsetGenerator og = new OffsetGenerator();
					ArrayList<Enemy> wave = new ArrayList<Enemy>();
					wave.add(new Enemy(new Enemy.Zombie01(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
					wave.add(new Enemy(new Enemy.Zombie01(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
					waveManager.addWave(wave, 20000);
				}
				{
					OffsetGenerator og = new OffsetGenerator();
					ArrayList<Enemy> wave = new ArrayList<Enemy>();
					wave.add(new Enemy(new Enemy.Zombie01(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
					wave.add(new Enemy(new Enemy.Zombie01(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
					wave.add(new Enemy(new Enemy.Zombie01(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
					waveManager.addWave(wave, 20000);
				}
			}
			{
				OffsetGenerator og = new OffsetGenerator();
				ArrayList<Enemy> wave = new ArrayList<Enemy>();
				wave.add(new Enemy(new Enemy.Zombie01(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				wave.add(new Enemy(new Enemy.Zombie02(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				wave.add(new Enemy(new Enemy.Zombie01(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				waveManager.addWave(wave, 30000);
			}
			{
				OffsetGenerator og = new OffsetGenerator();
				ArrayList<Enemy> wave = new ArrayList<Enemy>();
				wave.add(new Enemy(new Enemy.Zombie01(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				wave.add(new Enemy(new Enemy.Zombie02(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				wave.add(new Enemy(new Enemy.Zombie02(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				wave.add(new Enemy(new Enemy.Zombie01(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				waveManager.addWave(wave, 30000);
			}
			{
				OffsetGenerator og = new OffsetGenerator();
				ArrayList<Enemy> wave = new ArrayList<Enemy>();
				wave.add(new Enemy(new Enemy.Zombie02(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				wave.add(new Enemy(new Enemy.Zombie02(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				wave.add(new Enemy(new Enemy.Zombie02(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				wave.add(new Enemy(new Enemy.Zombie02(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				wave.add(new Enemy(new Enemy.Zombie02(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				waveManager.addWave(wave, 35000);
			}
			{
				OffsetGenerator og = new OffsetGenerator();
				ArrayList<Enemy> wave = new ArrayList<Enemy>();
				wave.add(new Enemy(new Enemy.Zombie02(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				wave.add(new Enemy(new Enemy.Zombie02(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				wave.add(new Enemy(new Enemy.Zombie02(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				wave.add(new Enemy(new Enemy.Zombie03(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				wave.add(new Enemy(new Enemy.Zombie03(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				waveManager.addWave(wave, 40000);
			}
			{
				OffsetGenerator og = new OffsetGenerator();
				ArrayList<Enemy> wave = new ArrayList<Enemy>();
				wave.add(new Enemy(new Enemy.Zombie03(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				wave.add(new Enemy(new Enemy.Zombie03(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				wave.add(new Enemy(new Enemy.Zombie04(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				wave.add(new Enemy(new Enemy.Zombie04(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				waveManager.addWave(wave, 40000);
			}
			{
				OffsetGenerator og = new OffsetGenerator();
				ArrayList<Enemy> wave = new ArrayList<Enemy>();
				wave.add(new Enemy(new Enemy.Zombie02(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				wave.add(new Enemy(new Enemy.Zombie02(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				wave.add(new Enemy(new Enemy.Zombie02(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				wave.add(new Enemy(new Enemy.Zombie04(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				wave.add(new Enemy(new Enemy.Zombie04(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				wave.add(new Enemy(new Enemy.Zombie04(level), road, og.getVerticalOffset(offset, offsetDir), audioPlayer, resRet));
				waveManager.addWave(wave, 40000);
			}
		}
	}

	public Vector2 drawData(Vector2 pos, SpriteResourceManager res, BitmapFont font) {
		Sprite sprite = res.getSprite(resRet.getBmpScoreSymbol());
		sprite.draw(pos, Sprite.defaultOrigin);
		final Vector2 frameSize = sprite.getFrameSize();
		String str = (new Integer(currentWave).toString()) + " - " + (new Integer(waveManager.getKilledEnemies()).toString());
		font.draw(new Vector2(frameSize.x, 0).add(pos), str);
		return new Vector2(0, frameSize.y);
	}

}

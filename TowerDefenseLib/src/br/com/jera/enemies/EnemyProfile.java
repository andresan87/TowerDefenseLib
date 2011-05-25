package br.com.jera.enemies;

import br.com.jera.resources.PropertyReader;
import br.com.jera.resources.ResourceIdRetriever;

public abstract class EnemyProfile {
	
	public EnemyProfile(final float level, final float enemySpeedScale) {
		this.level = level;
		this.enemySpeedScale = enemySpeedScale;
	}
	
	public float getSpeedBoost() {
		return (PropertyReader.isIncreaseEnemySpeed()) ? level * 2.5f : 0.0f;
	}
	
	public abstract float getSpeed();
	public abstract float getHp();
	public abstract int getResourceId(ResourceIdRetriever resRet);
	public abstract long getFrameStride();
	
	protected final float level;
	protected final float enemySpeedScale;
}

package br.com.jera.enemies;

import br.com.jera.resources.ResourceIdRetriever;

public abstract class EnemyProfile {
	
	public EnemyProfile(final float level) {
		this.level = level;
	}
	
	public abstract float getSpeed();
	public abstract float getHp();
	public abstract int getResourceId(ResourceIdRetriever resRet);
	public abstract long getFrameStride();
	
	protected final float level;
}

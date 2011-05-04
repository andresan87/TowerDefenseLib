package br.com.jera.weapons;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.effects.EffectManager;
import br.com.jera.enemies.Enemy;
import br.com.jera.towerdefenselib.GameCharacter;
import br.com.jera.util.CommonMath.Vector2;

public interface WeaponProfile {
	public float getRange();
	public long getCoolDownTime();
	public int getResourceId();
	public boolean shoot(GameCharacter actor, Vector2 pos, Enemy targetTower, ProjectileManager manager, AudioPlayer audioPlayer);
	public float getSpeed();
	public float getRotationSpeed();
	public HarmEffect getHarmEffect(EffectManager effectManager, GameCharacter actor);
	public int getPrice();
	public float getDamage();
}

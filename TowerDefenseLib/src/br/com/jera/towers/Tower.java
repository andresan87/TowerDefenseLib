package br.com.jera.towers;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.enemies.Enemy;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.towerdefenselib.GameCharacter;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.weapons.ProjectileManager;
import br.com.jera.weapons.WeaponProfile;

public class Tower extends GameCharacter {

	public Tower(TowerProfile profile, Vector2 pos, AudioPlayer audioPlayer, ResourceIdRetriever resRet) {
		super(profile.getResourceId(), pos, audioPlayer, resRet);
		this.weapon = profile.getWeapon();
		super.hp = 100.0f;
		super.speed = 0.0f;
	}

	static TowerProfile[] getTowerProfiles() {
		return towerProfiles;
	}

	// TODO criar perfis de vikings em arquivo resource separado, não no código
	static public void setTowerProfiles(TowerProfile[] towerProfiles) {
		Tower.towerProfiles = towerProfiles;
	}

	public boolean trigger(ProjectileManager manager, Enemy target, AudioPlayer audioPlayer) {
		if (lastShootElapsedTime > weapon.getCoolDownTime()) {
			if (weapon.shoot(this, get2DPos(), target, manager, audioPlayer)) {
				target.setDamageReceived(target.getDamageReceived() + this.getWeapon().getDamage());
				lastShootElapsedTime = 0;
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public void update(final long lastFrameDeltaTimeMS, AudioPlayer audioPlayer) {
		super.update(lastFrameDeltaTimeMS, audioPlayer);
		lastShootElapsedTime += lastFrameDeltaTimeMS;
	}

	public WeaponProfile getWeapon() {
		return weapon;
	}

	private WeaponProfile weapon;
	private long lastShootElapsedTime = 10000;
	static private TowerProfile[] towerProfiles;
	static final float defaultRadius = 24.0f;
}

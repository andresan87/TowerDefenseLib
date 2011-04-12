package br.com.jera.towers;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.enemies.Enemy;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.towerdefenselib.GameCharacter;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.weapons.Axe;
import br.com.jera.weapons.Net;
import br.com.jera.weapons.ProjectileManager;
import br.com.jera.weapons.Spear;
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
	static public void createTowerProfiles(final ResourceIdRetriever resRet) {
		towerProfiles[0] = new TowerProfile() {
			public int getResourceId() {
				return resRet.getBmpTower01();
			}

			public WeaponProfile getWeapon() {
				return weapon;
			}

			private WeaponProfile weapon = new Spear(resRet);
		};
		towerProfiles[1] = new TowerProfile() {
			public int getResourceId() {
				return resRet.getBmpTower02();
			}

			public WeaponProfile getWeapon() {
				return weapon;
			}

			private WeaponProfile weapon = new Net(resRet);

		};
		towerProfiles[2] = new TowerProfile() {
			public int getResourceId() {
				return resRet.getBmpTower03();
			}

			public WeaponProfile getWeapon() {
				return weapon;
			}

			private WeaponProfile weapon = new Axe(resRet);
		};
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
	static final int TOWER_PROFILES = 3;
	static private TowerProfile[] towerProfiles = new TowerProfile[TOWER_PROFILES];
	static final float defaultRadius = 24.0f;
}

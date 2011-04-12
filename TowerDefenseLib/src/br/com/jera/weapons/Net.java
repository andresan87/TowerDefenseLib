package br.com.jera.weapons;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.effects.EffectManager;
import br.com.jera.enemies.Enemy;
import br.com.jera.graphic.GraphicDevice.ALPHA_MODE;
import br.com.jera.graphic.Sprite;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.towerdefenselib.GameCharacter;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.SceneViewer;
import br.com.jera.util.SpriteResourceManager;
import br.com.jera.weapons.ProjectileManager.Projectile;

public class Net implements WeaponProfile {

	private static final float DAMAGE = 0;
	private static ResourceIdRetriever resRet;

	public Net(ResourceIdRetriever resRet) {
		Net.resRet = resRet;
	}

	public float getRange() {
		return 100.0f;
	}

	public long getCoolDownTime() {
		return 5000;
	}

	public int getResourceId() {
		return Net.resRet.getBmpWeaponProjectile02();
	}

	public boolean shoot(GameCharacter actor, Vector2 pos, Enemy targetZombie, ProjectileManager manager, AudioPlayer audioPlayer) {
		if (!targetZombie.hasHarmEffect(TRAP_EFFECT_NAME)) {
			manager.addProjectile(new Projectile(getResourceId(), pos, targetZombie, getSpeed(), getRotationSpeed(), this, actor));
			targetZombie.setNetLaunched(true);
			return true;
		} else {
			return false;
		}
	}

	public float getSpeed() {
		return 50.0f;
	}

	public float getRotationSpeed() {
		return 360.0f;
	}

	public int getPrice() {
		return 70;
	}

	public HarmEffect getHarmEffect(EffectManager effectManager, GameCharacter actor) {
		return new HarmEffect(4500, effectManager, actor) {

			public void applyEffect(GameCharacter target, AudioPlayer audioPlayer) {
				previousSpeed = target.getSpeed();
				target.setSpeed(0.0f);
			}

			public void removeEffect(GameCharacter target, AudioPlayer audioPlayer) {
				target.addToSpeed(previousSpeed);
				((Enemy) target).setNetLaunched(false);
			}

			public void drawHarmEffect(GameCharacter target, SceneViewer viewer, SpriteResourceManager res) {
				Sprite sprite = res.getSprite(Net.resRet.getBmpWeaponHitEffect02());
				res.getGraphicDevice().setAlphaMode(ALPHA_MODE.DEFAULT);
				sprite.draw(target.get2DPos().sub(viewer.getOrthogonalViewerPos().add(netOffset)), GameCharacter.defaultSpriteOrigin);
			}

			public boolean isUnique() {
				return true;
			}

			public String getHarmEffectName() {
				return TRAP_EFFECT_NAME;
			}

			private final Vector2 netOffset = new Vector2(0, 16);
			private float previousSpeed;
		};
	}

	private static final String TRAP_EFFECT_NAME = "NetTrapped";

	public float getDamage() {
		return Net.DAMAGE;
	}
}

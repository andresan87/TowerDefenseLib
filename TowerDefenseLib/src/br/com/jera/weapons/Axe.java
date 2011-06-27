package br.com.jera.weapons;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.effects.AnimatedParticle;
import br.com.jera.effects.EffectManager;
import br.com.jera.enemies.Enemy;
import br.com.jera.enemies.EnemyRoad;
import br.com.jera.resources.PropertyReader;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.towerdefenselib.GameCharacter;
import br.com.jera.util.CommonMath;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.SceneViewer;
import br.com.jera.util.SpriteResourceManager;
import br.com.jera.weapons.ProjectileManager.Projectile;

public class Axe implements WeaponProfile {

	private static final float DAMAGE = -230;
	private static ResourceIdRetriever resRet;

	public Axe(ResourceIdRetriever resRet) {
		Axe.resRet = resRet;
	}

	public float getRange() {
		return 100.0f * PropertyReader.getTowerRangeScale();
	}

	public long getCoolDownTime() {
		return 4000;
	}

	public int getResourceId() {
		return Axe.resRet.getBmpWeaponProjectile(3);
	}

	public boolean shoot(GameCharacter actor, Vector2 pos, Enemy targetZombie, ProjectileManager manager, AudioPlayer audioPlayer) {
		manager.addProjectile(new Projectile(getResourceId(), pos, targetZombie, getSpeed(), getRotationSpeed(), this, actor));
		audioPlayer.play(Axe.resRet.getSfxWeaponTrigger(3));
		return true;
	}

	public float getSpeed() {
		return 120.0f * PropertyReader.getProjectileSpeedFactor();
	}

	public float getRotationSpeed() {
		return (PropertyReader.isSpinAxe()) ? 530.0f : 0.0f;
	}

	public int getPrice() {
		return 240;
	}

	public HarmEffect getHarmEffect(EffectManager effectManager, GameCharacter actor) {
		return new HarmEffect(0, effectManager, actor) {

			public void applyEffect(GameCharacter target, AudioPlayer audioPlayer, EnemyRoad road) {
				target.addToHp(Axe.DAMAGE);
				if (!target.isDead()) {
					audioPlayer.play(Axe.resRet.getSfxWeaponHit(3));
					final Vector2 targetPos = target.get2DPos();
					final Vector2 effectPos = targetPos.add(new Vector2(0, PropertyReader.getHitEffectHeightOffset()));
					float angle = 0.0f;
					if (PropertyReader.isRotateHitEffects()) {
						angle = CommonMath.getAngle(super.actor.get2DPos().sub(targetPos).normalize());
						angle = -CommonMath.radianToDegree(angle) + 90.0f;
					}
					super.effectManager.addEffect(new AnimatedParticle(600, angle, Axe.resRet.getBmpWeaponHitEffect(3), effectPos, 6, 1,
							PropertyReader.getAxeHitEffectRadius()));
				}
			}

			public void removeEffect(GameCharacter target, AudioPlayer audioPlayer) {
			}

			public void drawHarmEffect(GameCharacter target, SceneViewer viewer, SpriteResourceManager res) {
			}

			public boolean isUnique() {
				return false;
			}

			public String getHarmEffectName() {
				return "AxeHit";
			}
		};
	}

	public float getDamage() {
		return Axe.DAMAGE;
	}
}

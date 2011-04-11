package br.com.jera.weapons;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.effects.AnimatedParticle;
import br.com.jera.effects.EffectManager;
import br.com.jera.enemies.Enemy;
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
	
	@Override
	public float getRange() {
		return 100.0f;
	}

	@Override
	public long getCoolDownTime() {
		return 4000;
	}

	@Override
	public int getResourceId() {
		return Axe.resRet.getBmpWeaponProjectile03();
	}

	@Override
	public boolean shoot(GameCharacter actor, Vector2 pos, Enemy targetZombie, ProjectileManager manager, AudioPlayer audioPlayer) {
		manager.addProjectile(new Projectile(getResourceId(), pos, targetZombie, getSpeed(), getRotationSpeed(), this, actor));
		audioPlayer.play(Axe.resRet.getSfxWeaponTrigger03());
		return true;
	}

	@Override
	public float getSpeed() {
		return 120.0f;
	}

	@Override
	public float getRotationSpeed() {
		return 530.0f;
	}

	@Override
	public int getPrice() {
		return 240;
	}

	@Override
	public HarmEffect getHarmEffect(EffectManager effectManager, GameCharacter actor) {
		return new HarmEffect(0, effectManager, actor) {

			@Override
			public void applyEffect(GameCharacter target, AudioPlayer audioPlayer) {
				target.addToHp(Axe.DAMAGE);
				if (!target.isDead()) {
					audioPlayer.play(Axe.resRet.getSfxWeaponHit03());
					final Vector2 targetPos = target.get2DPos();
					final Vector2 effectPos = targetPos.add(ProjectileManager.HEIGHT_OFFSET);
					final float angle = CommonMath.getAngle(super.actor.get2DPos().sub(targetPos).normalize());
					super.effectManager.addEffect(new AnimatedParticle(600, -CommonMath.radianToDegree(angle) + 90.0f,
							Axe.resRet.getBmpWeaponHitEffect03(), effectPos, 6, 1, 52.0f));
				}
			}

			@Override
			public void removeEffect(GameCharacter target, AudioPlayer audioPlayer) {
			}

			@Override
			public void drawHarmEffect(GameCharacter target, SceneViewer viewer, SpriteResourceManager res) {
			}

			@Override
			public boolean isUnique() {
				return false;
			}

			@Override
			public String getHarmEffectName() {
				return "AxeHit";
			}
		};
	}

	@Override
	public float getDamage() {
		return Axe.DAMAGE;
	}
}

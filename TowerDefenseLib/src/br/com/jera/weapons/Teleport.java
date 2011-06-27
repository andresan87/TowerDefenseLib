package br.com.jera.weapons;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.effects.AnimatedParticle;
import br.com.jera.effects.EffectManager;
import br.com.jera.enemies.Enemy;
import br.com.jera.enemies.EnemyRoad;
import br.com.jera.graphic.GraphicDevice.ALPHA_MODE;
import br.com.jera.graphic.Sprite;
import br.com.jera.resources.PropertyReader;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.towerdefenselib.GameCharacter;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.SceneViewer;
import br.com.jera.util.SpriteResourceManager;
import br.com.jera.weapons.ProjectileManager.Projectile;

public class Teleport implements WeaponProfile {

	private static final float DAMAGE = 0;
	private static ResourceIdRetriever resRet;
	private static final String TELEPORT_EFFECT_NAME = "SnapshotTrapped";

	public Teleport(ResourceIdRetriever resRet) {
		Teleport.resRet = resRet;
	}
	
	public float getRange() {
		return 70.0f * PropertyReader.getTowerRangeScale();
	}

	public long getCoolDownTime() {
		return 5000;
	}

	public int getResourceId() {
		return Teleport.resRet.getBmpWeaponProjectile(4);
	}

	public boolean shoot(GameCharacter actor, Vector2 pos, Enemy targetTower, ProjectileManager manager, AudioPlayer audioPlayer) {
		if (!targetTower.hasHarmEffect(TELEPORT_EFFECT_NAME)) {
			manager.addProjectile(new Projectile(getResourceId(), pos, targetTower, getSpeed(), getRotationSpeed(), this, actor));
			audioPlayer.play(Teleport.resRet.getSfxWeaponTrigger(4));
			targetTower.setTeleported(true);
			return true;
		} else {
			return false;
		}
	}

	public float getSpeed() {
		return 500.0f * PropertyReader.getProjectileSpeedFactor();
	}

	public float getRotationSpeed() {
		return 360.0f;
	}

	public HarmEffect getHarmEffect(EffectManager effectManager, GameCharacter actor) {
		return new HarmEffect(3600000, effectManager, actor) {

			public void applyEffect(GameCharacter target, AudioPlayer audioPlayer, EnemyRoad road) {
				if (!target.isDead()) {
					final Vector2 targetPos = target.get2DPos();
					final Vector2 effectPos = targetPos.add(new Vector2(0, PropertyReader.getHitEffectHeightOffset()));
					target.setPos(road.getTilePos(0));
					((Enemy) target).setNextTile(1);
					super.effectManager.addEffect(new AnimatedParticle(400, 0.0f, Teleport.resRet.getBmpWeaponHitEffect(4), effectPos, 4, 1, 30.0f));
				}
			}

			public void removeEffect(GameCharacter target, AudioPlayer audioPlayer) {
				// inimigos que receberam snapshot s� podem receber uma vez
			}

			public void drawHarmEffect(GameCharacter target, SceneViewer viewer, SpriteResourceManager res) {
				Sprite sprite = res.getSprite(Teleport.resRet.getBmpWeaponHitEffect(5));
				res.getGraphicDevice().setAlphaMode(ALPHA_MODE.DEFAULT);
				sprite.draw(target.get2DPos().sub(viewer.getOrthogonalViewerPos()), new Vector2(32,48).add(teleportOffset),
						0.0f, GameCharacter.defaultSpriteOrigin, 0, false);
			}

			public boolean isUnique() {
				return true;
			}

			public String getHarmEffectName() {
				return TELEPORT_EFFECT_NAME;
			}

			private final Vector2 teleportOffset = new Vector2(0,0);
		};
	}

	public int getPrice() {
		return 250;
	}

	public float getDamage() {
		return DAMAGE;
	}

}

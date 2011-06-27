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

public class Snapshot implements WeaponProfile {

	private static final float DAMAGE = 0;
	private static ResourceIdRetriever resRet;
	private static final String SNAPSHOT_EFFECT_NAME = "SnapshotTrapped";

	public Snapshot(ResourceIdRetriever resRet) {
		Snapshot.resRet = resRet;
	}
	
	public float getRange() {
		return 70.0f * PropertyReader.getTowerRangeScale();
	}

	public long getCoolDownTime() {
		return 6000;
	}

	public int getResourceId() {
		return Snapshot.resRet.getBmpWeaponProjectile(2);
	}

	public boolean shoot(GameCharacter actor, Vector2 pos, Enemy targetTower, ProjectileManager manager, AudioPlayer audioPlayer) {
		if (!targetTower.hasHarmEffect(SNAPSHOT_EFFECT_NAME)) {
			manager.addProjectile(new Projectile(getResourceId(), pos, targetTower, getSpeed(), getRotationSpeed(), this, actor));
			targetTower.setNetLaunched(true);
			return true;
		} else {
			return false;
		}
	}

	public float getSpeed() {
		return 50.0f * PropertyReader.getProjectileSpeedFactor();
	}

	public float getRotationSpeed() {
		return 360.0f;
	}

	public HarmEffect getHarmEffect(EffectManager effectManager, GameCharacter actor) {
		return new HarmEffect(3600000, effectManager, actor) {

			public void applyEffect(GameCharacter target, AudioPlayer audioPlayer, EnemyRoad road) {
				if (!target.isDead()) {
					audioPlayer.play(Snapshot.resRet.getSfxWeaponHit(2));
					final Vector2 targetPos = target.get2DPos();
					final Vector2 effectPos = targetPos.add(new Vector2(0, PropertyReader.getHitEffectHeightOffset()));
					target.setPos(road.getTilePos(1));
					((Enemy) target).setNextTile(2);
					super.effectManager.addEffect(new AnimatedParticle(200, 0.0f, Snapshot.resRet.getBmpWeaponHitEffect(4), effectPos, 4, 1, 854.0f));
				}
			}

			public void removeEffect(GameCharacter target, AudioPlayer audioPlayer) {
				// inimigos que receberam snapshot s� podem receber uma vez
				// ((Enemy) target).setNetLaunched(false);
			}

			public void drawHarmEffect(GameCharacter target, SceneViewer viewer, SpriteResourceManager res) {
				Sprite sprite = res.getSprite(Snapshot.resRet.getBmpWeaponHitEffect(2));
				res.getGraphicDevice().setAlphaMode(ALPHA_MODE.DEFAULT);
				// sprite.draw(.add(netOffset), GameCharacter.defaultSpriteOrigin);
				sprite.draw(target.get2DPos().sub(viewer.getOrthogonalViewerPos()), new Vector2(32,32).add(netOffset),
						0.0f, GameCharacter.defaultSpriteOrigin, 0, false);
			}

			public boolean isUnique() {
				return true;
			}

			public String getHarmEffectName() {
				return SNAPSHOT_EFFECT_NAME;
			}

			private final Vector2 netOffset = new Vector2(0,0);
		};
	}

	public int getPrice() {
		return 200;
	}

	public float getDamage() {
		return DAMAGE;
	}

}

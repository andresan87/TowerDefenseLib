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
	
	@Override
	public float getRange() {
		return 100.0f;
	}

	@Override
	public long getCoolDownTime() {
		return 5000;
	}

	@Override
	public int getResourceId() {
		return Net.resRet.getBmpWeaponProjectile02();
	}

	@Override
	public boolean shoot(GameCharacter actor, Vector2 pos, Enemy targetZombie, ProjectileManager manager, AudioPlayer audioPlayer) {
		if (!targetZombie.hasHarmEffect(TRAP_EFFECT_NAME)) {
			manager.addProjectile(new Projectile(getResourceId(), pos, targetZombie, getSpeed(), getRotationSpeed(), this, actor));
			targetZombie.setNetLaunched(true);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public float getSpeed() {
		return 50.0f;
	}

	@Override
	public float getRotationSpeed() {
		return 360.0f;
	}

	@Override
	public int getPrice() {
		return 70;
	}

	@Override
	public HarmEffect getHarmEffect(EffectManager effectManager, GameCharacter actor) {
		return new HarmEffect(4500, effectManager, actor) {

			@Override
			public void applyEffect(GameCharacter target, AudioPlayer audioPlayer) {
				previousSpeed = target.getSpeed();
				target.setSpeed(0.0f);
			}

			@Override
			public void removeEffect(GameCharacter target, AudioPlayer audioPlayer) {
				target.addToSpeed(previousSpeed);
				((Enemy) target).setNetLaunched(false);
			}

			@Override
			public void drawHarmEffect(GameCharacter target, SceneViewer viewer, SpriteResourceManager res) {
				Sprite sprite = res.getSprite(Net.resRet.getBmpWeaponHitEffect02());
				res.getGraphicDevice().setAlphaMode(ALPHA_MODE.DEFAULT);
				sprite.draw(target.get2DPos().sub(viewer.getOrthogonalViewerPos().add(netOffset)), GameCharacter.defaultSpriteOrigin);
			}

			@Override
			public boolean isUnique() {
				return true;
			}

			@Override
			public String getHarmEffectName() {
				return TRAP_EFFECT_NAME;
			}

			private final Vector2 netOffset = new Vector2(0, 16);
			private float previousSpeed;
		};
	}

	private static final String TRAP_EFFECT_NAME = "NetTrapped";

	@Override
	public float getDamage() {
		return Net.DAMAGE;
	}
}

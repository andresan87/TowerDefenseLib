package br.com.jera.weapons;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.effects.EffectManager;
import br.com.jera.towerdefenselib.GameCharacter;
import br.com.jera.util.SceneViewer;
import br.com.jera.util.SpriteResourceManager;

public abstract class HarmEffect {

	public HarmEffect(final long duration, EffectManager effectManager, GameCharacter actor) {
		this.duration = duration;
		this.effectManager = effectManager;
		this.actor = actor;
	}

	public boolean isOver(final long lastFrameDeltaTimeMS) {
		elapsedTime += lastFrameDeltaTimeMS;
		if (elapsedTime > duration)
			return true;
		else
			return false;
	}

	public abstract String getHarmEffectName();

	public abstract boolean isUnique();

	public abstract void applyEffect(GameCharacter target, AudioPlayer audioPlayer);

	public abstract void drawHarmEffect(GameCharacter target, SceneViewer viewer, SpriteResourceManager res);

	public abstract void removeEffect(GameCharacter target, AudioPlayer audioPlayer);

	private final long duration;
	private long elapsedTime = 0;
	protected EffectManager effectManager;
	protected GameCharacter actor;
}

package br.com.jera.effects;

import java.util.LinkedList;
import java.util.ListIterator;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.util.CommonMath.Rectangle2D;
import br.com.jera.util.SceneViewer;
import br.com.jera.util.SpriteResourceManager;

public class EffectManager {

	public void addEffect(TemporaryEffect effect) {
		effects.add(effect);
	}

	public void update(long lastFrameDeltaTimeMS, AudioPlayer audioPlayer) {
		ListIterator<TemporaryEffect> iter = effects.listIterator();
		while (iter.hasNext()) {
			TemporaryEffect effect = iter.next();
			if (!effect.isOver()) {
				effect.update(lastFrameDeltaTimeMS, audioPlayer);
			} else {
				iter.remove();
			}
		}
	}

	public void draw(SceneViewer viewer, SpriteResourceManager res, Rectangle2D clientRect) {
		ListIterator<TemporaryEffect> iter = effects.listIterator();
		while (iter.hasNext()) {
			TemporaryEffect effect = iter.next();
			if (effect.isVisible(viewer, clientRect)) {
				effect.draw(viewer, res);
			}
		}
	}

	private LinkedList<TemporaryEffect> effects = new LinkedList<TemporaryEffect>();
}

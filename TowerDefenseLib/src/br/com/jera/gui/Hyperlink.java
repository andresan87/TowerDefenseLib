package br.com.jera.gui;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.graphic.GraphicDevice;
import br.com.jera.input.InputListener;
import br.com.jera.util.SpriteResourceManager;
import br.com.jera.util.CommonMath.Vector2;

public class Hyperlink extends TouchButton {

	public Hyperlink(Vector2 pos, Vector2 origin, int buttonSpriteId, String url) {
		super(pos, origin, buttonSpriteId, 0, null);
		this.url = url;
	}

	@Override
	public void putButton(GraphicDevice device, AudioPlayer player, SpriteResourceManager res, InputListener input) {
		super.putButton(device, player, res, input);
		if (super.getStatus() == STATUS.ACTIVATED) {
			super.setStatus(STATUS.IDLE);
			device.openUrl(url);
		}
	}

	private final String url;
}

package br.com.jera.gui;

import java.util.HashMap;
import java.util.Map;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.graphic.GraphicDevice;
import br.com.jera.input.InputListener;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.SpriteResourceManager;
import br.com.jeramobstats.JeraAgent;

public class Hyperlink extends TouchButton {

	public Hyperlink(Vector2 pos, Vector2 origin, int buttonSpriteId, String url, float scale) {
		super(pos, origin, buttonSpriteId, 0, null, scale);
		this.url = url;
	}

	@Override
	public void putButton(GraphicDevice device, AudioPlayer player, SpriteResourceManager res, InputListener input) {
		super.putButton(device, player, res, input);
		if (super.getStatus() == STATUS.ACTIVATED) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("url", url);
			JeraAgent.logEvent("HYPERLINK_CLICKED", params);
			super.setStatus(STATUS.IDLE);
			device.openUrl(url);
		}
	}

	private final String url;
}

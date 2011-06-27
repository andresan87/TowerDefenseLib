package br.com.jera.gui;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.graphic.GraphicDevice.ALPHA_MODE;
import br.com.jera.input.InputListener;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.SpriteResourceManager;

public class GlobalSoundSwitch {

	public GlobalSoundSwitch(Vector2 pos, AudioPlayer audioPlayer, ResourceIdRetriever resRet, int customIcon) {
		icon = new TouchButton(pos, new Vector2(0, 0), customIcon, getButtonFrame(audioPlayer), null);
		this.pos = pos;
		// audioPlayer.setGlobalVolume(status ? 1.0f : 0.0f);
	}

	private int getButtonFrame(AudioPlayer audioPlayer) {
		return (audioPlayer.getGlobalVolume() == 0.0f) ? 1 : 0;
	}

	public boolean putButton(Vector2 pos, InputListener input, SpriteResourceManager res, AudioPlayer audioPlayer) {
		res.getGraphicDevice().setAlphaMode(ALPHA_MODE.DEFAULT);
		icon.setPos(pos);
		icon.putButton(res.getGraphicDevice(), audioPlayer, res, input);
		if (icon.getStatus() == TouchButton.STATUS.ACTIVATED) {
			icon.setButtonFrame((icon.getButtonFrame() == 0) ? 1 : 0);
			icon.setStatus(TouchButton.STATUS.IDLE);
		}
		updateVolumeStatus(audioPlayer);
		return audioPlayer.getGlobalVolume() == 0.0f ? false : true;
	}

	public boolean putButton(InputListener input, SpriteResourceManager res, AudioPlayer audioPlayer) {
		return putButton(this.pos, input, res, audioPlayer);
	}

	private void updateVolumeStatus(AudioPlayer audioPlayer) {
		audioPlayer.setGlobalVolume((icon.getButtonFrame() == 0) ? 1.0f : 0.0f);
	}

	private TouchButton icon;
	private Vector2 pos;
}

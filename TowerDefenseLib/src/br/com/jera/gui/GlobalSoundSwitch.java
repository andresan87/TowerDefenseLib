package br.com.jera.gui;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.graphic.GraphicDevice.ALPHA_MODE;
import br.com.jera.input.InputListener;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.SpriteResourceManager;

public class GlobalSoundSwitch {

	public GlobalSoundSwitch(Vector2 pos, AudioPlayer audioPlayer, ResourceIdRetriever resRet) {
		icon = new TouchButton(pos, new Vector2(0, 1), resRet.getBmpSoundToggle(), getButtonFrame(audioPlayer), null, 1.0f);
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

	private void updateVolumeStatus(AudioPlayer audioPlayer) {
		if (icon.getButtonFrame() == 0) {
			audioPlayer.setGlobalVolume(1.0f);
		} else {
			audioPlayer.setGlobalVolume(0.0f);
		}
	}

	private TouchButton icon;
}

package br.com.jera.towers;

import android.app.Activity;
import android.widget.Toast;
import br.com.jera.android.R;
import br.com.jera.audio.AudioPlayer;
import br.com.jera.graphic.GraphicDevice;
import br.com.jera.graphic.GraphicDevice.ALPHA_MODE;
import br.com.jera.graphic.Sprite;
import br.com.jera.gui.TouchButton;
import br.com.jera.input.InputListener;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.towerdefenselib.TDActivity;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.SpriteResourceManager;
import br.com.jera.util.VirtualGoods;

public class SpeedController {

	private static final int SPEED_COUNT = 2;

	public SpeedController(ResourceIdRetriever resRet) {
		this.resRet = resRet;
		button = new TouchButton(Sprite.zero, Sprite.zero, resRet.getBmpSpeedButtons(), 0, new Integer(resRet.getSfxBack()));
	}

	public void putButtons(SpriteResourceManager res, AudioPlayer audioPlayer, InputListener input) {
		GraphicDevice device = res.getGraphicDevice();

		Vector2 cursor = new Vector2(res.getSprite(resRet.getBmpNextWaveButton()).getFrameSize().x, 0);
		button.setPos(cursor);

		if (button.getStatus() == TouchButton.STATUS.ACTIVATED) {
			currentState = (currentState >= SPEED_COUNT) ? 0 : currentState + 1;
			button.setStatus(TouchButton.STATUS.IDLE);
		}
		button.setButtonFrame(currentState);
		device.setAlphaMode(ALPHA_MODE.DEFAULT);
		res.getSprite(resRet.getBmpSpeedButtons()).draw(cursor, Sprite.zero, 0);
		button.putButton(device, audioPlayer, res, input);
	}

	public int getSpeed(SpriteResourceManager res) {
		int speed;
		switch (currentState) {
		case 0:
			speed = 2;
			break;
		case 1:
			speed = 4;
			break;
		case 2:
			if (VirtualGoods.purchasedSpeeds.contains(2) || VirtualGoods.purchasedSpeeds.contains(3)) {
				speed = 8;
			} else {
				if (!toasted)
					TDActivity.toast(R.string.buy_vvzstore, (Activity) res.getGraphicDevice().getContext(), Toast.LENGTH_SHORT);
				toasted = true;
				speed = 2;
				currentState = 0;
			}
			break;
		default:
			speed = 2;
			break;
		}
		return speed;
	}

	private int currentState = 0;
	private TouchButton button;
	private ResourceIdRetriever resRet;
	private boolean toasted = false;
}

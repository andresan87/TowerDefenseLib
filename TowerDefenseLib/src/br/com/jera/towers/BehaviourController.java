package br.com.jera.towers;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.graphic.GraphicDevice;
import br.com.jera.graphic.GraphicDevice.ALPHA_MODE;
import br.com.jera.graphic.Sprite;
import br.com.jera.gui.TouchButton;
import br.com.jera.input.InputListener;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.towers.TowerManager.ShootPriority;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.SpriteResourceManager;

public class BehaviourController {

	public BehaviourController(ResourceIdRetriever resRet) {
		this.resRet = resRet;
		int length = buttons.length;
		for (int t = 0; t < length; t++) {
			buttons[t] = new TouchButton(Sprite.zero, Sprite.zero, resRet.getBmpBehaveButtons(), t, new Integer(resRet.getSfxBack()));
		}
	}

	public void putButtons(SpriteResourceManager res, AudioPlayer audioPlayer, InputListener input) {
		GraphicDevice device = res.getGraphicDevice();
		Vector2 screenSize = device.getScreenSize();
		Vector2 buttonSize = res.getSprite(resRet.getBmpBehaveButtons()).getFrameSize();
		int length = buttons.length;

		Vector2 cursor = new Vector2(res.getSprite(resRet.getBmpBackButton()).getFrameSize().x * 1.1f, screenSize.y - buttonSize.y);
		for (int t = 0; t < length; t++) {
			buttons[t].setPos(cursor);
			buttons[t].putButton(device, audioPlayer, res, input);

			if (buttons[t].getStatus() == TouchButton.STATUS.ACTIVATED) {
				currentState = t;
				buttons[t].setStatus(TouchButton.STATUS.IDLE);
			}

			if (currentState == t) {
				device.setAlphaMode(ALPHA_MODE.ADD);
				res.getSprite(resRet.getBmpBehaveButtons()).draw(cursor, Sprite.zero, t);
				device.setAlphaMode(ALPHA_MODE.DEFAULT);
			}
			cursor = cursor.add(new Vector2(buttonSize.x + 1.0f, 0.0f));
		}
	}

	public ShootPriority getCurrentState() {
		switch (currentState) {
		case 0:
			return ShootPriority.FURTHEST;
		case 1:
		default:
			return ShootPriority.WEAKEST;
		}
	}

	private int currentState = 0;
	private TouchButton[] buttons = new TouchButton[2];
	private ResourceIdRetriever resRet;
}

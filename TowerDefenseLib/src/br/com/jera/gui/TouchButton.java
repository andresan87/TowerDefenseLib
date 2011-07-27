package br.com.jera.gui;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.graphic.GraphicDevice;
import br.com.jera.graphic.GraphicDevice.ALPHA_MODE;
import br.com.jera.graphic.Sprite;
import br.com.jera.input.InputListener;
import br.com.jera.util.CommonMath;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.SpriteResourceManager;

public class TouchButton {

	public enum STATUS {
		PRESSED, SACTIVATED, ACTIVATED, IDLE
	}

	public TouchButton(Vector2 pos, Vector2 origin, int buttonSpriteId, int buttonFrame, Integer soundId, float scale) {
		this.buttonFrame = buttonFrame;
		this.buttonSpriteId = buttonSpriteId;
		this.pos = pos;
		this.soundId = soundId;
		this.origin = origin;
		this.scale = scale;
	}

	public void putButton(GraphicDevice device, AudioPlayer player, SpriteResourceManager res, InputListener input) {
		Sprite sprite = res.getSprite(buttonSpriteId);
		Vector2 currentTouch = input.getCurrentTouch();
		Vector2 buttonSize = sprite.getFrameSize().multiply(scale);
		if (input.getTouchState(0) == InputListener.KEY_STATE.HIT && currentTouch != null)
			hitPos = currentTouch;
		if (currentTouch != null) {
			lastTouch = currentTouch;
			if (CommonMath.isPointInRect(pos, buttonSize, origin, currentTouch)
					&& CommonMath.isPointInRect(pos, buttonSize, origin, hitPos)) {
				status = STATUS.PRESSED;
			} else {
				status = STATUS.IDLE;
			}
		}
		if (input.getTouchState(0) == InputListener.KEY_STATE.RELEASE) {
			if (CommonMath.isPointInRect(pos, buttonSize, origin, lastTouch)
					&& CommonMath.isPointInRect(pos, buttonSize, origin, hitPos)) {
				status = STATUS.SACTIVATED;
			}
		}

		device.setAlphaMode(ALPHA_MODE.DEFAULT);
		if (sprite != null) {
			sprite.draw(pos, buttonSize, 0.0f, origin, buttonFrame, true);
	
			if (status == STATUS.PRESSED) {
				device.setAlphaMode(ALPHA_MODE.ADD);
				sprite.draw(pos, buttonSize, 0.0f, origin, buttonFrame, true);
				device.setAlphaMode(ALPHA_MODE.DEFAULT);
			} else if (status == STATUS.SACTIVATED) {
				if (soundId != null)
					player.play(soundId);
				status = STATUS.ACTIVATED;
			}
		}
	}

	public STATUS getStatus() {
		return status;
	}

	public void setStatus(STATUS status) {
		this.status = status;
	}

	public Vector2 getPos() {
		return pos;
	}

	public void setPos(Vector2 pos) {
		this.pos = pos;
	}

	public void setButtonFrame(int frame) {
		this.buttonFrame = frame;
	}

	public int getButtonFrame() {
		return buttonFrame;
	}
	
	public Vector2 getSize(SpriteResourceManager res) {
		return res.getSprite(buttonSpriteId).getFrameSize();
	}

	private Vector2 hitPos = new Vector2(-30, -30);
	private Vector2 lastTouch = new Vector2();
	private STATUS status = STATUS.IDLE;
	private Vector2 pos, origin;
	private Integer soundId;
	private int buttonFrame;
	private int buttonSpriteId;
	private float scale;
}

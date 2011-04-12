package br.com.jera.effects;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.graphic.GraphicDevice;
import br.com.jera.graphic.GraphicDevice.ALPHA_MODE;
import br.com.jera.graphic.Sprite;
import br.com.jera.input.InputListener;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.util.BaseApplication;
import br.com.jera.util.CommonMath.Vector4;

public class FadeEffect implements BaseApplication {

	protected FadeEffect(long fadeTime, ResourceIdRetriever resRet) {
		this.fadeTime = fadeTime;
		this.resRet = resRet;
		startTime = System.currentTimeMillis();
	}
	
	public enum FADE_STATE {
		FADING_OUT, FADED_OUT, NOT_FADED
	}

	public void resetFrameBuffer(int width, int height) {
	}

	public void create(GraphicDevice device, InputListener input, AudioPlayer player) {
		this.device = device;
	}

	public void draw() {
		final long elapsedTime = System.currentTimeMillis()-startTime; 
		if (elapsedTime <= fadeTime) {
			final float color = 1-((float) elapsedTime/(float) fadeTime);
			drawFadeWall(color);
		}
	}
	
	private void drawFadeWall(float color) {
		device.setAlphaMode(ALPHA_MODE.DEFAULT);
		fadeWall.setColor(new Vector4(0, 0, 0, color));
		fadeWall.draw(Sprite.defaultOrigin, device.getScreenSize(), Sprite.defaultOrigin);
	}
	
	protected FADE_STATE doFadeOut() {
		if (fadeOutTime == 0) {
			fadeOutTime = System.currentTimeMillis();
		}
		final long elapsedTime = System.currentTimeMillis()-fadeOutTime;
		if (elapsedTime > fadeTime) {
			drawFadeWall(1.0f);
			return FADE_STATE.FADED_OUT;
		} else {
			final float color = ((float) elapsedTime/(float) fadeTime);
			drawFadeWall(color);
			return FADE_STATE.FADING_OUT;
		}
	}

	public void loadResources() {
		fadeWall = new Sprite(device, resRet.getBmpWhite(), 1, 1);
	}

	public STATE update(long lastFrameDeltaTimeMS) {
		return null;
	}

	public String getStateName() {
		return null;
	}

	private GraphicDevice device;
	private Sprite fadeWall;
	private long startTime;
	private long fadeOutTime;
	private long fadeTime;
	private ResourceIdRetriever resRet;
}

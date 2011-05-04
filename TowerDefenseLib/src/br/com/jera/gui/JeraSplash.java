package br.com.jera.gui;

import br.com.jera.graphic.GraphicDevice;
import br.com.jera.graphic.GraphicDevice.ALPHA_MODE;
import br.com.jera.graphic.Sprite;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.CommonMath.Vector4;
import br.com.jera.util.SpriteResourceManager;

public class JeraSplash {
	
	public JeraSplash(ResourceIdRetriever resRet) {
		this.resRet = resRet;
	}

	public void draw(SpriteResourceManager res) {
		if (!isShowing()) {
			GraphicDevice device = res.getGraphicDevice();
			Sprite sprite = res.getSprite(resRet.getBmpCompanyLogo());
			Sprite bg = res.getSprite(resRet.getBmpSplashScreenBg());
			Vector2 pos = device.getScreenSize().multiply(0.5f);
			
			device.setAlphaMode(ALPHA_MODE.DEFAULT);
			float alpha;
			if (!isOver())
				alpha = 1.0f;
			else
				alpha = (1.0f-(((float)(System.currentTimeMillis() - SPLASH_LENGTH - start)) / (float) FADE_OUT_LENGTH));
			
			bg.setColor(new Vector4(1,1,1,alpha));
			bg.draw(Sprite.zero, device.getScreenSize(), 0, Sprite.defaultOrigin, 0, true);
			sprite.setColor(new Vector4(1,1,1,alpha));
			sprite.draw(pos, sprite.getBitmapSize(), 0, Sprite.centerOrigin, 0, true);
		}
	}
	
	public boolean isOver() {
		return ((System.currentTimeMillis() - start) > (SPLASH_LENGTH));
	}

	public boolean isShowing() {
		return ((System.currentTimeMillis() - start) > (SPLASH_LENGTH+FADE_OUT_LENGTH));
	}

	public boolean isFading() {
		return (!isOver() && isShowing());
	}

	private static final long SPLASH_LENGTH = 4000;
	private static final long FADE_OUT_LENGTH = 2000;
	private ResourceIdRetriever resRet;

	private final long start = System.currentTimeMillis();

}

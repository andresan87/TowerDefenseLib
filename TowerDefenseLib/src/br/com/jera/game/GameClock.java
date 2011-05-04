package br.com.jera.game;

import br.com.jera.graphic.Sprite;
import br.com.jera.resources.PropertyReader;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.towerdefenselib.OutputData;
import br.com.jera.util.BitmapFont;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.SpriteResourceManager;

public class GameClock implements OutputData.Data {

	private long elapsedTime;
	private ResourceIdRetriever resRet;
	
	public GameClock(ResourceIdRetriever resRet) {
		this.resRet = resRet;
	}

	public void update(final long lastFrameDeltaTimeMS) {
		elapsedTime += lastFrameDeltaTimeMS;
	}

	public String toString() {
		long elapsedTimeMS = elapsedTime;
		long timeLeft = (PropertyReader.getLevelTime() - elapsedTimeMS) / 1000;
		return new Long(timeLeft / 60).toString() + ":" + new Long(timeLeft % 60).toString();
	}
	
	public void drawHelper() {
		
	}

	public Vector2 drawData(Vector2 pos, SpriteResourceManager res, BitmapFont font) {
		Sprite sprite = res.getSprite(resRet.getBmpScoreSymbol());
		sprite.draw(pos, Sprite.defaultOrigin);
		final Vector2 frameSize = sprite.getFrameSize();
		font.draw(new Vector2(frameSize.x, 0).add(pos), this.toString());
		return new Vector2(0, frameSize.y);
	}
}

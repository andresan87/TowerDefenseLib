package br.com.jera.game;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.graphic.Sprite;
import br.com.jera.resources.PropertyReader;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.towerdefenselib.OutputData;
import br.com.jera.util.BitmapFont;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.CommonMath.Vector4;
import br.com.jera.util.SpriteResourceManager;

public class GameClock implements OutputData.Data {

	private long elapsedTime;
	private ResourceIdRetriever resRet;
	private int currentLevel = 1;
	private static final long HELP_FRAME_DURATION = 20000;
	private static final long FADE_OUT_DURATION = 1000;

	public GameClock(ResourceIdRetriever resRet) {
		this.resRet = resRet;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}
	
	public boolean isGameWon() {
		if (currentLevel > PropertyReader.getNumLevels()) {
			return true;
		} else {
			return false;
		}
	}

	public void update(final long lastFrameDeltaTimeMS, SpriteResourceManager res, AudioPlayer audioPlayer) {
		if (elapsedTime == 0) {
			audioPlayer.play(resRet.getSfxNextLevel());
		}
		elapsedTime += lastFrameDeltaTimeMS;
		if (elapsedTime > PropertyReader.getLevelTime()) {
			goToNextLevel(res);
		}
	}

	private void goToNextLevel(SpriteResourceManager res) {
		currentLevel++;
		elapsedTime = 0;

		Sprite character = res.getSprite(resRet.getBmpClockHelpCharacter());
		Sprite text = res.getSprite(resRet.getBmpClockHelpTexts());
		Sprite balloon = res.getSprite(resRet.getBmpClockHelpBalloon());
		Vector4 color = new Vector4(1, 1, 1, 1);
		character.setColor(color);
		text.setColor(color);
		balloon.setColor(color);
	}

	public String toString() {
		long elapsedTimeMS = elapsedTime;
		long timeLeft = (PropertyReader.getLevelTime() - elapsedTimeMS) / 1000;
		long tls = timeLeft % 60;
		return new Long(timeLeft / 60).toString() + ":" + (tls < 10 ? "0" : "") + new Long(tls).toString() + "\nLevel" + new Integer(currentLevel);
	}

	public void drawHelper(SpriteResourceManager res) {
		if (elapsedTime < HELP_FRAME_DURATION + FADE_OUT_DURATION) {
			Sprite character = res.getSprite(resRet.getBmpClockHelpCharacter());
			Sprite text = res.getSprite(resRet.getBmpClockHelpTexts());
			Sprite balloon = res.getSprite(resRet.getBmpClockHelpBalloon());

			Vector2 screenSize = res.getGraphicDevice().getScreenSize();
			Vector2 rightEdge = new Vector2(1, 1);
			Vector2 balloonPos = screenSize.add(new Vector2(-balloon.getFrameSize().x + 26.0f, 0));

			if (elapsedTime > HELP_FRAME_DURATION && elapsedTime < HELP_FRAME_DURATION + FADE_OUT_DURATION) {
				final float alpha = (1.0f - (((float) (elapsedTime - HELP_FRAME_DURATION)) / (float) FADE_OUT_DURATION));
				Vector4 color = new Vector4(1, 1, 1, alpha);
				character.setColor(color);
				text.setColor(color);
				balloon.setColor(color);
			}

			character.draw(screenSize, rightEdge);
			balloon.draw(balloonPos, rightEdge);
			if (currentLevel < text.getNumFrames()) {
				text.draw(balloonPos, rightEdge, currentLevel - 1);
			}
		}
	}

	public Vector2 drawData(Vector2 pos, SpriteResourceManager res, BitmapFont font) {
		Sprite sprite = res.getSprite(resRet.getBmpScoreSymbol());
		sprite.draw(pos, Sprite.defaultOrigin);
		final Vector2 frameSize = sprite.getFrameSize();
		font.draw(new Vector2(frameSize.x, 0).add(pos), this.toString());
		return new Vector2(0, frameSize.y);
	}
}

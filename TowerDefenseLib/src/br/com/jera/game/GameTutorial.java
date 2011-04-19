package br.com.jera.game;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.graphic.Sprite;
import br.com.jera.gui.TouchButton;
import br.com.jera.gui.TouchButton.STATUS;
import br.com.jera.input.InputListener;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.util.CommonMath;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.SpriteResourceManager;

public class GameTutorial {

	public void loadResources(SpriteResourceManager res, ResourceIdRetriever resRet) {
		for (int t = 0; t < resRet.getNumHelpFrames(); t++) {
			res.loadResource(resRet.getBmpHelpFrame(t), 1, 1);
		}
		res.loadResource(resRet.getBmpNextFrameButton(), 1, 1);
		res.loadResource(resRet.getBmpSkipTutorialButton(), 1, 1);

		nextPage = new TouchButton(Sprite.defaultOrigin, new Vector2(1.0f, 0.5f), resRet.getBmpNextFrameButton(), 0, null);
		skipTutorial = new TouchButton(Sprite.defaultOrigin, new Vector2(0.5f, 0.0f), resRet.getBmpSkipTutorialButton(), 0, null);
	}

	private void updateTutorial(SpriteResourceManager res, InputListener input, AudioPlayer player, ResourceIdRetriever resRet) {
		Vector2 screenSize = res.getGraphicDevice().getScreenSize();
		Sprite frame = res.getSprite(resRet.getBmpHelpFrame(currentPage));
		Vector2 frameSize = CommonMath.resizeToMatchHeight(frame.getFrameSize(), screenSize.y);
		frame.draw(screenSize.multiply(0.5f), frameSize, Sprite.centerOrigin);
		putButtons(res, input, player, resRet);
	}

	private void putButtons(SpriteResourceManager res, InputListener input, AudioPlayer player, ResourceIdRetriever resRet) {
		Vector2 screenSize = res.getGraphicDevice().getScreenSize();

		nextPage.setPos(screenSize.multiply(new Vector2(1.0f, 0.5f)));
		nextPage.putButton(res.getGraphicDevice(), player, res, input);

		if (nextPage.getStatus() == STATUS.ACTIVATED) {
			goToNextPage(resRet);
			nextPage.setStatus(STATUS.IDLE);
		}

		skipTutorial.setPos(screenSize.multiply(new Vector2(0.5f, 0.0f)));
		skipTutorial.putButton(res.getGraphicDevice(), player, res, input);

		if (skipTutorial.getStatus() == STATUS.ACTIVATED) {
			finished = true;
		}

	}

	private void goToNextPage(ResourceIdRetriever resRet) {
		if (!isLastPage(resRet)) {
			currentPage++;
		} else {
			finished = true;
		}
	}

	private boolean isLastPage(ResourceIdRetriever resRet) {
		return (currentPage >= resRet.getNumHelpFrames());
	}

	public void doTutorial(SpriteResourceManager res, InputListener input, AudioPlayer player, ResourceIdRetriever resRet) {
		updateTutorial(res, input, player, resRet);
	}

	public boolean isFinished() {
		return finished;
	}

	private boolean finished = false;
	private int currentPage = 0;
	private TouchButton nextPage;
	private TouchButton skipTutorial;
}

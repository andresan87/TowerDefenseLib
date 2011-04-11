package br.com.jera.game;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.graphic.GraphicDevice;
import br.com.jera.input.InputListener;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.util.BaseApplication;

public class StateManager implements BaseApplication {

	public StateManager(String versionStr, ResourceIdRetriever resRet) {
		level = new GameLevel(FADE_IN_TIME, resRet);
		this.versionStr = versionStr;
		this.resRet = resRet;
		currentState = new MainMenu(FADE_IN_TIME, versionStr, resRet);
	}

	@Override
	public void create(GraphicDevice device, InputListener input, AudioPlayer player) {
		currentState.create(device, input, player);
		this.device = device;
		this.input = input;
		this.player = player;
	}

	@Override
	public void loadResources() {
		currentState.loadResources();
	}

	@Override
	public void resetFrameBuffer(int width, int height) {
		currentState.resetFrameBuffer(width, height);
	}

	@Override
	public STATE update(long lastFrameDeltaTimeMS) {
		return currentState.update(lastFrameDeltaTimeMS);
	}

	@Override
	public void draw() {
		currentState.draw();
		checkStateChange();
	}

	private void checkStateChange() {
		if (currentState instanceof MainMenu) {
			switch (((MainMenu) (currentState)).getChosenState()) {
			case NEW_GAME:
				level = new GameLevel(FADE_IN_TIME, resRet);
				currentState = level;
				currentState.create(device, input, player);
				currentState.loadResources();
				currentState.resetFrameBuffer((int) device.getScreenSize().x, (int) device.getScreenSize().y);
				break;
			case CONTINUE:
				currentState = level;
				currentState.create(device, input, player);
				currentState.loadResources();
				currentState.resetFrameBuffer((int) device.getScreenSize().x, (int) device.getScreenSize().y);
				break;
			case NONE:
			default:
				break;
			}
		} else if (currentState instanceof GameLevel) {
			if (level.isRequestingMainMenu()) {
				callMainMenu();
			} else if (level.callGameOver()) {
				currentState = new GameOver(GAMEOVER_FADE_IN_TIME, level.getScore(), resRet);
				currentState.create(device, input, player);
				currentState.loadResources();
				currentState.resetFrameBuffer((int) device.getScreenSize().x, (int) device.getScreenSize().y);
				level = new GameLevel(FADE_IN_TIME, resRet);
			}
		} else if (currentState instanceof GameOver) {
			if (((GameOver) (currentState)).isBackToMainMenu()) {
				callMainMenu();
			}
		}
	}

	private void callMainMenu() {
		currentState = new MainMenu(FADE_IN_TIME, versionStr, resRet);
		currentState.create(device, input, player);
		currentState.loadResources();
		currentState.resetFrameBuffer((int) device.getScreenSize().x, (int) device.getScreenSize().y);
	}

	@Override
	public String getStateName() {
		return "manager";
	}

	private String versionStr;
	private static final long FADE_IN_TIME = 2000;
	private static final long GAMEOVER_FADE_IN_TIME = 4000;
	private BaseApplication currentState;
	private static GameLevel level;
	private ResourceIdRetriever resRet;
	GraphicDevice device;
	InputListener input;
	AudioPlayer player;
}

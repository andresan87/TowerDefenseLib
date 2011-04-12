package br.com.jera.game;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.graphic.GraphicDevice;
import br.com.jera.input.InputListener;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.util.BaseApplication;
import br.com.jera.util.CommonMath.Vector2;

public class StateManager implements BaseApplication {

	public StateManager(String versionStr, ResourceIdRetriever resRet, int tilemapSizeX, int tilemapSizeY, Vector2 tileSize, int[] mainLayer, int[] pathLayer) {
		level = new GameLevel(FADE_IN_TIME, resRet, tilemapSizeX, tilemapSizeY, tileSize, mainLayer, pathLayer);
		this.versionStr = versionStr;
		this.resRet = resRet;
		this.tilemapSizeX = tilemapSizeX;
		this.tilemapSizeY = tilemapSizeY;
		this.tileSize = tileSize;
		this.mainLayer = mainLayer;
		this.pathLayer = pathLayer;
		currentState = new MainMenu(FADE_IN_TIME, versionStr, resRet);
	}

	public void create(GraphicDevice device, InputListener input, AudioPlayer player) {
		currentState.create(device, input, player);
		this.device = device;
		this.input = input;
		this.player = player;
	}

	public void loadResources() {
		currentState.loadResources();
	}

	public void resetFrameBuffer(int width, int height) {
		currentState.resetFrameBuffer(width, height);
	}

	public STATE update(long lastFrameDeltaTimeMS) {
		return currentState.update(lastFrameDeltaTimeMS);
	}

	public void draw() {
		currentState.draw();
		checkStateChange();
	}

	private void checkStateChange() {
		if (currentState instanceof MainMenu) {
			switch (((MainMenu) (currentState)).getChosenState()) {
			case NEW_GAME:
				level = new GameLevel(FADE_IN_TIME, resRet, tilemapSizeX, tilemapSizeY, tileSize, mainLayer, pathLayer);
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
				level = new GameLevel(FADE_IN_TIME, resRet, tilemapSizeX, tilemapSizeY, tileSize, mainLayer, pathLayer);
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
	
	private int tilemapSizeX;
	private int tilemapSizeY;
	private Vector2 tileSize;
	private int[] mainLayer;
	private int[] pathLayer;
}

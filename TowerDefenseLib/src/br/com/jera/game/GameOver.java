package br.com.jera.game;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.effects.FadeEffect;
import br.com.jera.graphic.GraphicDevice;
import br.com.jera.graphic.Sprite;
import br.com.jera.input.InputListener;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.util.BaseApplication;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.SpriteResourceManager;

public class GameOver extends FadeEffect {

	protected GameOver(long fadeTime, int score, ResourceIdRetriever resRet, GameLevel.GAME_STATUS status) {
		super(fadeTime, resRet);
		this.resRet = resRet;
		this.status = status;
		gameOverStartTime = System.currentTimeMillis();
		GameOver.score = new Integer(score);
	}

	@Override
	public void create(GraphicDevice device, InputListener input, AudioPlayer player) {
		super.create(device, input, player);
		this.device = device;
		this.audioPlayer = player;
		res = new SpriteResourceManager(device);
	}

	@Override
	public void loadResources() {
		super.loadResources();
		if (status == GameLevel.GAME_STATUS.WON) {
			gameOver = new Sprite(device, resRet.getBmpGameWon(), 1, 1);	
		} else {
			gameOver = new Sprite(device, resRet.getBmpGameOver(), 1, 1);
		}
		res.loadResource(resRet.getBmpForwarButton(), 1, 1);
		res.loadResource(resRet.getBmpThemeFont16(), 16, 16);
		audioPlayer.load(resRet.getSfxBack());
	}

	@Override
	public void resetFrameBuffer(int width, int height) {
		super.resetFrameBuffer(width, height);
		device.setup2DView(width, height);
	}

	@Override
	public STATE update(long lastFrameDeltaTimeMS) {
		super.update(lastFrameDeltaTimeMS);
		if (System.currentTimeMillis() - gameOverStartTime > gameOverDuration) {
			if (backToMainMenu == false) {
				device.callDialog(GameOver.SUBMIT_SCORE);
			}
			backToMainMenu = true;
		}
		return BaseApplication.STATE.CONTINUE;
	}

	public boolean isBackToMainMenu() {
		MainMenu.mayResume = false;
		return backToMainMenu;
	}

	@Override
	public void draw() {
		device.beginScene();
		final Vector2 screenSize = device.getScreenSize();
		final Vector2 screenMiddle = screenSize.multiply(0.5f);
		gameOver.draw(screenMiddle, new Vector2(0.5f, 0.5f));
		super.draw();
		device.endScene();
	}

	@Override
	public String getStateName() {
		return "gameOver";
	}

	public static int score; // TODO tirar isso logo daqui e passar o score
							 // certinho via Bundle!
	
	private final GameLevel.GAME_STATUS status;
	private final long gameOverDuration = 8000;
	private final long gameOverStartTime;
	private boolean backToMainMenu = false;
	private Sprite gameOver;
	private GraphicDevice device;
	private SpriteResourceManager res;
	private AudioPlayer audioPlayer;
	private ResourceIdRetriever resRet;
	public static final int SUBMIT_SCORE = 10;
}

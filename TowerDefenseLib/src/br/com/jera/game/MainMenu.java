package br.com.jera.game;

import android.app.Activity;
import br.com.jera.audio.AudioPlayer;
import br.com.jera.effects.FadeEffect;
import br.com.jera.graphic.GraphicDevice;
import br.com.jera.graphic.GraphicDevice.ALPHA_MODE;
import br.com.jera.graphic.Sprite;
import br.com.jera.gui.GlobalSoundSwitch;
import br.com.jera.gui.HyperlinkList;
import br.com.jera.gui.JeraSplash;
import br.com.jera.gui.TouchButton;
import br.com.jera.input.InputListener;
import br.com.jera.resources.PropertyReader;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.util.BaseApplication;
import br.com.jera.util.BitmapFont;
import br.com.jera.util.CommonMath;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.SpriteResourceManager;
import br.com.jera.util.VirtualGoods;

public class MainMenu extends FadeEffect {

	protected static boolean mayResume = false;
	private static JeraSplash splash;

	/*
	 * @author unknown
	 */
	private final int SELECT_MAP_DIALOG = 666;

	protected MainMenu(long fadeTime, String versionStr, ResourceIdRetriever resRet) {
		super(fadeTime, resRet);
		this.versionStr = versionStr;
		this.resRet = resRet;
	}

	@Override
	public void create(GraphicDevice device, InputListener input, AudioPlayer player) {
		super.create(device, input, player);
		this.device = device;
		this.input = input;
		this.audioPlayer = player;
		setChosenState(BUTTON.NONE);
	}

	@Override
	public void loadResources() {
		super.loadResources();
		simsun16 = new BitmapFont(device, resRet.getBmpDefaultFont16(), 7, 16);
		resourceManager = new SpriteResourceManager(device);
		resourceManager.loadResource(resRet.getBmpMenuButtons(), 1, 8);
		resourceManager.loadResource(resRet.getBmpMenuBackground(), 1, 1);
		resourceManager.loadResource(resRet.getBmpMenuLogo(), 1, 1);
		resourceManager.loadResource(resRet.getBmpSoundToggle(), 2, 1);
		resourceManager.loadResource(resRet.getBmpCompanyLogo(), 1, 1);
		resourceManager.loadResource(resRet.getBmpSplashScreenBg(), 1, 1);

		if (PropertyReader.isShowMenuLinks()) {
			resourceManager.loadResource(resRet.getBmpTwitterButton(), 1, 1);
			resourceManager.loadResource(resRet.getBmpFacebookButton(), 1, 1);
			hyperlinks = new HyperlinkList(resourceManager, resRet, new Vector2(1.0f, 0.0f));
		}

		if (PropertyReader.hasHelp()) {
			tutorial.loadResources(resourceManager, resRet);
		}

		audioPlayer.load(resRet.getSfxMenuButtonPressed());
		audioPlayer.load(resRet.getSfxBack());
		soundSwitch = new GlobalSoundSwitch(new Vector2(0, 0), audioPlayer, resRet, resRet.getBmpSoundToggle());
		if (splash == null) {
			splash = new JeraSplash(resRet);
		}

		if (PropertyReader.hasClock()) {
			audioPlayer.load(resRet.getSfxNextLevel());
		}
	}

	@Override
	public void resetFrameBuffer(int width, int height) {
		super.resetFrameBuffer(width, height);
		device.setup2DView(width, height);
		Sprite sprite = resourceManager.getSprite(resRet.getBmpMenuButtons());
		Vector2 cursor = device.getScreenSize().sub(new Vector2(0, (menuButtons - 1) * (sprite.getFrameSize().y + 16.0f)));
		for (int n = 0; n < menuButtons; n++) {
			buttons[n] = new TouchButton(cursor, buttonOrigin, resRet.getBmpMenuButtons(), n, new Integer(resRet.getSfxMenuButtonPressed()));
			if (n != 1 || mayResume) {
				cursor = cursor.add(new Vector2(0, (sprite.getFrameSize().y + 16.0f)));
			}
		}
	}

	@Override
	public STATE update(long lastFrameDeltaTimeMS) {
		super.update(lastFrameDeltaTimeMS);

		if (splash != null) {
			if (getChosenState() != BUTTON.TUTORIAL) {
				if (splash.isOver()) {
					if (buttons[0].getStatus() == TouchButton.STATUS.ACTIVATED) {
						if (PropertyReader.hasHelp()) {
							setChosenState(BUTTON.TUTORIAL);
						} else {
							device.callDialog(SELECT_MAP_DIALOG);
						}
						buttons[0].setStatus(TouchButton.STATUS.IDLE);
					} else if (buttons[1].getStatus() == TouchButton.STATUS.ACTIVATED) {
						setChosenState(BUTTON.CONTINUE);
					} else if (buttons[2].getStatus() == TouchButton.STATUS.ACTIVATED) {
						buttons[3].setStatus(TouchButton.STATUS.IDLE);
						device.openUrl(PropertyReader.getScoreUrl());
					} else if (buttons[3].getStatus() == TouchButton.STATUS.ACTIVATED) {
						VirtualGoods goods = new VirtualGoods();
						goods.openStore((Activity) device.getContext());
					} else if (buttons[4].getStatus() == TouchButton.STATUS.ACTIVATED) {
						setChosenState(BUTTON.EXIT);
						splash = null;
						return BaseApplication.STATE.EXIT;
					}
				}
			}
		}
		return BaseApplication.STATE.CONTINUE;
	}

	public BUTTON getChosenState() {
		if (chosenState != BUTTON.NONE && chosenState != BUTTON.TUTORIAL) {
			if (super.doFadeOut() == FadeEffect.FADE_STATE.FADED_OUT)
				return chosenState;
			else
				return BUTTON.NONE;
		}
		return BUTTON.NONE;
	}

	@Override
	public void draw() {
		device.beginScene();
		device.setup2DView();
		device.setDepthTest(false);

		if (getChosenState() != BUTTON.TUTORIAL || !PropertyReader.hasHelp()) {
			Sprite bg = resourceManager.getSprite(resRet.getBmpMenuBackground());
			Vector2 bgSize;
			if (bg != null) {
				if (device.getScreenSize().x > device.getScreenSize().y) {
					bgSize = CommonMath.resizeToMatchWidth(bg.getFrameSize(), device.getScreenSize().x);
				} else {
					bgSize = CommonMath.resizeToMatchHeight(bg.getFrameSize(), device.getScreenSize().y);
				}
				bg.draw(device.getScreenSize().multiply(0.5f), bgSize, Sprite.centerOrigin);
			}
			device.setAlphaMode(ALPHA_MODE.DEFAULT);
			//drawLogo();
			for (int t = 0; t < menuButtons; t++) {
				if (t != 1 || mayResume) {
					buttons[t].putButton(device, audioPlayer, resourceManager, input);
				}
			}
			simsun16.draw(Sprite.zero, versionStr);
			soundSwitch.putButton(new Vector2(0, device.getScreenSize().y - 32.0f), input, resourceManager, audioPlayer);

			if (PropertyReader.isShowMenuLinks()) {
				hyperlinks.putButtons(resourceManager, audioPlayer, input, new Vector2(device.getScreenSize().x, 0.0f), false);
			}

			super.draw();
			if (splash != null)
				splash.draw(resourceManager);
		} else {
			tutorial.doTutorial(resourceManager, input, audioPlayer, resRet);
			if (tutorial.isFinished()) {
				setChosenState(BUTTON.NEW_GAME);
			}
		}
		device.endScene();
	}

	public enum BUTTON {
		NONE, NEW_GAME, CONTINUE, EXIT, TUTORIAL
	}

	@Override
	public String getStateName() {
		return "mainMenu";
	}

	public static void setChosenState(BUTTON chosenState) {
		MainMenu.chosenState = chosenState;
	}

	private String versionStr;
	private final Vector2 buttonOrigin = new Vector2(1, 1);
	private static BUTTON chosenState = BUTTON.NONE;
	private InputListener input;
	private final int menuButtons = 5;
	private BitmapFont simsun16;
	private TouchButton[] buttons = new TouchButton[menuButtons];
	private GraphicDevice device;
	private SpriteResourceManager resourceManager;
	private AudioPlayer audioPlayer;
	private GlobalSoundSwitch soundSwitch;
	private ResourceIdRetriever resRet;
	private HyperlinkList hyperlinks;
	private GameTutorial tutorial = new GameTutorial();
}

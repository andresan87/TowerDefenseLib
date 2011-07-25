package br.com.jera.game;

import android.app.Activity;
import br.com.jera.androidutil.Preferences;
import br.com.jera.audio.AudioPlayer;
import br.com.jera.effects.FadeEffect;
import br.com.jera.graphic.GraphicDevice;
import br.com.jera.graphic.GraphicDevice.ALPHA_MODE;
import br.com.jera.graphic.Sprite;
import br.com.jera.gui.GlobalSoundSwitch;
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
import br.com.jeramobstats.JeraAgent;

public class MainMenu extends FadeEffect {

	protected static boolean mayResume = false;

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
		this.tutorial = new GameTutorial((Activity) device.getContext());
		this.drawMenu = true;
		setChosenState(BUTTON.NONE);
	}

	@Override
	public void loadResources() {
		super.loadResources();
		simsun16 = new BitmapFont(device, resRet.getBmpDefaultFont16(), 7, 16);
		resourceManager = new SpriteResourceManager(device);
		resourceManager.loadResource(resRet.getBmpMainMenu(0), 2, 1);
		resourceManager.loadResource(resRet.getBmpMainMenu(1), 1, 1);
		resourceManager.loadResource(resRet.getBmpMainMenu(2), 1, 1);
		resourceManager.loadResource(resRet.getBmpMainMenu(3), 1, 1);
		resourceManager.loadResource(resRet.getBmpMainMenu(4), 1, 1);
		resourceManager.loadResource(resRet.getBmpMainMenu(5), 1, 1);
		resourceManager.loadResource(resRet.getBmpMainMenu(6), 1, 1);
		resourceManager.loadResource(resRet.getBmpMainMenu(7), 1, 1);
		resourceManager.loadResource(resRet.getBmpMenuBackground(), 1, 1);
		resourceManager.loadResource(resRet.getBmpMenuLogo(), 1, 1);
		resourceManager.loadResource(resRet.getBmpCompanyLogo(), 1, 1);

		audioPlayer.load(resRet.getSfxMenuButtonPressed());
		audioPlayer.load(resRet.getSfxBack());
		soundSwitch = new GlobalSoundSwitch(new Vector2(0, 0), audioPlayer, resRet, resRet.getBmpMainMenu(0));

		if (PropertyReader.hasClock()) {
			audioPlayer.load(resRet.getSfxNextLevel());
		}

		if (PropertyReader.hasHelp()) {
			tutorial.loadResources(resourceManager, resRet);
		}
	}

	@Override
	public void resetFrameBuffer(int width, int height) {
		super.resetFrameBuffer(width, height);
		device.setup2DView(width, height);
		float buttonSpacing = 10.0f;
		float lastButtonSize;
		float barSize = 0;

		for (int n = 0; n < 4; n++) {
			if (!Preferences.PAID || n != 2) {
				barSize += resourceManager.getSprite(resRet.getBmpMainMenu(n)).getFrameSize().x + buttonSpacing;
			}
		}

		Vector2 cursor = new Vector2(device.getScreenSize().x / 2 - barSize / 2, device.getScreenSize().y * 0.75f);

		for (int n = 0; n < 4; n++) {
			if (!Preferences.PAID || n != 2) {
				lastButtonSize = resourceManager.getSprite(resRet.getBmpMainMenu(n)).getFrameSize().x;
				buttons[n] = new TouchButton(cursor, new Vector2(0, 0), resRet.getBmpMainMenu(n), 0, new Integer(resRet.getSfxMenuButtonPressed()));
				cursor = cursor.add(new Vector2(buttonSpacing + lastButtonSize, 0));
			}
		}

		buttons[4] = new TouchButton(new Vector2(device.getScreenSize().x, 0), new Vector2(1, 0), resRet.getBmpMainMenu(4), 0, new Integer(
				resRet.getSfxMenuButtonPressed()));

		buttons[5] = new TouchButton(new Vector2(device.getScreenSize().x * 0.6f, device.getScreenSize().y * 0.4f), new Vector2(0, 0),
				resRet.getBmpMainMenu(5), 0, new Integer(resRet.getSfxMenuButtonPressed()));
		if (mayResume) {
			buttons[5] = new TouchButton(new Vector2(device.getScreenSize().x * 0.6f, device.getScreenSize().y * 0.5f), new Vector2(0, 0),
					resRet.getBmpMainMenu(7), 0, new Integer(resRet.getSfxMenuButtonPressed()));
			buttons[6] = new TouchButton(new Vector2(device.getScreenSize().x * 0.6f, device.getScreenSize().y * 0.3f), new Vector2(0, 0),
					resRet.getBmpMainMenu(6), 0, new Integer(resRet.getSfxMenuButtonPressed()));
		}

	}

	@Override
	public STATE update(long lastFrameDeltaTimeMS) {
		super.update(lastFrameDeltaTimeMS);
		if (chosenState != BUTTON.TUTORIAL) {
			if (buttons[1].getStatus() == TouchButton.STATUS.ACTIVATED) {
				buttons[1].setStatus(TouchButton.STATUS.IDLE);
				device.openUrl(PropertyReader.getScoreUrl());
			} else if (buttons[2] != null && buttons[2].getStatus() == TouchButton.STATUS.ACTIVATED) {
				VirtualGoods goods = new VirtualGoods();
				JeraAgent.logEvent("VVZ_STORE");
				goods.openStore((Activity) device.getContext());
			} else if (buttons[3].getStatus() == TouchButton.STATUS.ACTIVATED) {
				setChosenState(BUTTON.EXIT);
				return BaseApplication.STATE.EXIT;
			} else if (buttons[4] != null && buttons[4].getStatus() == TouchButton.STATUS.ACTIVATED) {
				buttons[4].setStatus(TouchButton.STATUS.IDLE);
				JeraAgent.logEvent("FULL_VERSION");
				device.openUrl(Preferences.getPaidUrl());
			} else if (buttons[5].getStatus() == TouchButton.STATUS.ACTIVATED) {
				device.callDialog(SELECT_MAP_DIALOG);
				buttons[5].setStatus(TouchButton.STATUS.IDLE);
			} else if (buttons[6] != null && buttons[6].getStatus() == TouchButton.STATUS.ACTIVATED) {
				setChosenState(BUTTON.CONTINUE);
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

		if ((chosenState != BUTTON.TUTORIAL || !PropertyReader.hasHelp()) && drawMenu) {

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

			for (int t = 1; t < menuButtons; t++) {
				if (buttons[t] != null && (!Preferences.PAID || (t != 4 && t != 2))) {
					buttons[t].putButton(device, audioPlayer, resourceManager, input);
				}
			}
			soundSwitch.putButton(buttons[0].getPos(), input, resourceManager, audioPlayer);
			simsun16.draw(Sprite.zero, versionStr);

			super.draw();
		} else {
			tutorial.doTutorial(resourceManager, input, audioPlayer, resRet);
			if (tutorial.isFinished()) {
				drawMenu = false;
				chosenState = BUTTON.NEW_GAME;
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
	public static BUTTON chosenState = BUTTON.NONE;
	private boolean drawMenu = true;
	private InputListener input;
	private final int menuButtons = 7;
	private BitmapFont simsun16;
	private TouchButton[] buttons = new TouchButton[menuButtons];
	private GraphicDevice device;
	private SpriteResourceManager resourceManager;
	private AudioPlayer audioPlayer;
	private GlobalSoundSwitch soundSwitch;
	private ResourceIdRetriever resRet;
	private GameTutorial tutorial;
}
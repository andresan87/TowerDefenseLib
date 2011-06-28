package br.com.jera.gui;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.enemies.EnemyRoad;
import br.com.jera.graphic.GraphicDevice;
import br.com.jera.graphic.GraphicDevice.ALPHA_MODE;
import br.com.jera.graphic.Sprite;
import br.com.jera.input.InputListener;
import br.com.jera.resources.PropertyReader;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.towerdefenselib.Player;
import br.com.jera.towerdefenselib.Scenario;
import br.com.jera.towers.TowerManager;
import br.com.jera.towers.TowerSelector;
import br.com.jera.util.BitmapFont;
import br.com.jera.util.Classic2DViewer;
import br.com.jera.util.CommonMath.Rectangle2D;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.SpriteResourceManager;

public class SideBar {

	private void updateArrowPos(GraphicDevice device, SpriteResourceManager res, ResourceIdRetriever resRet) {
		Sprite sprite = res.getSprite(resRet.getBmpSpeedButtons());
		backArrow.setPos(new Vector2(sprite.getFrameSize().x + sprite.getBitmapSize().x, 0));
	}

	public SideBar(GraphicDevice device, InputListener input, Classic2DViewer viewer, TowerManager vikingManager, Scenario scene, SpriteResourceManager res,
			Player player, AudioPlayer audioPlayer, ResourceIdRetriever resRet, int numTowers, float sideBarWidth) {
		this.input = input;
		this.sideBarWidth = sideBarWidth;
		selector = new TowerSelector(this, viewer, vikingManager, scene, res, player, audioPlayer, resRet, numTowers);
		sideBarSprite = new Sprite(device, resRet.getBmpSideBar(), 1, 1);
		sideBarExtend = new Sprite(device, resRet.getBmpSideBarExtend(), 1, 1);
		sideBarBottom = new Sprite(device, resRet.getBmpSideBarBottom(), 1, 1);
		backArrow = new TouchButton(new Vector2(0, 0), new Vector2(0, 0), resRet.getBmpBackButton(), 0, new Integer(resRet.getSfxBack()));
		topBar = new Sprite(device, resRet.getBmpMenu(5), 1, 1);
		Sprite sprite = res.getSprite(resRet.getBmpBackButton());
		updateArrowPos(device, res, resRet);
		sound = new GlobalSoundSwitch(new Vector2(backArrow.getPos().x + sprite.getFrameSize().x, 0), audioPlayer, resRet, resRet.getBmpMenu(4));
		updateArrowPos(device, res, resRet);
	}

	public boolean isRequestingMainMenu() {
		return backArrow.getStatus() == TouchButton.STATUS.ACTIVATED;
	}
	
	public float getSideBarWidth() {
		return sideBarWidth;
	}

	public boolean isOverSideBar(GraphicDevice device, Vector2 v) {
		if (v.x > device.getScreenSize().x - sideBarWidth)
			return true;
		else
			return false;
	}

	public void update(EnemyRoad road, SpriteResourceManager res, TowerManager manager, AudioPlayer audioPlayer, ResourceIdRetriever resRet,
			final long lastFrameDeltaTimeMS) {
		updateArrowPos(res.getGraphicDevice(), res, resRet);
		selector.update(input, res, getClientRect(res.getGraphicDevice()), lastFrameDeltaTimeMS, road, audioPlayer);
	}

	public void draw(SpriteResourceManager res, EnemyRoad road, BitmapFont font, AudioPlayer audioPlayer, ResourceIdRetriever resRet) {
		GraphicDevice device = res.getGraphicDevice();
		device.setAlphaMode(ALPHA_MODE.DEFAULT);
		final Vector2 screenSize = device.getScreenSize();
		if (screenSize.y > sideBarSprite.getFrameSize().y) {
			sideBarExtend.draw(new Vector2(screenSize.x - sideBarWidth, sideBarSprite.getFrameSize().y), new Vector2(0, 0));
		}
		if (!PropertyReader.isHideBottomBar()) {
			sideBarBottom.draw(device.getScreenSize().sub(new Vector2(sideBarWidth, 0)), new Vector2(0.0f, 1.0f));
		}
		backArrow.putButton(device, audioPlayer, res, input);
		sound.putButton(input, res, audioPlayer);
		topBar.draw(new Vector2(sound.getPos().x + res.getSprite(resRet.getBmpMenu(4)).getBitmapSize().x, 0), new Vector2(0.0f, 0.0f));
		sideBarSprite.draw(new Vector2(screenSize.x - sideBarWidth, 0), new Vector2(0, 0));
		selector.draw(input, res, road, font);
	}

	public Rectangle2D getClientRect(GraphicDevice device) {
		return new Rectangle2D(new Vector2(0, 0), device.getScreenSize().sub(new Vector2(sideBarWidth, 0)));
	}

	public Vector2 getSideBarOrigin(GraphicDevice device) {
		return new Vector2(device.getScreenSize().x - sideBarWidth, 0.0f);
	}

	private TowerSelector selector;
	private Sprite sideBarSprite;
	private Sprite sideBarExtend;
	private Sprite sideBarBottom;
	private Sprite topBar;
	private TouchButton backArrow;
	private GlobalSoundSwitch sound;
	private final float sideBarWidth;
	private InputListener input;
}

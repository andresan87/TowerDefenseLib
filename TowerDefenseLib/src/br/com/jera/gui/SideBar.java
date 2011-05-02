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

	private void updateArrowPos(GraphicDevice device) {
		backArrow.setPos(new Vector2(0, device.getScreenSize().y));
	}

	public SideBar(GraphicDevice device, InputListener input, Classic2DViewer viewer, TowerManager vikingManager, Scenario scene,
			SpriteResourceManager res, Player player, AudioPlayer audioPlayer, ResourceIdRetriever resRet, int numTowers, float sideBarWidth) {
		this.input = input;
		this.sideBarWidth = sideBarWidth;
		selector = new TowerSelector(this, viewer, vikingManager, scene, res, player, audioPlayer, resRet, numTowers);
		sideBarSprite = new Sprite(device, resRet.getBmpSideBar(), 1, 1);
		sideBarExtend = new Sprite(device, resRet.getBmpSideBarExtend(), 1, 1);
		sideBarBottom = new Sprite(device, resRet.getBmpSideBarBottom(), 1, 1);
		backArrow = new TouchButton(new Vector2(0, device.getScreenSize().y), new Vector2(0, 1), resRet.getBmpBackButton(), 0, new Integer(resRet.getSfxBack()));
		updateArrowPos(device);
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

	public void update(EnemyRoad road, SpriteResourceManager res, TowerManager manager, AudioPlayer audioPlayer, final long lastFrameDeltaTimeMS) {
		updateArrowPos(res.getGraphicDevice());
		selector.update(input, res, getClientRect(res.getGraphicDevice()), lastFrameDeltaTimeMS, road, audioPlayer);
	}

	public void draw(SpriteResourceManager res, EnemyRoad road, BitmapFont font, AudioPlayer audioPlayer) {
		GraphicDevice device = res.getGraphicDevice();
		device.setAlphaMode(ALPHA_MODE.DEFAULT);
		final Vector2 screenSize = device.getScreenSize();
		sideBarSprite.draw(new Vector2(screenSize.x - sideBarWidth, 0), new Vector2(0, 0));
		if (screenSize.y > sideBarSprite.getFrameSize().y) {
			sideBarExtend.draw(new Vector2(screenSize.x - sideBarWidth, sideBarSprite.getFrameSize().y), new Vector2(0, 0));
		}
		if (!PropertyReader.isHideBottomBar()) {
			sideBarBottom.draw(device.getScreenSize().sub(new Vector2(sideBarWidth, 0)), new Vector2(0.0f, 1.0f));
		}
		backArrow.putButton(device, audioPlayer, res, input);
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
	private TouchButton backArrow;
	private final float sideBarWidth;
	private InputListener input;
}

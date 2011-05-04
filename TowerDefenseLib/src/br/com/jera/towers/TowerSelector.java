package br.com.jera.towers;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.enemies.EnemyRoad;
import br.com.jera.graphic.GraphicDevice;
import br.com.jera.graphic.GraphicDevice.ALPHA_MODE;
import br.com.jera.graphic.Sprite;
import br.com.jera.gui.SideBar;
import br.com.jera.input.InputListener;
import br.com.jera.resources.PropertyReader;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.towerdefenselib.GameCharacter;
import br.com.jera.towerdefenselib.Player;
import br.com.jera.towerdefenselib.Scenario;
import br.com.jera.util.BitmapFont;
import br.com.jera.util.Classic2DViewer;
import br.com.jera.util.CommonMath;
import br.com.jera.util.CommonMath.Rectangle2D;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.CommonMath.Vector4;
import br.com.jera.util.SpriteResourceManager;

public class TowerSelector {

	public TowerSelector(SideBar sideBar, Classic2DViewer viewer, TowerManager manager, Scenario scene, SpriteResourceManager res,
			Player player, AudioPlayer audioPlayer, ResourceIdRetriever resRet, int numTowers) {
		this.selectableVikingPos = new Vector2[numTowers];
		this.player = player;
		this.sideBar = sideBar;
		this.scene = scene;
		this.viewer = viewer;
		this.manager = manager;
		this.resRet = resRet;
	}

	public void update(InputListener input, SpriteResourceManager res, Rectangle2D clientRect, final long lastFrameDeltaTimeMS,
			EnemyRoad road, AudioPlayer audioPlayer) {
		GraphicDevice device = res.getGraphicDevice();
		final Vector2 startPoint = sideBar.getSideBarOrigin(device).add(padding);
		Vector2 cursor = new Vector2(startPoint);
		final int id = Tower.getTowerProfiles()[0].getResourceId();
		Sprite sprite = res.getSprite(id);
		final Vector2 size = sprite.getFrameSize().add(new Vector2(0, 12.0f));
		for (int t = 0; t < selectableVikingPos.length; t++) {
			selectableVikingPos[t] = new Vector2(cursor);
			cursor = cursor.add(new Vector2(size.x, 0.0f));
			if (cursor.x + size.x > device.getScreenSize().x) {
				cursor.x = startPoint.x;
				cursor.y += size.y;
			}
		}
		doGrabber(res, audioPlayer, road, input, sprite.getFrameSize(), clientRect, lastFrameDeltaTimeMS);
	}

	public void draw(InputListener input, SpriteResourceManager res, EnemyRoad road, BitmapFont font) {
		GraphicDevice device = res.getGraphicDevice();
		for (int t = 0; t < selectableVikingPos.length; t++) {

			TowerProfile profile = Tower.getTowerProfiles()[t];
			final int price = profile.getWeapon().getPrice();
			Sprite sprite = res.getSprite(profile.getResourceId());
			if (player.getMoney() < price) {
				device.setAlphaMode(ALPHA_MODE.MODULATE);
			} else {
				device.setAlphaMode(ALPHA_MODE.DEFAULT);
			}
			sprite.draw(selectableVikingPos[t], new Vector2(0, 0));
			device.setAlphaMode(ALPHA_MODE.DEFAULT);
			font.draw(selectableVikingPos[t], new Integer(price).toString());
		}
		drawGrabber(device, input, res, road);
	}
	

	private void doGrabber(SpriteResourceManager res, AudioPlayer audioPlayer, EnemyRoad road, InputListener input, final Vector2 spriteSize, Rectangle2D clientRect,
			final long lastFrameDeltaTimeMS) {
		boolean anySelected = false;
		for (int t = 0; t < selectableVikingPos.length; t++) {
			Vector2 lastTouch = input.getLastTouch();
			if (lastTouch != null) {
				TowerProfile profile = Tower.getTowerProfiles()[t];
				final int price = profile.getWeapon().getPrice();
				if (player.getMoney() >= price) {
					if (CommonMath.isPointInRect(selectableVikingPos[t].sub(buttonPadding), spriteSize.add(buttonPadding), lastTouch)) {
						if (PropertyReader.isUseDragDropSFX()) {
							if (currentVikingGrabbed == null)
								audioPlayer.play(resRet.getSfxTowerDrag());
						}
						currentVikingGrabbed = new Integer(t);
						anySelected = true;
						break;
					}
				}
			}
		}

		Vector2 relativePos = viewer.computeRelativePosition(grabbingTo);
		if (anySelected == false && currentVikingGrabbed != null && isValidPlace(relativePos, road, res)) {
			TowerProfile profile = Tower.getTowerProfiles()[currentVikingGrabbed];
			final int price = profile.getWeapon().getPrice();
			if (player.getMoney() >= price) {
				if (manager.addViking(profile, relativePos, audioPlayer)) {
					player.pay(price);
					if (PropertyReader.isUseDragDropSFX()) {
						audioPlayer.play(resRet.getSfxTowerDrop());
					}
				}
			}
		}

		if (anySelected == false) {
			currentVikingGrabbed = null;
			enableBorderPush = false;
		}
		scrollOnBorders(clientRect, input, lastFrameDeltaTimeMS);
	}

	private boolean isValidPlace(Vector2 relativePos, EnemyRoad road, SpriteResourceManager res) {
		Vector2 absolutePos = viewer.computeAbsolutePosition(relativePos);
		return !(road.isPointOnRoad(relativePos) || manager.hasUnityIn(relativePos) || sideBar.isOverSideBar(res.getGraphicDevice(), absolutePos) ||
				!scene.isPointInScene(relativePos, res));
	}

	private Vector2 validatePos(Vector2 currentPos, Vector2 nextPos, EnemyRoad road, SpriteResourceManager res) {
		Vector2 relativeCurrentPos = viewer.computeRelativePosition(currentPos);
		Vector2 relativeNextPos = viewer.computeRelativePosition(nextPos);
		if (isValidPlace(relativeNextPos, road, res)) {
			return nextPos;
		} else if (relativeNextPos.distance(relativeCurrentPos) > PropertyReader.getSmartPlaceTolerance()) {
			return nextPos;
		} else {
			Vector2 testWithX = new Vector2(relativeNextPos.x, relativeCurrentPos.y);
			Vector2 testWithY = new Vector2(relativeCurrentPos.x, relativeNextPos.y);
			if (isValidPlace(testWithX, road, res)) {
				return viewer.computeAbsolutePosition(testWithX);
			}
			else if (isValidPlace(testWithY, road, res)) { 
				return viewer.computeAbsolutePosition(testWithY);
			}
			return grabbingTo;
		}
	}
	
	private void drawGrabber(GraphicDevice device, InputListener input, SpriteResourceManager res, EnemyRoad road) {
		final Vector2 currentTouch = input.getCurrentTouch();
		if (currentVikingGrabbed != null && currentTouch != null) {
			// TODO fazer as operações de atualização no método update
			grabbingTo = validatePos(grabbingTo, currentTouch, road, res);

			device.setAlphaMode(ALPHA_MODE.ADD);
			Sprite sphere = res.getSprite(resRet.getBmpRange());
			Sprite sprite = res.getSprite(Tower.getTowerProfiles()[currentVikingGrabbed].getResourceId());

			Vector4 color = GraphicDevice.COLOR_GREEN;
			final Vector2 relativePos = viewer.computeRelativePosition(grabbingTo);
			if (!isValidPlace(relativePos, road, res)) {
				color = GraphicDevice.COLOR_RED;
			}
			sphere.setColor(color);
			final float weaponRangeDiameter = Tower.getTowerProfiles()[currentVikingGrabbed].getWeapon().getRange() * 2.0f;
			sphere.draw(grabbingTo, new Vector2(weaponRangeDiameter, weaponRangeDiameter), 0.0f, Sprite.centerOrigin, 0, false);
			sprite.draw(grabbingTo, GameCharacter.defaultSpriteOrigin);
		}
	}

	private void scrollOnBorders(Rectangle2D clientRect, InputListener input, final long lastFrameDeltaTimeMS) {
		final float bias = (float) ((double) lastFrameDeltaTimeMS / 1000.0);
		final Vector2 currentTouch = input.getCurrentTouch();
		if (currentVikingGrabbed != null && currentTouch != null) {
			if (viewer.isPointOnBorder(currentTouch, borderWidth, clientRect)) {
				if (enableBorderPush) {
					final Vector2 move = clientRect.getCenter().sub(currentTouch).multiply(-bias);
					viewer.scroll(move);
				}
			} else if (clientRect.isPointInRectangle(currentTouch)) {
				enableBorderPush = true;
			}
		}
	}

	private Player player;
	private Scenario scene;
	private TowerManager manager;
	private Vector2 grabbingTo = new Vector2();
	private boolean enableBorderPush = false;
	private final float borderWidth = 32.0f;
	private final Vector2 buttonPadding = new Vector2(6, 6);
	private Classic2DViewer viewer;
	private Integer currentVikingGrabbed = null;
	private static Vector2 padding = new Vector2(16.0f, 16.0f);
	private SideBar sideBar;
	private ResourceIdRetriever resRet;
	private Vector2[] selectableVikingPos;
}

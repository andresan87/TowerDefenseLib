package br.com.jera.game;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.effects.EffectManager;
import br.com.jera.effects.FadeEffect;
import br.com.jera.enemies.EnemyRoad;
import br.com.jera.enemies.EnemyWaveManager;
import br.com.jera.enemies.InfiniteWaveManager;
import br.com.jera.graphic.GraphicDevice;
import br.com.jera.graphic.GraphicDevice.ALPHA_MODE;
import br.com.jera.graphic.Sprite;
import br.com.jera.gui.SideBar;
import br.com.jera.gui.TouchButton;
import br.com.jera.input.InputListener;
import br.com.jera.resources.PropertyReader;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.towerdefenselib.OutputData;
import br.com.jera.towerdefenselib.Player;
import br.com.jera.towerdefenselib.Scenario;
import br.com.jera.towerdefenselib.SortedDisplayableEntityList;
import br.com.jera.towers.Tower;
import br.com.jera.towers.TowerManager;
import br.com.jera.towers.TowerProfile;
import br.com.jera.util.BaseApplication;
import br.com.jera.util.BitmapFont;
import br.com.jera.util.Classic2DViewer;
import br.com.jera.util.CommonMath.Rectangle2D;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.CommonMath.Vector4;
import br.com.jera.util.DisplayableEntity;
import br.com.jera.util.SpriteResourceManager;
import br.com.jera.weapons.ProjectileManager;

public class GameLevel extends FadeEffect {

	protected GameLevel(long fadeTime, ResourceIdRetriever resRet, int tilemapSizeX, int tilemapSizeY, Vector2 tileSize, int[] mainLayer,
			int[] pathLayer, TowerProfile[] towerProfiles) {
		super(fadeTime, resRet);
		this.resRet = resRet;
		this.numTowers = towerProfiles.length;
		this.tilemapSizeX = tilemapSizeX;
		this.tilemapSizeY = tilemapSizeY;
		this.tileSize = tileSize;
		this.mainLayer = mainLayer;
		this.pathLayer = pathLayer;
		forceNextWave = new TouchButton(Sprite.zero, Sprite.zero, resRet.getBmpNextWaveButton(), 0, resRet.getSfxBack());
		Tower.setTowerProfiles(towerProfiles); // TODO carregar profiles de
												// arquivo e não via static
	}

	@Override
	public void create(GraphicDevice device, InputListener input, AudioPlayer audioPlayer) {
		super.create(device, input, audioPlayer);
		graphicDevice = device;
		this.input = input;
		this.audioPlayer = audioPlayer;
		spriteManager = new SpriteResourceManager(device);
		gameClock = new GameClock(resRet);

		if (isFirstTime) {
			scenario = new Scenario(resRet.getBmpScenario(), new Vector2(0, 0), resRet);
			road = new EnemyRoad(resRet, tilemapSizeX, tilemapSizeY, tileSize, mainLayer, pathLayer);
			waveManager = new EnemyWaveManager(road, System.currentTimeMillis(), resRet);
			vikingManager = new TowerManager(resRet);
			projectileManager = new ProjectileManager();
			effectManager = new EffectManager();
			infiniteWaveManager = new InfiniteWaveManager(waveManager, road, resRet);
			outputData = new OutputData();
			player = new Player(resRet);
		}
		isFirstTime = false;
		requestingMainMenu = false;
	}

	@Override
	public void loadResources() {
		super.loadResources();

		// TODO carregar recursos específicos de forma automatica fora do código
		sideBar = new SideBar(graphicDevice, input, viewer, vikingManager, scenario, spriteManager, player, audioPlayer, resRet, numTowers,
				PropertyReader.getSideBarWidth());
		viking16 = new BitmapFont(graphicDevice, resRet.getBmpThemeFont16(), PropertyReader.getDefaultFontSize() / 2,
				PropertyReader.getDefaultFontSize());
		spriteManager.loadResource(resRet.getBmpScenario(), 1, 1);
		spriteManager.loadResource(resRet.getBmpTower01(), 4, 4);
		spriteManager.loadResource(resRet.getBmpTower02(), 4, 4);
		spriteManager.loadResource(resRet.getBmpTower03(), 4, 4);
		spriteManager.loadResource(resRet.getBmpEnemy01(), 4, 4);
		spriteManager.loadResource(resRet.getBmpEnemy02(), 4, 4);
		spriteManager.loadResource(resRet.getBmpEnemy03(), 4, 4);
		spriteManager.loadResource(resRet.getBmpEnemy04(), 4, 4);
		spriteManager.loadResource(resRet.getBmpShadow(), 1, 1);
		spriteManager.loadResource(resRet.getBmpProgessBar(), 1, 1);
		spriteManager.loadResource(resRet.getBmpTiles(), 4, 4);
		spriteManager.loadResource(resRet.getBmpRange(), 1, 1);
		spriteManager.loadResource(resRet.getBmpWeaponProjectile01(), 1, 1);
		spriteManager.loadResource(resRet.getBmpWeaponProjectile02(), 1, 1);
		spriteManager.loadResource(resRet.getBmpWeaponProjectile03(), 1, 1);
		spriteManager.loadResource(resRet.getBmpWeaponHitEffect01(), 6, 1);
		spriteManager.loadResource(resRet.getBmpWeaponHitEffect02(), 1, 1);
		spriteManager.loadResource(resRet.getBmpWeaponHitEffect03(), 6, 1);
		spriteManager.loadResource(resRet.getBmpDeathAnim(), 6, 1);
		spriteManager.loadResource(resRet.getBmpNextWaveSymbol(), 1, 1);
		spriteManager.loadResource(resRet.getBmpScenarioSeam(), 1, 1);
		spriteManager.loadResource(resRet.getBmpMoneySymbol(), 1, 1);
		spriteManager.loadResource(resRet.getBmpTowerSymbol(), 1, 1);
		spriteManager.loadResource(resRet.getBmpScoreSymbol(), 1, 1);
		spriteManager.loadResource(resRet.getBmpBackButton(), 1, 1);
		spriteManager.loadResource(resRet.getBmpNextWaveButton(), 1, 1);
		spriteManager.loadResource(resRet.getBmpBehaveButtons(), 1, 4);

		if (PropertyReader.hasSnapshotFX()) {
			spriteManager.loadResource(resRet.getBmpSnapshotFX(), 4, 1);
		}

		if (PropertyReader.hasClock()) {
			spriteManager.loadResource(resRet.getBmpClockHelpCharacter(), 1, 1);
		}

		audioPlayer.load(resRet.getSfxGameOver());
		audioPlayer.load(resRet.getSfxWeaponTrigger01());
		audioPlayer.load(resRet.getSfxWeaponTrigger02());
		audioPlayer.load(resRet.getSfxWeaponHit01());
		audioPlayer.load(resRet.getSfxWeaponHit02());
		audioPlayer.load(resRet.getSfxWeaponTrigger03());
		audioPlayer.load(resRet.getSfxWeaponHit03());
		audioPlayer.load(resRet.getSfxEnemyDeath());
		audioPlayer.load(resRet.getSfxBack());

		if (PropertyReader.isUseDragDropSFX()) {
			audioPlayer.load(resRet.getSfxTowerDrop());
			audioPlayer.load(resRet.getSfxTowerDrag());
		}
	}

	@Override
	public void resetFrameBuffer(int width, int height) {
		super.resetFrameBuffer(width, height);
		graphicDevice.setup2DView(width, height);
		resetScrollBounds(scenario);
	}

	@Override
	public STATE update(final long lastFrameDeltaTimeMS) {
		scrollView();
		effectManager.update(lastFrameDeltaTimeMS, audioPlayer);
		projectileManager.update(lastFrameDeltaTimeMS, effectManager, audioPlayer, spriteManager, road);
		waveManager.update(lastFrameDeltaTimeMS, effectManager, player, audioPlayer);
		vikingManager.update(lastFrameDeltaTimeMS, waveManager.getEnemies(), projectileManager, audioPlayer);
		requestingMainMenu = sideBar.isRequestingMainMenu();
		sideBar.update(road, spriteManager, vikingManager, audioPlayer, lastFrameDeltaTimeMS);
		infiniteWaveManager.updateWaveManager(audioPlayer, waveManager, spriteManager.getSprite(resRet.getBmpEnemy01()).getFrameSize().x);

		if (PropertyReader.hasClock()) {
			gameClock.update(lastFrameDeltaTimeMS);
		}
		return BaseApplication.STATE.CONTINUE;
	}

	@Override
	public void draw() {
		final Rectangle2D clientRect = sideBar.getClientRect(graphicDevice);
		listOutputData();

		// TODO feio
		if (PropertyReader.isHideBottomBar()) {
			graphicDevice.setBackgroundColor(new Vector4(228.0f / 255.0f, 243.0f / 255.0f, 242.0f / 255.0f, 1.0f));
		}
		graphicDevice.beginScene();

		// TODO feio [2]
		if (PropertyReader.isHideBottomBar()) {
			graphicDevice.setBackgroundColor(new Vector4(0.0f, 0.0f, 0.0f, 1.0f));
		}
		graphicDevice.setup2DView();
		graphicDevice.setDepthTest(false);
		graphicDevice.setAlphaMode(ALPHA_MODE.DEFAULT);
		scenario.draw(viewer, spriteManager);
		road.draw(spriteManager, viewer);
		waveManager.draw(viewer, displayList, clientRect);
		vikingManager.draw(viewer, displayList, clientRect, spriteManager, audioPlayer, input);
		projectileManager.draw(viewer, displayList, clientRect);
		displayList.draw(viewer, spriteManager);
		effectManager.draw(viewer, spriteManager, clientRect);
		sideBar.draw(spriteManager, road, viking16, audioPlayer);
		outputData.draw(new Vector2(0, 32), viking16, spriteManager);
		putNextWaveButton();
		super.draw();
		graphicDevice.endScene();
	}

	private void putNextWaveButton() {
		putNextWaveButton(Sprite.zero);
	}

	private void putNextWaveButton(Vector2 pos) {
		forceNextWave.setPos(pos);
		forceNextWave.putButton(graphicDevice, audioPlayer, spriteManager, input);
		if (forceNextWave.getStatus() == TouchButton.STATUS.ACTIVATED) {
			forceNextWave.setStatus(TouchButton.STATUS.IDLE);
			waveManager.forceNextWave();
		}
	}

	private void listOutputData() {
		outputData.add(vikingManager);
		outputData.add(player);

		if (PropertyReader.hasClock()) {
			outputData.add(gameClock);
		} else {
			outputData.add(infiniteWaveManager);
		}

		if (PropertyReader.isShowNextWaveTime()) {
			outputData.add(waveManager);
		}
	}

	private void resetScrollBounds(DisplayableEntity scene) {
		viewer.setScrollBounds(scene.getMin(spriteManager), scene.getMax(spriteManager),
				graphicDevice.getScreenSize().sub(new Vector2(sideBar.getSideBarWidth(), 0)));
		if (!PropertyReader.isCustomViewStart()) {
			viewer.scrollTo(new Vector2(((scene.getMin(spriteManager).x + scene.getMax(spriteManager).x / 2.0f))
					- (graphicDevice.getScreenSize().x / 2.0f), scene.getMax(spriteManager).y));
		} else {
			viewer.scrollTo(PropertyReader.getViewStart());
		}
	}

	private void scrollView() {
		Vector2 lastTouch = input.getLastTouch();
		if (lastTouch != null) {
			if (isInSceneArea(lastTouch)) {
				final Vector2 touchMove = input.getTouchMove();
				if (touchMove.length() > 0.0f) {
					viewer.scroll(touchMove.multiply(-1));
				}
			}
		}
	}

	public boolean callGameOver() {
		if (waveManager.hasReachedLastNode()) {
			if (super.doFadeOut() == FadeEffect.FADE_STATE.FADED_OUT) {
				audioPlayer.play(resRet.getSfxGameOver());
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	private boolean isInSceneArea(final Vector2 pos) {
		return !(pos.x < 0 || pos.y < 0 || sideBar.isOverSideBar(graphicDevice, pos));
	}

	public boolean isRequestingMainMenu() {
		if (requestingMainMenu) {
			MainMenu.mayResume = true;
		}
		return requestingMainMenu;
	}

	@Override
	public String getStateName() {
		return "game";
	}

	public int getScore() {
		return waveManager.getKilledEnemies();
	}

	private TouchButton forceNextWave;
	private AudioPlayer audioPlayer;
	private Player player;
	private OutputData outputData;
	private BitmapFont viking16;
	private InfiniteWaveManager infiniteWaveManager;
	private SortedDisplayableEntityList displayList = new SortedDisplayableEntityList();
	private TowerManager vikingManager;
	private ProjectileManager projectileManager;
	private Scenario scenario;
	private GraphicDevice graphicDevice;
	private InputListener input;
	private Classic2DViewer viewer = new Classic2DViewer(new Vector2(0, 0));
	private SideBar sideBar;
	private SpriteResourceManager spriteManager;
	private boolean isFirstTime = true;
	private EnemyRoad road;
	private boolean requestingMainMenu = false;
	private EnemyWaveManager waveManager;
	private EffectManager effectManager;
	private ResourceIdRetriever resRet;
	private GameClock gameClock;

	private final int numTowers;
	private final int tilemapSizeX;
	private final int tilemapSizeY;
	private final Vector2 tileSize;
	private final int[] mainLayer;
	private final int[] pathLayer;
}

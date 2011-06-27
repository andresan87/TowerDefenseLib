package br.com.jera.game;

import android.os.Build;
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
import br.com.jera.towers.SpeedController;
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

	public static int CURRENT_MAP = 0;

	public enum GAME_STATUS {
		LOST, WON, RUNNING
	}

	protected GameLevel(long fadeTime, ResourceIdRetriever resRet, TowerProfile[] towerProfiles) {
		super(fadeTime, resRet);
		this.resRet = resRet;
		this.numTowers = towerProfiles.length;
		forceNextWave = new TouchButton(Sprite.zero, Sprite.zero, resRet.getBmpNextWaveButton(), 0, resRet.getSfxBack());
		Tower.setTowerProfiles(towerProfiles);
	}

	@Override
	public void create(GraphicDevice device, InputListener input, AudioPlayer audioPlayer) {
		super.create(device, input, audioPlayer);
		graphicDevice = device;
		this.input = input;
		this.audioPlayer = audioPlayer;
		spriteManager = new SpriteResourceManager(device);

		if (isFirstTime) {
			scenario = new Scenario(resRet.getBmpScenario(GameLevel.CURRENT_MAP), new Vector2(0, 0), resRet);
			road = new EnemyRoad(resRet, GameLevel.tilemapSizeX, GameLevel.tilemapSizeY, GameLevel.tileSize, GameLevel.mainLayer, GameLevel.pathLayer);
			waveManager = new EnemyWaveManager(road, System.currentTimeMillis(), resRet);
			towerManager = new TowerManager(resRet);
			projectileManager = new ProjectileManager();
			effectManager = new EffectManager();
			infiniteWaveManager = new InfiniteWaveManager(waveManager, road, resRet);
			outputData = new OutputData();
			player = new Player(resRet);
			gameClock = new GameClock(resRet);
		}
		isFirstTime = false;
		requestingMainMenu = false;
	}

	@Override
	public void loadResources() {
		super.loadResources();

		// TODO carregar recursos específicos de forma automatica fora do código
		viking16 = new BitmapFont(graphicDevice, resRet.getBmpThemeFont16(), PropertyReader.getDefaultFontSize() / 2, PropertyReader.getDefaultFontSize());
		spriteManager.loadResource(resRet.getBmpScenario(GameLevel.CURRENT_MAP), 1, 1);
		spriteManager.loadResource(resRet.getBmpTower(1), 4, 4);
		spriteManager.loadResource(resRet.getBmpTower(2), 4, 4);
		spriteManager.loadResource(resRet.getBmpTower(3), 4, 4);
		spriteManager.loadResource(resRet.getBmpTower(4), 4, 4);
		spriteManager.loadResource(resRet.getBmpEnemy(1), 4, 4);
		spriteManager.loadResource(resRet.getBmpEnemy(2), 4, 4);
		spriteManager.loadResource(resRet.getBmpEnemy(3), 4, 4);
		spriteManager.loadResource(resRet.getBmpEnemy(4), 4, 4);
		spriteManager.loadResource(resRet.getBmpShadow(), 1, 1);
		spriteManager.loadResource(resRet.getBmpProgessBar(), 1, 1);
		spriteManager.loadResource(resRet.getBmpTiles(), 4, 4);
		spriteManager.loadResource(resRet.getBmpRange(), 1, 1);
		spriteManager.loadResource(resRet.getBmpMenu(4), 1, 2);
		spriteManager.loadResource(resRet.getBmpMenu(5), 1, 1);
		spriteManager.loadResource(resRet.getBmpWeaponProjectile(1), 1, 1);
		spriteManager.loadResource(resRet.getBmpWeaponProjectile(2), 1, 1);
		spriteManager.loadResource(resRet.getBmpWeaponProjectile(3), 1, 1);
		spriteManager.loadResource(resRet.getBmpWeaponProjectile(4), 1, 1);
		spriteManager.loadResource(resRet.getBmpWeaponHitEffect(1), 6, 1);
		spriteManager.loadResource(resRet.getBmpWeaponHitEffect(2), 1, 1);
		spriteManager.loadResource(resRet.getBmpWeaponHitEffect(3), 6, 1);
		spriteManager.loadResource(resRet.getBmpWeaponHitEffect(4), 6, 1);
		spriteManager.loadResource(resRet.getBmpWeaponHitEffect(5), 1, 1);
		spriteManager.loadResource(resRet.getBmpDeathAnim(), 6, 1);
		spriteManager.loadResource(resRet.getBmpNextWaveSymbol(), 1, 1);
		spriteManager.loadResource(resRet.getBmpScenarioSeam(), 1, 1);
		spriteManager.loadResource(resRet.getBmpMoneySymbol(), 1, 1);
		spriteManager.loadResource(resRet.getBmpTowerSymbol(), 1, 1);
		spriteManager.loadResource(resRet.getBmpScoreSymbol(), 1, 1);
		spriteManager.loadResource(resRet.getBmpNextWaveButton(), 1, 1);
		spriteManager.loadResource(resRet.getBmpSpeedButtons(), 1, 4);
		spriteManager.loadResource(resRet.getBmpBackButton(), 1, 1);

		sideBar = new SideBar(graphicDevice, input, viewer, towerManager, scenario, spriteManager, player, audioPlayer, resRet, numTowers,
				PropertyReader.getSideBarWidth());

		if (PropertyReader.hasSnapshotFX()) {
			spriteManager.loadResource(resRet.getBmpWeaponHitEffect(4), 4, 1);
		}

		if (PropertyReader.hasClock()) {
			spriteManager.loadResource(resRet.getBmpClockHelpCharacter(), 1, 1);
			spriteManager.loadResource(resRet.getBmpClockHelpBalloon(), 1, 1);
			spriteManager.loadResource(resRet.getBmpClockHelpTexts(), 2, 2);
		}

		audioPlayer.load(resRet.getSfxEnemyDeath());
		audioPlayer.load(resRet.getSfxWeaponTrigger(1));
		audioPlayer.load(resRet.getSfxWeaponTrigger(3));
		audioPlayer.load(resRet.getSfxWeaponTrigger(4));
		audioPlayer.load(resRet.getSfxWeaponHit(1));
		audioPlayer.load(resRet.getSfxWeaponHit(3));
		audioPlayer.load(resRet.getSfxEnemyDeath(1));
		audioPlayer.load(resRet.getSfxEnemyDeath(2));
		audioPlayer.load(resRet.getSfxEnemyDeath(3));
		audioPlayer.load(resRet.getSfxEnemyDeath(4));
		audioPlayer.load(resRet.getSfxEnemyDeath(5));
		audioPlayer.load(resRet.getSfxBack());

		if (PropertyReader.isUseDragDropSFX()) {
			audioPlayer.load(resRet.getSfxTowerDrop());
			audioPlayer.load(resRet.getSfxTowerDrag());
		}

		if (PropertyReader.hasClock()) {
			audioPlayer.load(resRet.getSfxNextLevel());
			audioPlayer.load(resRet.getSfxGameWon());
		}

		speedController = new SpeedController(resRet);
	}

	@Override
	public void resetFrameBuffer(int width, int height) {
		super.resetFrameBuffer(width, height);
		graphicDevice.setup2DView(width, height);
		resetScrollBounds(scenario);
	}

	@Override
	public STATE update(long lastFrameDeltaTimeMS) {
		lastFrameDeltaTimeMS *= speedController.getSpeed(spriteManager);
		scrollView();
		effectManager.update(lastFrameDeltaTimeMS, audioPlayer);
		projectileManager.update(lastFrameDeltaTimeMS, effectManager, audioPlayer, spriteManager, road);
		waveManager.update(lastFrameDeltaTimeMS, effectManager, player, audioPlayer);
		towerManager.update(lastFrameDeltaTimeMS, waveManager.getEnemies(), projectileManager, audioPlayer);
		requestingMainMenu = sideBar.isRequestingMainMenu();
		sideBar.update(road, spriteManager, towerManager, audioPlayer, resRet, lastFrameDeltaTimeMS);
		infiniteWaveManager.updateWaveManager(audioPlayer, waveManager, spriteManager.getSprite(resRet.getBmpEnemy(1)).getFrameSize().x);

		if (PropertyReader.hasClock()) {
			gameClock.update(lastFrameDeltaTimeMS, spriteManager, audioPlayer);
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
		towerManager.draw(viewer, displayList, clientRect, spriteManager, audioPlayer, input);
		projectileManager.draw(viewer, displayList, clientRect);
		displayList.draw(viewer, spriteManager);
		effectManager.draw(viewer, spriteManager, clientRect);
		sideBar.draw(spriteManager, road, viking16, audioPlayer, resRet);
		outputData.draw(new Vector2(0, 32), viking16, spriteManager);
		speedController.putButtons(spriteManager, audioPlayer, input);
		putNextWaveButton();
		if (PropertyReader.hasClock()) {
			gameClock.drawHelper(spriteManager);
		}
		super.draw();
		graphicDevice.endScene();

	}

	private void putNextWaveButton() {
		putNextWaveButton(Sprite.zero);
	}

	private void putNextWaveButton(Vector2 pos) {
		forceNextWave.setPos(pos);
		forceNextWave.putButton(graphicDevice, audioPlayer, spriteManager, input, waveManager.verifyForceNextWave());
		if (forceNextWave.getStatus() == TouchButton.STATUS.ACTIVATED) {
			forceNextWave.setStatus(TouchButton.STATUS.IDLE);
			if (waveManager.verifyForceNextWave()) {
				waveManager.forceNextWave();
			}
		}
		graphicDevice.setAlphaMode(ALPHA_MODE.DEFAULT);
	}

	private void listOutputData() {
		if (PropertyReader.isLimitTowers()) {
			outputData.add(towerManager);
		}

		outputData.add(player);

		outputData.add(infiniteWaveManager);

		if (PropertyReader.hasClock()) {
			outputData.add(gameClock);
		}

		if (PropertyReader.isShowNextWaveTime()) {
			outputData.add(waveManager);
		}
	}

	private void resetScrollBounds(DisplayableEntity scene) {
		viewer.setScrollBounds(scene.getMin(spriteManager), scene.getMax(spriteManager),
				graphicDevice.getScreenSize().sub(new Vector2(sideBar.getSideBarWidth(), 0)));
		if (!PropertyReader.isCustomViewStart()) {
			viewer.scrollTo(new Vector2(((scene.getMin(spriteManager).x + scene.getMax(spriteManager).x / 2.0f)) - (graphicDevice.getScreenSize().x / 2.0f),
					scene.getMax(spriteManager).y));
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

	public GAME_STATUS callGameOver() {
		if (waveManager.hasReachedLastNode()) {
			if (super.doFadeOut() == FadeEffect.FADE_STATE.FADED_OUT) {
				return GAME_STATUS.LOST;
			} else {
				return GAME_STATUS.RUNNING;
			}
		}
		return GAME_STATUS.RUNNING;
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
	private TowerManager towerManager;
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
	private SpeedController speedController;

	private final int numTowers;
	public static int tilemapSizeX;
	public static int tilemapSizeY;
	public static Vector2 tileSize;
	public static int[] mainLayer;
	public static int[] pathLayer;
}

package br.com.jera.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.app.Activity;
import br.com.jera.util.CommonMath.Vector2;

public class PropertyReader {

	public PropertyReader(Activity activity) {
		try {
			InputStream inputStream = activity.getAssets().open("td.properties");
			Properties properties = new Properties();
			properties.load(inputStream);

			enemyStartOffset.x = getFloat("enemyStartOffsetX", properties, enemyStartOffset.x);
			enemyStartOffset.y = getFloat("enemyStartOffsetY", properties, enemyStartOffset.y);
			drawTowerShadow = getBoolean("drawTowerShadow", properties, drawTowerShadow);
			customViewStart = getBoolean("customViewStart", properties, customViewStart);
			hasHelp = getBoolean("hasHelp", properties, hasHelp);
			hasMenuSong = getBoolean("hasMenuSong", properties, hasMenuSong);
			viewStart.x = getFloat("viewStartX", properties, viewStart.x);
			viewStart.y = getFloat("viewStartY", properties, viewStart.y);
			smartPlaceTolerance = getFloat("smartPlaceTolerance", properties, smartPlaceTolerance);
			defaultFontSize = getInt("defaultFontSize", properties, defaultFontSize);
			sideBarWidth = getFloat("sideBarWidth", properties, sideBarWidth);
			towerRangeScale = getFloat("towerRangeScale", properties, towerRangeScale);
			defaultEnemySize.x = getFloat("defaultEnemySizeX", properties, defaultEnemySize.x);
			defaultEnemySize.y = getFloat("defaultEnemySizeY", properties, defaultEnemySize.y);
			hpBarHeight = getFloat("hpBarHeight", properties, hpBarHeight);
			hideBehaviourController = getBoolean("hideBehaviourController", properties, hideBehaviourController);
			hideBottomBar = getBoolean("hideBottomBar", properties, hideBottomBar);
			hasClock = getBoolean("hasClock", properties, hasClock);
			numLevels = getInt("numLevels", properties, numLevels);
			levelTime = getLong("levelTime", properties, levelTime);
			showNextWaveTime = getBoolean("showNextWaveTime", properties, showNextWaveTime);
			startMoney = getInt("startMoney", properties, startMoney);
			hasSnapshotFX = getBoolean("hasSnapshotFX", properties, hasSnapshotFX);
			useDragDropSFX = getBoolean("useDragDropSFX", properties, useDragDropSFX);
			limitTowers = getBoolean("limitTowers", properties, limitTowers);
			axeHitEffectRadius = getFloat("axeHitEffectRadius", properties, axeHitEffectRadius);
			spearHitEffectRadius = getFloat("spearHitEffectRadius", properties, spearHitEffectRadius);
			rotateHitEffects = getBoolean("rotateHitEffects", properties, rotateHitEffects);
			hitEffectDuration = getLong("hitEffectDuration", properties, hitEffectDuration);
			hitEffectHeightOffset = getFloat("hitEffectHeightOffset", properties, hitEffectHeightOffset);
			alphaAddHitEffects = getBoolean("alphaAddHitEffects", properties, alphaAddHitEffects);
			hasShowUpSfx = getBoolean("hasShowUpSfx", properties, hasShowUpSfx);
			projectileSpeedFactor = getFloat("projectileSpeedFactor", properties, projectileSpeedFactor);
			horizontalSceneWrapSprite = getBoolean("horizontalSceneWrapSprite", properties, horizontalSceneWrapSprite);
		} catch (IOException e) {
			// no problem...
		}
	}

	private float getFloat(String name, Properties properties, float defaultValue) {
		String value = properties.getProperty(name);
		if (value != null) {
			return Float.parseFloat(value);
		} else {
			return defaultValue;
		}
	}

	private int getInt(String name, Properties properties, int defaultValue) {
		String value = properties.getProperty(name);
		if (value != null) {
			return Integer.parseInt(value);
		} else {
			return defaultValue;
		}
	}

	private long getLong(String name, Properties properties, long defaultValue) {
		String value = properties.getProperty(name);
		if (value != null) {
			return Long.parseLong(value);
		} else {
			return defaultValue;
		}
	}

	private boolean getBoolean(String name, Properties properties, boolean defaultValue) {
		String value = properties.getProperty(name);
		if (value != null) {
			return value.equals("0") ? false : true;
		} else {
			return defaultValue;
		}
	}

	public static Vector2 getEnemyStartOffset() {
		return new Vector2(enemyStartOffset);
	}

	public static boolean isDrawTowerShadow() {
		return drawTowerShadow;
	}

	public static boolean isCustomViewStart() {
		return customViewStart;
	}

	public static Vector2 getViewStart() {
		return new Vector2(viewStart);
	}

	public static float getSmartPlaceTolerance() {
		return smartPlaceTolerance;
	}

	public static int getDefaultFontSize() {
		return defaultFontSize;
	}

	public static float getSideBarWidth() {
		return sideBarWidth;
	}

	public static float getTowerRangeScale() {
		return towerRangeScale;
	}

	public static float getHpBarHeight() {
		return hpBarHeight;
	}

	public static boolean hasMenuSong() {
		return hasMenuSong;
	}

	public static boolean hasHelp() {
		return hasHelp;
	}

	public static Vector2 getDefaultEnemySize() {
		return new Vector2(defaultEnemySize);
	}

	public static boolean isHideBehaviourController() {
		return hideBehaviourController;
	}

	public static boolean isHideBottomBar() {
		return hideBottomBar;
	}

	public static boolean hasClock() {
		return hasClock;
	}

	public static long getLevelTime() {
		return levelTime;
	}

	public static long getHitEffectDuration() {
		return hitEffectDuration;
	}

	public static boolean isShowNextWaveTime() {
		return showNextWaveTime;
	}

	public static int getStartMoney() {
		return startMoney;
	}

	public static boolean hasSnapshotFX() {
		return hasSnapshotFX;
	}

	public static boolean isUseDragDropSFX() {
		return useDragDropSFX;
	}

	public static boolean isLimitTowers() {
		return limitTowers;
	}

	public static float getAxeHitEffectRadius() {
		return axeHitEffectRadius;
	}

	public static float getSpearHitEffectRadius() {
		return spearHitEffectRadius;
	}

	public static boolean isRotateHitEffects() {
		return rotateHitEffects;
	}

	public static float getHitEffectHeightOffset() {
		return hitEffectHeightOffset;
	}

	public static boolean isAlphaAddHitEffects() {
		return alphaAddHitEffects;
	}

	public static boolean hasShowUpSfx() {
		return hasShowUpSfx;
	}

	public static float getProjectileSpeedFactor() {
		return projectileSpeedFactor;
	}

	public static int getNumLevels() {
		return numLevels;
	}
	
	public static boolean getHorizontalSceneWrapSprite() {
		return horizontalSceneWrapSprite;
	}

	private static Vector2 enemyStartOffset = new Vector2(0, 1);
	private static boolean drawTowerShadow = true;
	private static boolean customViewStart = false;
	private static boolean hideBehaviourController = false;
	private static boolean hasHelp = false;
	private static boolean hasMenuSong = false;
	private static boolean hideBottomBar = false;
	private static Vector2 viewStart = new Vector2(0, 0);
	private static float smartPlaceTolerance = 128.0f;
	private static int defaultFontSize = 16;
	private static float sideBarWidth = 64.0f;
	private static float towerRangeScale = 1.0f;
	private static float hpBarHeight = 4.0f;
	private static Vector2 defaultEnemySize = new Vector2(32, 48);
	private static boolean hasClock = false;
	private static int numLevels = 0;
	private static long levelTime = 0;
	private static long hitEffectDuration = 600;
	private static boolean showNextWaveTime = true;
	private static int startMoney = 50;
	private static boolean hasSnapshotFX = false;
	private static boolean useDragDropSFX = false;
	private static boolean limitTowers = true;
	private static float axeHitEffectRadius = 52.0f;
	private static float spearHitEffectRadius = 46.0f;
	private static boolean rotateHitEffects = true;
	private static float hitEffectHeightOffset = -24.0f;
	private static boolean alphaAddHitEffects = false;
	private static boolean hasShowUpSfx = false;
	private static float projectileSpeedFactor = 1.0f;
	private static boolean horizontalSceneWrapSprite = false;
}

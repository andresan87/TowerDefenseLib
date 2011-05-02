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
}

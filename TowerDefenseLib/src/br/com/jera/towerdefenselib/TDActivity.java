package br.com.jera.towerdefenselib;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.Gravity;
import android.widget.Toast;
import br.com.jera.game.StateManager;
import br.com.jera.platform.android.AndroidSurfaceView;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.util.CommonMath.Vector2;

public class TDActivity extends Activity {

	public String getVersion() {
		String versionStr = "";
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			info = null;
		}
		if (info != null) {
			versionStr = info.versionName + "." + info.versionCode;
		}
		return versionStr;
	}

	public static void toast(int id, Context context) {
		Toast toast = Toast.makeText(context, id, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2, toast.getYOffset() / 2);
		toast.show();
	}
	
	public void startGame(ResourceIdRetriever resRet, int tilemapSizeX, int tilemapSizeY, Vector2 tileSize, int[] mainLayer, int[] pathLayer)
	{
		if (manager == null) {
			manager = new StateManager(getVersion(), resRet, tilemapSizeX, tilemapSizeY, tileSize, mainLayer, pathLayer);
		}

		mGLSurfaceView = new AndroidSurfaceView(this, manager);
		setContentView(mGLSurfaceView);

	}

	@Override
	protected void onResume() {
		super.onResume();
		mGLSurfaceView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mGLSurfaceView.onPause();
	}
	
	private AndroidSurfaceView mGLSurfaceView;
	private static StateManager manager;
}

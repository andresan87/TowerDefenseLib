package br.com.jera.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.net.ParseException;
import android.os.AsyncTask;
import br.com.jera.androidutil.Preferences;

public class Highscore {

	public static String get(Activity activity, int mapId) {
		return Preferences.readString(activity, "highscore_map" + mapId);
	}

	public static void set(Activity activity, String mapId, String score) {
		Preferences.write(activity, "highscore_map" + mapId, score);
	}

	public static void populate(Activity activity) {
		AsyncTask<Activity, Void, Void> task = new AsyncTask<Activity, Void, Void>() {
			@Override
			protected Void doInBackground(Activity... activities) {
				try {
					HttpGet get = new HttpGet(PropertyReader.getScoreUrl() + "highscore.php");
					HttpResponse response = connectToServer().execute(get);
					String data = getResponseBody(response.getEntity());
					String[] scores = data.split(";");
					for (String score : scores) {
						String[] highscore = score.split("=");
						set(activities[0], highscore[0], highscore[1]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		task.execute(activity);
	}

	public static String getResponseBody(final HttpEntity entity) throws IOException, ParseException {
		InputStream instream = entity.getContent();
		String charset = getContentCharSet(entity);
		if (charset == null) {
			charset = HTTP.UTF_8;
		}
		Reader reader = new InputStreamReader(instream, charset);
		StringBuilder buffer = new StringBuilder();
		try {
			char[] tmp = new char[1024];
			int i;
			while ((i = reader.read(tmp)) != -1) {
				buffer.append(tmp, 0, i);
			}
		} finally {
			reader.close();
		}
		return buffer.toString();
	}

	public static String getContentCharSet(final HttpEntity entity) {
		String charset = null;
		if (entity.getContentType() != null) {
			HeaderElement values[] = entity.getContentType().getElements();
			if (values.length > 0) {
				NameValuePair param = values[0].getParameterByName("charset");
				if (param != null) {
					charset = param.getValue();
				}
			}
		}
		return charset;
	}

	public static HttpClient connectToServer() {
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		int timeoutSocket = 3000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		return new DefaultHttpClient(httpParameters);
	}
}

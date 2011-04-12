package br.com.jera.towerdefenselib;

import java.util.LinkedList;
import java.util.ListIterator;

import br.com.jera.graphic.GraphicDevice.ALPHA_MODE;
import br.com.jera.util.BitmapFont;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.SpriteResourceManager;

public class OutputData {

	public static interface Data {
		public Vector2 drawData(Vector2 pos, SpriteResourceManager res, BitmapFont font);
	}

	public void add(Data data) {
		dataList.add(data);
	}

	public void draw(Vector2 pos, BitmapFont font, SpriteResourceManager res) {
		res.getGraphicDevice().setAlphaMode(ALPHA_MODE.DEFAULT);
		ListIterator<Data> iter = dataList.listIterator();
		cursor = new Vector2(pos);
		while (iter.hasNext()) {
			cursor = cursor.add(iter.next().drawData(cursor, res, font));
		}
		dataList.clear();
	}
	
	public Vector2 getCursor() {
		return (cursor == null) ? new Vector2(0, 0) : cursor;
	}

	private Vector2 cursor = new Vector2();
	private LinkedList<Data> dataList = new LinkedList<Data>();
}

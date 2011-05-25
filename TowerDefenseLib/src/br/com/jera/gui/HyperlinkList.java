package br.com.jera.gui;

import java.util.ArrayList;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.graphic.Sprite;
import br.com.jera.input.InputListener;
import br.com.jera.resources.PropertyReader;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.SpriteResourceManager;

public class HyperlinkList {

	public HyperlinkList(SpriteResourceManager res, ResourceIdRetriever resRet, Vector2 origin) {
		linkList.add(new Hyperlink(Sprite.zero, origin, resRet.getBmpTwitterButton(), PropertyReader.getTwitterUrl()));
		linkList.add(new Hyperlink(Sprite.zero, origin, resRet.getBmpFacebookButton(), PropertyReader.getFacebookUrl()));
	}

	public void putButtons(SpriteResourceManager res, AudioPlayer player, InputListener input, Vector2 pos, boolean toTheRight) {
		Vector2 cursor = pos;
		for (int t = 0; t < linkList.size(); t++) {
			Hyperlink hl = linkList.get(t);
			
			Vector2 add;
			if (toTheRight)
				add = hl.getSize(res).multiply(new Vector2(1.0f, 0.0f));
			else
				add = hl.getSize(res).multiply(new Vector2(-1.0f, 0.0f));
			hl.setPos(cursor);
			hl.putButton(res.getGraphicDevice(), player, res, input);
			cursor = cursor.add(add);
		}
	}

	private ArrayList<Hyperlink> linkList = new ArrayList<Hyperlink>();
}

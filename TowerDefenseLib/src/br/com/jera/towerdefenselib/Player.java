package br.com.jera.towerdefenselib;

import br.com.jera.graphic.Sprite;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.util.BitmapFont;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.SpriteResourceManager;

public class Player implements OutputData.Data {
	
	private ResourceIdRetriever resRet;
	
	public Player(ResourceIdRetriever resRet) {
		this.resRet = resRet;
	}

	public boolean pay(final int price) {
		if (price <= money) {
			money -= price;
			return true;
		} else {
			return false;
		}
	}

	public int getMoney() {
		return money;
	}

	private int money = 50;

	public Vector2 drawData(Vector2 pos, SpriteResourceManager res, BitmapFont font) {
		Sprite sprite = res.getSprite(resRet.getBmpMoneySymbol());
		sprite.draw(pos, Sprite.defaultOrigin);
		final Vector2 frameSize = sprite.getFrameSize();
		font.draw(new Vector2(frameSize.x, 0).add(pos), new Integer(getMoney()).toString());
		return new Vector2(0, frameSize.y);
	}

	public void addMoney(final int m) {
		money += m;
		
	}

}

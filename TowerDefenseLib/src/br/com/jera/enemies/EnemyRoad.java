package br.com.jera.enemies;

import java.util.ArrayList;

import br.com.jera.graphic.GraphicDevice.ALPHA_MODE;
import br.com.jera.graphic.Sprite;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.SceneViewer;
import br.com.jera.util.SpriteResourceManager;
import br.com.jera.util.SpriteTileMap;
import br.com.jera.util.SpriteTileMap.Tile;
import br.com.jera.util.SpriteTileMap.TileMap;

public class EnemyRoad {

	private SpriteTileMap tileMap;
	private TileMap pathMap;
	
	// TODO carregar multiplos níveis de arquivo resource, não de array no código
	public EnemyRoad(ResourceIdRetriever resRet) {
		final int tilemapSizeX = 8;
		final int tilemapSizeY = 10;
		final Vector2 tileSize = new Vector2(64, 64).multiply(Sprite.getGlobalSpriteScale());

		final int[] mainLayer = new int[] {
				-1, -1, -1, -1,  4, -1, -1, -1, 
				-1,  2,  6,  6,  1, -1, -1, -1, 
				 2,  1, -1, -1, -1, -1, -1, -1, 
				 0,  6,  6,  6,  6,  3, -1, -1, 
				-1, -1, -1, -1, -1,  7, -1, -1, 
				-1, -1, -1,  2,  6,  1, -1, -1, 
				-1, -1, -1,  7, -1, -1, -1, -1, 
				-1, -1, -1,  7, -1, -1, -1, -1, 
				-1, -1, -1,  7, -1, -1, -1, -1, 
				-1, -1, -1,  7, -1, -1, -1, -1, 
		};

		tileMap = new SpriteTileMap(resRet.getBmpTiles(), new TileMap(tilemapSizeX, tilemapSizeY, mainLayer, tileSize));

		final int[] pathLayer = new int[] {
				-1, -1, -1, -1, 20, -1, -1, -1, 
				-1, 16, 17, 18, 19, -1, -1, -1, 
				14, 15, -1, -1, -1, -1, -1, -1, 
				13, 12, 11, 10,  9,  8, -1, -1, 
				-1, -1, -1, -1, -1,  7, -1, -1, 
				-1, -1, -1,  4,  5,  6, -1, -1, 
				-1, -1, -1,  3, -1, -1, -1, -1, 
				-1, -1, -1,  2, -1, -1, -1, -1, 
				-1, -1, -1,  1, -1, -1, -1, -1, 
				-1, -1, -1,  0, -1, -1, -1, -1, 
		};

		pathMap = new TileMap(tilemapSizeX, tilemapSizeY, pathLayer, tileSize);
	}
	
	public boolean  isPointOnRoad(Vector2 p) {
		return tileMap.isPointOnRoad(p);
	}

	public void draw(SpriteResourceManager res, SceneViewer viewer) {
		res.getGraphicDevice().setAlphaMode(ALPHA_MODE.DEFAULT);
		tileMap.draw(viewer, res);
	}
	
	public ArrayList<Tile> getRoadPath() {
		return pathMap.tiles;
	}
}

package br.com.jera.enemies;

import java.util.ArrayList;

import br.com.jera.graphic.GraphicDevice.ALPHA_MODE;
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
	public EnemyRoad(ResourceIdRetriever resRet, int tilemapSizeX, int tilemapSizeY, Vector2 tileSize, int[] mainLayer, int[] pathLayer) {
		tileMap = new SpriteTileMap(resRet.getBmpTiles(), new TileMap(tilemapSizeX, tilemapSizeY, mainLayer, tileSize));
		pathMap = new TileMap(tilemapSizeX, tilemapSizeY, pathLayer, tileSize);
	}
	
	public Vector2 getTilePos(int t) {
		return pathMap.getTilePos(t);
	}

	public boolean isPointOnRoad(Vector2 p) {
		return (pathMap.isPointOnRoad(p) || tileMap.isPointOnRoad(p));
	}

	public void draw(SpriteResourceManager res, SceneViewer viewer) {
		res.getGraphicDevice().setAlphaMode(ALPHA_MODE.DEFAULT);
		tileMap.draw(viewer, res);
	}
	
	public ArrayList<Tile> getRoadPath() {
		return pathMap.tiles;
	}
}

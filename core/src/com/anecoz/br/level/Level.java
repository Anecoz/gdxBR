package com.anecoz.br.level;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

// Abstraction for the level, i.e. tiles etc
public class Level {

    // How many tiles in x/y-direction
    public static int MAP_WIDTH;
    public static int MAP_HEIGHT;

    private final static String COLLISION_PROPERTY = "isCollision";

    private OrthogonalTiledMapRenderer _mapRenderer;
    private TiledMapTileLayer _tileLayer;

    public Level(String mapName, SpriteBatch batch) {
        TiledMap map = new TmxMapLoader().load(mapName);
        _tileLayer = (TiledMapTileLayer)map.getLayers().get(0);
        MAP_WIDTH = _tileLayer.getWidth();
        MAP_HEIGHT = _tileLayer.getHeight();

        _mapRenderer = new OrthogonalTiledMapRenderer(map, 1/64f, batch);
    }

    public void render(OrthographicCamera camera) {
        _mapRenderer.setView(camera);
        _mapRenderer.renderTileLayer(_tileLayer);
    }

    private boolean isProperty(String prop, int x, int y) {
        if (_tileLayer.getCell(x, y) == null)
            return false;

        return _tileLayer.getCell(x, y)
                .getTile()
                .getProperties()
                .get(prop, String.class) != null;
    }

    public boolean isTileBuildableAt(int x, int y) {
        return isProperty(COLLISION_PROPERTY, x, y);
    }
}


package com.anecoz.br.level;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.*;

// Abstraction for the level, i.e. tiles etc
public class Level {

    // How many tiles in x/y-direction
    public static int MAP_WIDTH;
    public static int MAP_HEIGHT;
    public static float PIX_TO_WORLD_FACTOR;

    private final static String COLLISION_PROPERTY = "isCollision";
    private final static String SHADOWCASTER_PROPERTY = "isShadowCaster";

    private OrthogonalTiledMapRenderer _mapRenderer;
    private TiledMapTileLayer _tileLayer;

    public Level(String mapName, SpriteBatch batch) {
        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.textureMinFilter = Texture.TextureFilter.Nearest;
        params.textureMagFilter = Texture.TextureFilter.Nearest;

        TiledMap map = new TmxMapLoader().load(mapName, params);
        _tileLayer = (TiledMapTileLayer)map.getLayers().get(0);

        MAP_WIDTH = _tileLayer.getWidth();
        MAP_HEIGHT = _tileLayer.getHeight();
        PIX_TO_WORLD_FACTOR = 1.0f/_tileLayer.getTileHeight();

        _mapRenderer = new OrthogonalTiledMapRenderer(map, PIX_TO_WORLD_FACTOR, batch);
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

    public boolean isTileCollidableAt(int x, int y) {
        return isProperty(COLLISION_PROPERTY, x, y);
    }
    public boolean isTileShadowCasterAt(int x, int y) {return isProperty(SHADOWCASTER_PROPERTY, x, y);}

    public static Rectangle getBounds() {
        return new Rectangle(0, 0, MAP_WIDTH, MAP_HEIGHT);
    }
}


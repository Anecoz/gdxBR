package com.anecoz.br.systems;

import com.anecoz.br.components.TiledMapComponent;
import com.anecoz.br.components.TiledMapRenderComponent;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class TiledMapRenderSystem extends EntitySystem {
    private OrthographicCamera _cam;
    private SpriteBatch _sb;

    private ImmutableArray<Entity> _entities;

    private ComponentMapper<TiledMapComponent> tc = ComponentMapper.getFor(TiledMapComponent.class);

    public TiledMapRenderSystem(SpriteBatch sb, OrthographicCamera cam) {
        priority = -1; // do this before any other rendering...
        _sb = sb;
        _cam = cam;
    }

    @Override
    public void addedToEngine(Engine engine) {
        _entities = engine.getEntitiesFor(Family.all(TiledMapRenderComponent.class, TiledMapComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        TiledMapComponent mapComponent;

        _cam.update();
        _sb.begin();
        _sb.setProjectionMatrix(_cam.combined);

        for (int i = 0; i < _entities.size(); i++) {
            Entity e = _entities.get(i);

            mapComponent = tc.get(e);
            TiledMapTileLayer tileLayer = (TiledMapTileLayer)mapComponent._map.getLayers().get(0);
            float factor = 1.0f/tileLayer.getTileHeight();
            OrthogonalTiledMapRenderer mapRenderer = new OrthogonalTiledMapRenderer(mapComponent._map, factor, _sb);

            mapRenderer.setView(_cam);
            mapRenderer.renderTileLayer(tileLayer);
        }
        _sb.end();
    }
}

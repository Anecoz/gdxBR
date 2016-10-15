package com.anecoz.br;

import com.anecoz.br.components.*;
import com.anecoz.br.systems.*;
import com.anecoz.br.utils.ResourceHandler;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;

public class EntityManager {
    private Engine _engine;
    private SpriteBatch _sb;
    private OrthographicCamera _cam;

    private float _pixToWorldFactor;

    public EntityManager(Engine engine, SpriteBatch sb, OrthographicCamera cam) {
        _engine = engine;
        _sb = sb;
        _cam = cam;
        init();
    }

    private void init() {
        initLevelEntity();
        initPlayerEntity();
        initSystems();
    }

    private void initLevelEntity() {
        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.textureMinFilter = Texture.TextureFilter.Nearest;
        params.textureMagFilter = Texture.TextureFilter.Nearest;
        TiledMap map = new TmxMapLoader().load("maps/map_01.tmx", params);

        Entity level = new Entity();
        level.add(new TiledMapRenderComponent())
                .add(new TiledMapComponent(map));

        _engine.addEntity(level);

        TiledMapTileLayer tileLayer = (TiledMapTileLayer)map.getLayers().get(0);

        // If we ever need this, which we might
        //MAP_WIDTH = tileLayer.getWidth();
        //MAP_HEIGHT = tileLayer.getHeight();
        _pixToWorldFactor = 1.0f/tileLayer.getTileHeight();
    }

    private void initPlayerEntity() {
        Entity player = new Entity();
        player.add(new PositionComponent(new Vector2(2, 2)))
                .add(new VelocityComponent(new Vector2(0, 0)))
                .add(new CameraTargetComponent())
                .add(new TextureComponent(ResourceHandler.PLAYER_TEXTURE))
                .add(new RenderComponent(0f, .45f))
                .add(new PlayerInputComponent());
        _engine.addEntity(player);
    }

    private void initSystems() {
        EntityRenderSystem renderSystem = new EntityRenderSystem(_sb, _cam, _pixToWorldFactor);
        TiledMapRenderSystem tiledRenderSystem = new TiledMapRenderSystem(_sb, _cam);
        InputSystem inputSystem = new InputSystem(_cam, _pixToWorldFactor);
        CameraSystem cameraSystem = new CameraSystem(_cam);
        MovementSystem movementSystem = new MovementSystem();

        _engine.addSystem(renderSystem);
        _engine.addSystem(tiledRenderSystem);
        _engine.addSystem(inputSystem);
        _engine.addSystem(cameraSystem);
        _engine.addSystem(movementSystem);
    }
}

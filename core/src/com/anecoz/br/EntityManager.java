package com.anecoz.br;

import com.anecoz.br.blueprints.BulletProjectileBlueprint;
import com.anecoz.br.components.*;
import com.anecoz.br.components.weapon.ReloadTimerComponent;
import com.anecoz.br.components.weapon.RpmTimerComponent;
import com.anecoz.br.components.weapon.ShootingComponent;
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
    public static float PIX_TO_WORLD_FACTOR;
    public static float MAP_WIDTH;
    public static float MAP_HEIGHT;

    private Engine _engine;
    private SpriteBatch _sb;
    private static OrthographicCamera _cam;
    private TiledMapTileLayer _tileLayer;

    public EntityManager(Engine engine, SpriteBatch sb, OrthographicCamera cam) {
        _engine = engine;
        _sb = sb;
        _cam = cam;
        init();
    }

    public static OrthographicCamera getWorldCam() {
        return _cam;
    }

    private void init() {
        initLevelEntity();
        initPlayerEntity();
        initDebugWeaponEntity();
        initInventoryEntity();
        initSystems();
    }

    private void initDebugWeaponEntity() {
        Entity weapon1 = new Entity();
        Entity weapon2 = new Entity();
        Entity weapon3 = new Entity();
        Entity weapon4 = new Entity();
        Entity weapon5 = new Entity();

        weapon1.add(new ShootingComponent(80, 40, 40, true))
                .add(new PickedUpComponent(true, 0))
                .add(new RpmTimerComponent(500))
                .add(new ReloadTimerComponent(60))
                .add(new ProjectileFactoryComponent(new BulletProjectileBlueprint()))
                .add(new PositionComponent(new Vector2(0,0)))
                .add(new TextureComponent(ResourceHandler.RIFLE_TEXTURE))
                .add(new RenderComponent(0f, 0.03125f, 2))
                .add(new TextComponent("", new Vector2(10, 30)))
                .add(new VisibilityComponent())
                .add(new BoundingBoxComponent(new Vector2(0,0)));
        weapon2.add(new ShootingComponent(80, 40, 40, true))
                .add(new PickedUpComponent(false, 1))
                .add(new RpmTimerComponent(300))
                .add(new ReloadTimerComponent(60))
                .add(new ProjectileFactoryComponent(new BulletProjectileBlueprint()))
                .add(new PositionComponent(new Vector2(0,0)))
                .add(new TextureComponent(ResourceHandler.RIFLE_TEXTURE))
                .add(new RenderComponent(0f, 0.03125f, 2))
                .add(new TextComponent("", new Vector2(10, 30)))
                .add(new VisibilityComponent())
                .add(new BoundingBoxComponent(new Vector2(0,0)));
        weapon3.add(new ShootingComponent(80, 40, 40, true))
                .add(new PickedUpComponent(false, 2))
                .add(new RpmTimerComponent(300))
                .add(new ReloadTimerComponent(60))
                .add(new ProjectileFactoryComponent(new BulletProjectileBlueprint()))
                .add(new PositionComponent(new Vector2(0,0)))
                .add(new TextureComponent(ResourceHandler.RIFLE_TEXTURE))
                .add(new RenderComponent(0f, 0.03125f, 2))
                .add(new TextComponent("", new Vector2(10, 30)))
                .add(new VisibilityComponent())
                .add(new BoundingBoxComponent(new Vector2(0,0)));
        weapon4.add(new ShootingComponent(80, 40, 40, true))
                .add(new PickedUpComponent(false, 3))
                .add(new RpmTimerComponent(300))
                .add(new ReloadTimerComponent(60))
                .add(new ProjectileFactoryComponent(new BulletProjectileBlueprint()))
                .add(new PositionComponent(new Vector2(0,0)))
                .add(new TextureComponent(ResourceHandler.RIFLE_TEXTURE))
                .add(new RenderComponent(0f, 0.03125f, 2))
                .add(new TextComponent("", new Vector2(10, 30)))
                .add(new VisibilityComponent())
                .add(new BoundingBoxComponent(new Vector2(0,0)));
        weapon5.add(new ShootingComponent(80, 40, 40, true))
                .add(new PickedUpComponent(false, 4))
                .add(new RpmTimerComponent(300))
                .add(new ReloadTimerComponent(60))
                .add(new ProjectileFactoryComponent(new BulletProjectileBlueprint()))
                .add(new PositionComponent(new Vector2(0,0)))
                .add(new TextureComponent(ResourceHandler.RIFLE_TEXTURE))
                .add(new RenderComponent(0f, 0.03125f, 2))
                .add(new TextComponent("", new Vector2(10, 30)))
                .add(new VisibilityComponent())
                .add(new BoundingBoxComponent(new Vector2(0,0)));

        _engine.addEntity(weapon1);
        _engine.addEntity(weapon2);
        _engine.addEntity(weapon3);
        _engine.addEntity(weapon4);
        _engine.addEntity(weapon5);
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

        _tileLayer = (TiledMapTileLayer)map.getLayers().get(0);

        MAP_WIDTH = _tileLayer.getWidth();
        MAP_HEIGHT = _tileLayer.getHeight();
        PIX_TO_WORLD_FACTOR = 1.0f/_tileLayer.getTileHeight();
    }

    private void initPlayerEntity() {
        Vector2 pos = new Vector2(2, 2);
        Entity player = new Entity();
        player.add(new PositionComponent(pos))
                .add(new VelocityComponent(new Vector2(0, 0)))
                .add(new CameraTargetComponent())
                .add(new TextureComponent(ResourceHandler.PLAYER_TEXTURE))
                .add(new RenderComponent(0f, .45f))
                .add(new PlayerInputComponent())
                .add(new PlayerComponent())
                .add(new VisibilityComponent())
                .add(new HealthComponent(100f));
        _engine.addEntity(player);
    }

    private void initInventoryEntity() {
        Entity inventory = new Entity();
        inventory.add(new InventoryComponent())
                .add(new TextureComponent(ResourceHandler.INVENTORY_TEXTURE))
                .add(new PositionComponent(new Vector2(10, 10)))
                .add(new RenderComponent(0f, 0.3f, 1))
                .add(new VisibilityComponent());
        _engine.addEntity(inventory);
    }

    private void initSystems() {
        EntityRenderSystem renderSystem = new EntityRenderSystem(_sb, _cam);
        TiledMapRenderSystem tiledRenderSystem = new TiledMapRenderSystem(_sb, _cam);
        InputSystem inputSystem = new InputSystem(_cam);
        CameraSystem cameraSystem = new CameraSystem(_cam);
        CollisionSystem collisionSystem = new CollisionSystem(_tileLayer);
        WeaponSystem weaponSystem = new WeaponSystem();
        TextRenderSystem textSystem = new TextRenderSystem(_sb, _cam);
        InventorySystem inventorySystem = new InventorySystem(_cam);
        NetworkSystem networkSystem = new NetworkSystem();
        ProjectileCollisionSystem projCollSystem = new ProjectileCollisionSystem();
        HealthSystem healthSystem = new HealthSystem();

        _engine.addSystem(renderSystem);
        _engine.addSystem(tiledRenderSystem);
        _engine.addSystem(inputSystem);
        _engine.addSystem(cameraSystem);
        _engine.addSystem(collisionSystem);
        _engine.addSystem(weaponSystem);
        _engine.addSystem(textSystem);
        _engine.addSystem(inventorySystem);
        _engine.addSystem(networkSystem);
        _engine.addSystem(projCollSystem);
        _engine.addSystem(healthSystem);
    }
}

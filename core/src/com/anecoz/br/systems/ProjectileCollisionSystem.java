package com.anecoz.br.systems;

import com.anecoz.br.components.*;
import com.anecoz.br.components.network.NetworkPlayerComponent;
import com.anecoz.br.components.weapon.ProjectileComponent;
import com.anecoz.br.utils.CollisionUtils;
import com.anecoz.br.utils.RenderUtils;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class ProjectileCollisionSystem extends EntitySystem {

    private Engine _engine;

    private ImmutableArray<Entity> _projectileEntities;
    private ImmutableArray<Entity> _networkPlayerEntities;
    private ImmutableArray<Entity> _playerEntities;

    private ComponentMapper<PositionComponent> posMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<TextureComponent> textureMapper = ComponentMapper.getFor(TextureComponent.class);
    private ComponentMapper<RenderComponent> renderMapper = ComponentMapper.getFor(RenderComponent.class);
    private ComponentMapper<ProjectileComponent> projMapper = ComponentMapper.getFor(ProjectileComponent.class);
    private ComponentMapper<HealthComponent> healthMapper = ComponentMapper.getFor(HealthComponent.class);

    public ProjectileCollisionSystem() {

    }

    @Override
    public void addedToEngine(Engine engine) {
        _engine = engine;
        _projectileEntities = engine.getEntitiesFor(Family.all(
                VisibilityComponent.class,
                TextureComponent.class,
                ProjectileComponent.class,
                PositionComponent.class).get());
        _networkPlayerEntities = engine.getEntitiesFor(Family.all(RenderComponent.class, NetworkPlayerComponent.class, TextureComponent.class, PositionComponent.class).get());
        _playerEntities = engine.getEntitiesFor(Family.all(HealthComponent.class, RenderComponent.class, TextureComponent.class, PlayerComponent.class, PositionComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        PositionComponent projPos, playerPos, netPlayerPos;
        TextureComponent projTex, playerTex, netPlayerTex;
        RenderComponent projRen, playerRen, netPlayerRen;
        ProjectileComponent projComp;
        HealthComponent playerHealth = new HealthComponent(-1); // Dummy default

        Rectangle playerRectangle = new Rectangle(); // Dummy default
        if (_playerEntities.size() > 0) {
            playerPos = posMapper.get(_playerEntities.first());
            playerTex = textureMapper.get(_playerEntities.first());
            playerRen = renderMapper.get(_playerEntities.first());
            playerHealth = healthMapper.get(_playerEntities.first());
            Vector2 playerWorldDims = RenderUtils.getWorldDims(playerRen, playerTex);
            // Get a somewhat smaller bounding box
            float x = playerPos._pos.x + playerWorldDims.x/4.0f;
            float y = playerPos._pos.y + playerWorldDims.y/4.0f;
            float w = playerWorldDims.x/2.0f;
            float h = playerWorldDims.y/2.0f;

            playerRectangle = new Rectangle(x, y, w, h);
        }

        Rectangle projRectangle = new Rectangle();
        Vector2 projWorldDims;

        for (Entity e : _projectileEntities) {
            projPos = posMapper.get(e);
            projTex = textureMapper.get(e);
            projRen = renderMapper.get(e);
            projComp = projMapper.get(e);

            projWorldDims = RenderUtils.getWorldDims(projRen, projTex);

            projRectangle.x = projPos._pos.x;
            projRectangle.y = projPos._pos.y;
            projRectangle.width = projWorldDims.x;
            projRectangle.height = projWorldDims.y;

            if (_playerEntities.size() > 0) {
                if (CollisionUtils.AABBCollision(projRectangle, playerRectangle)) {
                    System.out.println("Applying " + projComp._damage + " damage to player!");
                    playerHealth._health -= projComp._damage;
                    _engine.removeEntity(e);
                }
            }

            // Loop over networked players
            Vector2 netWorldDims;
            Rectangle netRectangle = new Rectangle();
            for (Entity netPlayer : _networkPlayerEntities) {
                netPlayerPos = posMapper.get(netPlayer);
                netPlayerTex = textureMapper.get(netPlayer);
                netPlayerRen = renderMapper.get(netPlayer);

                netWorldDims = RenderUtils.getWorldDims(netPlayerRen, netPlayerTex);
                float netx = netPlayerPos._pos.x + netWorldDims.x/4.0f;
                float nety = netPlayerPos._pos.y + netWorldDims.y/4.0f;
                float netw = netWorldDims.x/2.0f;
                float neth = netWorldDims.y/2.0f;

                netRectangle.x = netx;
                netRectangle.y = nety;
                netRectangle.width = netw;
                netRectangle.height =neth;

                if (CollisionUtils.AABBCollision(projRectangle, netRectangle)) {
                    System.out.println("A projectile hit a network player!");
                    _engine.removeEntity(e);
                }
            }
        }
    }
}

package com.anecoz.br.systems;

import com.anecoz.br.*;
import com.anecoz.br.components.*;
import com.anecoz.br.components.weapon.ProjectileComponent;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class TiledMapCollisionSystem extends EntitySystem {
    private Engine _engine;
    private TiledMapTileLayer _tileLayer;
    private ImmutableArray<Entity> _entities;

    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
    private ComponentMapper<RenderComponent> rm = ComponentMapper.getFor(RenderComponent.class);
    private ComponentMapper<TextureComponent> tm = ComponentMapper.getFor(TextureComponent.class);
    private ComponentMapper<ProjectileComponent> projMapper = ComponentMapper.getFor(ProjectileComponent.class);

    public TiledMapCollisionSystem(TiledMapTileLayer tileLayer) {
        _tileLayer = tileLayer;
    }

    @Override
    public void addedToEngine(Engine engine) {
        _entities = engine.getEntitiesFor(Family.all(
                PositionComponent.class,
                VelocityComponent.class,
                RenderComponent.class,
                TextureComponent.class)
                .get());
        _engine = engine;
    }

    @Override
    public void update(float deltaTime) {
        PositionComponent posComp;
        VelocityComponent velComp;
        RenderComponent renComp;
        TextureComponent texComp;

        for (int i = 0; i < _entities.size(); i++) {
            Entity e = _entities.get(i);
            posComp = pm.get(e);
            velComp = vm.get(e);
            renComp = rm.get(e);
            texComp = tm.get(e);
            boolean didCollide = false;

            float w = renComp._scale * texComp._texture.getWidth() * EntityManager.PIX_TO_WORLD_FACTOR;
            float h = renComp._scale * texComp._texture.getHeight() * EntityManager.PIX_TO_WORLD_FACTOR;
            w = w/2.0f;
            h = h/2.0f;

            Vector2 tmp = new Vector2(posComp._pos);

            if (velComp._vel.x > 0) {
                posComp._pos.x += velComp._vel.x * deltaTime;
                if (checkCollision(posComp._pos, w, h)) {
                    posComp._pos.x = tmp.x;
                    didCollide = true;
                }
            }
            if (velComp._vel.x < 0) {
                posComp._pos.x += velComp._vel.x * deltaTime;
                if (checkCollision(posComp._pos, w, h)) {
                    posComp._pos.x = tmp.x;
                    didCollide = true;
                }
            }
            if (velComp._vel.y > 0) {
                posComp._pos.y += velComp._vel.y * deltaTime;
                if (checkCollision(posComp._pos, w, h)) {
                    posComp._pos.y = tmp.y;
                    didCollide = true;
                }
            }
            if (velComp._vel.y < 0) {
                posComp._pos.y += velComp._vel.y * deltaTime;
                if (checkCollision(posComp._pos, w, h)) {
                    posComp._pos.y = tmp.y;
                    didCollide = true;
                }
            }

            // If is projectile, remove from engine
            if (didCollide && projMapper.has(e)) {
                _engine.removeEntity(e);
            }
        }
    }

    private boolean checkCollision(Vector2 compPos, float w, float h) {
        Vector2 pos = new Vector2(compPos);
        pos.x = pos.x + w/2.0f;
        pos.y = pos.y + h/2.0f;

        if (isCollisionAt((int)pos.x, (int)pos.y)) {
            return true;
        }
        else if (isCollisionAt((int)(pos.x + w), (int)pos.y)) {
            return true;
        }
        else if (isCollisionAt((int)pos.x, (int)(pos.y + h))) {
            return true;
        }
        else if (isCollisionAt((int)(pos.x + w), (int)(pos.y + h))) {
            return true;
        }
        else
            return false;
    }

    private boolean isCollisionAt(int x, int y) {
        if (_tileLayer.getCell(x, y) == null)
            return false;

        return _tileLayer.getCell(x, y)
                .getTile()
                .getProperties()
                .get("isCollision", String.class) != null;
    }
}

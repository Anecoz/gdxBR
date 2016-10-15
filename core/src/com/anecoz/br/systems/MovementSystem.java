package com.anecoz.br.systems;

import com.anecoz.br.components.PositionComponent;
import com.anecoz.br.components.VelocityComponent;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;

public class MovementSystem extends EntitySystem {
    private ImmutableArray<Entity> _entities;

    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);

    public MovementSystem() {

    }

    @Override
    public void addedToEngine(Engine engine) {
        _entities = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        PositionComponent posComp;
        VelocityComponent velComp;

        for (int i = 0; i < _entities.size(); i++) {
            Entity e = _entities.get(i);
            posComp = pm.get(e);
            velComp = vm.get(e);

            posComp._pos.x += velComp._vel.x * deltaTime;
            posComp._pos.y += velComp._vel.y * deltaTime;
        }
    }
}

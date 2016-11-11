package com.anecoz.br.systems;

import com.anecoz.br.components.HealthComponent;
import com.anecoz.br.components.PlayerComponent;
import com.anecoz.br.network.client.ClientMasterHandler;
import com.anecoz.br.network.client.ClientSender;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;

public class HealthSystem extends EntitySystem {
    private ImmutableArray<Entity> _entities;
    private Engine _engine;

    private ComponentMapper<HealthComponent> healthMapper = ComponentMapper.getFor(HealthComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        _entities = engine.getEntitiesFor(Family.all(HealthComponent.class, PlayerComponent.class).get());
        _engine = engine;
    }

    @Override
    public void update(float deltaTime) {
        for (Entity e : _entities) {
            HealthComponent healthComp = healthMapper.get(e);

            if (healthComp._health <= 0) {
                // For now we assume this is the player and send a disconnect
                ClientMasterHandler._client.stop();
                _engine.removeEntity(e);
            }

            ClientSender.updatePlayerHealth(healthComp._health);
        }
    }
}

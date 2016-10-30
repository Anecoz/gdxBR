package com.anecoz.br.systems;


import com.anecoz.br.components.*;
import com.anecoz.br.components.network.NetworkPlayerComponent;
import com.anecoz.br.network.client.ClientSender;
import com.anecoz.br.utils.ResourceHandler;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;

import java.util.concurrent.ConcurrentHashMap;

public class NetworkSystem extends EntitySystem {
    public static ConcurrentHashMap<Integer, Vector2> _pendingPlayersToAdd;
    public static ConcurrentHashMap<Integer, Vector2> _pendingPositionUpdates;

    private Engine _engine;
    private ImmutableArray<Entity> _entities;
    private Entity _playerEntity;

    private ComponentMapper<NetworkPlayerComponent> nc = ComponentMapper.getFor(NetworkPlayerComponent.class);
    private ComponentMapper<PositionComponent> pc = ComponentMapper.getFor(PositionComponent.class);

    public NetworkSystem() {
        _pendingPlayersToAdd = new ConcurrentHashMap<Integer, Vector2>();
        _pendingPositionUpdates = new ConcurrentHashMap<Integer, Vector2>();
    }

    @Override
    public void addedToEngine(Engine engine) {
        _engine = engine;
        _entities = engine.getEntitiesFor(Family.all(NetworkPlayerComponent.class, PositionComponent.class).get());
        _playerEntity = engine.getEntitiesFor(Family.all(PlayerComponent.class, PositionComponent.class).get()).first();
    }

    @Override
    public void update(float delta) {
        NetworkPlayerComponent netComp;
        PositionComponent posComp;

        // Go through all pending players to add
        for (Integer key : _pendingPlayersToAdd.keySet()) {
            Entity player = new Entity();
            player.add(new NetworkPlayerComponent(key))
                    .add(new TextureComponent(ResourceHandler.PLAYER_TEXTURE))
                    .add(new PositionComponent(_pendingPlayersToAdd.get(key)))
                    .add(new RenderComponent(0, .45f))
                    .add(new VisibilityComponent());
            _engine.addEntity(player);
        }

        _pendingPlayersToAdd.clear();

        for (int i = 0; i < _entities.size(); i++) {
            Entity e = _entities.get(i);
            netComp = nc.get(e);
            posComp = pc.get(e);

            // Check if this entity has a pending update
            if (_pendingPositionUpdates.containsKey(netComp._id)) {
                posComp._pos = _pendingPositionUpdates.get(netComp._id);
                _pendingPositionUpdates.remove(netComp._id);
            }
        }

        // Update player position
        PositionComponent playerPosComp = pc.get(_playerEntity);
        ClientSender.updatePlayerPos(playerPosComp._pos);
    }
}

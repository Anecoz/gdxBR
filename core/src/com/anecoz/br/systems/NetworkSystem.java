package com.anecoz.br.systems;


import com.anecoz.br.blueprints.ProjectileBlueprint;
import com.anecoz.br.components.*;
import com.anecoz.br.components.network.NetworkPlayerComponent;
import com.anecoz.br.network.client.ClientSender;
import com.anecoz.br.utils.ResourceHandler;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class NetworkSystem extends EntitySystem {
    public static ConcurrentHashMap<Integer, Vector2> _pendingPlayersToAdd;
    public static ConcurrentHashMap<Integer, Vector2> _pendingPositionUpdates;
    public static ConcurrentHashMap<Integer, Float> _pendingRotationUpdates;
    public static CopyOnWriteArrayList<ProjectileBlueprint> _pendingProjectiles;
    public static CopyOnWriteArrayList<Integer> _pendingPlayersToRemove;

    private Engine _engine;
    private ImmutableArray<Entity> _entities;
    private Entity _playerEntity;

    private ComponentMapper<NetworkPlayerComponent> nc = ComponentMapper.getFor(NetworkPlayerComponent.class);
    private ComponentMapper<PositionComponent> pc = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<RenderComponent> rc = ComponentMapper.getFor(RenderComponent.class);

    public NetworkSystem() {
        _pendingPlayersToAdd = new ConcurrentHashMap<Integer, Vector2>();
        _pendingPositionUpdates = new ConcurrentHashMap<Integer, Vector2>();
        _pendingRotationUpdates = new ConcurrentHashMap<Integer, Float>();
        _pendingProjectiles = new CopyOnWriteArrayList<ProjectileBlueprint>();
        _pendingPlayersToRemove = new CopyOnWriteArrayList<Integer>();
    }

    @Override
    public void addedToEngine(Engine engine) {
        _engine = engine;
        _entities = engine.getEntitiesFor(Family.all(NetworkPlayerComponent.class, PositionComponent.class, RenderComponent.class).get());
        _playerEntity = engine.getEntitiesFor(Family.all(PlayerComponent.class, PositionComponent.class, RenderComponent.class).get()).first();
    }

    @Override
    public void update(float delta) {
        NetworkPlayerComponent netComp;
        PositionComponent posComp;
        RenderComponent renComp;

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

        // Update position/forward
        for (int i = 0; i < _entities.size(); i++) {
            Entity e = _entities.get(i);
            netComp = nc.get(e);
            posComp = pc.get(e);
            renComp = rc.get(e);

            // Check if this entity has a pending update
            if (_pendingPositionUpdates.containsKey(netComp._id)) {
                posComp._pos = _pendingPositionUpdates.get(netComp._id);
                _pendingPositionUpdates.remove(netComp._id);
            }

            if (_pendingRotationUpdates.containsKey(netComp._id)) {
                renComp._rotation = _pendingRotationUpdates.get(netComp._id);
                _pendingRotationUpdates.remove(netComp._id);
            }

            if (_pendingPlayersToRemove.contains(netComp._id)) {
                _engine.removeEntity(e);
            }
            _pendingPlayersToRemove.clear();
        }

        // Go through projectiles to spawn
        for (ProjectileBlueprint blueprint : _pendingProjectiles) {
            Entity proj = new Entity();
            for (Component comp : blueprint.getComponents()) {
                proj.add(comp);
            }
            _engine.addEntity(proj);
        }
        _pendingProjectiles.clear();

        // Send updated player position and rotation
        PositionComponent playerPosComp = pc.get(_playerEntity);
        RenderComponent playerRenComp = rc.get(_playerEntity);
        ClientSender.updatePlayerPos(playerPosComp._pos);
        ClientSender.updatePlayerRotation(playerRenComp._rotation);
    }
}

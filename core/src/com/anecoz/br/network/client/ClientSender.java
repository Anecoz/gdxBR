package com.anecoz.br.network.client;

import com.anecoz.br.blueprints.ProjectileBlueprint;
import com.badlogic.gdx.math.Vector2;

import static com.anecoz.br.network.client.ClientMasterHandler._client;
import static com.anecoz.br.network.shared.SharedNetwork.*;

public class ClientSender {

    public static void registerPlayer(String name, Vector2 pos) {
        RegisterPlayerToServer reg = new RegisterPlayerToServer();
        reg._displayName = name;
        reg._initPos = pos;
        _client.sendTCP(reg);
    }

    public static void updatePlayerHealth(float health) {
        UpdatePlayerHealth up = new UpdatePlayerHealth();
        up._health = health;
        _client.sendTCP(up);
    }

    public static void updatePlayerPos(Vector2 pos) {
        UpdatePlayerPosition up = new UpdatePlayerPosition();
        up._pos = pos;
        _client.sendTCP(up);
    }

    public static void updatePlayerRotation(float rot) {
        UpdatePlayerRotation up = new UpdatePlayerRotation();
        up._rotation = rot;
        _client.sendTCP(up);
    }

    public static void disconnectPlayer() {
        DisconnectPlayer disc = new DisconnectPlayer();
        _client.sendTCP(disc);
    }

    public static void spawnProjectile(ProjectileBlueprint blueprint) {
        SpawnProjectile spawn = new SpawnProjectile();
        spawn._forward = blueprint.getForward();
        spawn._pos = blueprint.getPos();
        spawn._rotation = blueprint.getRotation();
        spawn._type = blueprint.getType();

        _client.sendTCP(spawn);
    }

    public static void dropItem(Vector2 pos, int ammo, WEAPON_TYPE type) {
        AddWeapon add = new AddWeapon();
        add._ammo = ammo;
        add._pos = pos;
        add._type = type;

        _client.sendTCP(add);
    }
}

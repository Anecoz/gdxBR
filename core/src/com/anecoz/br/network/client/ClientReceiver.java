package com.anecoz.br.network.client;

import com.anecoz.br.blueprints.BulletProjectileBlueprint;
import com.anecoz.br.blueprints.RifleWeaponBlueprint;
import com.anecoz.br.network.shared.SharedNetwork;
import com.anecoz.br.systems.NetworkSystem;
import com.badlogic.gdx.math.Vector2;

import static com.anecoz.br.network.client.ClientMasterHandler._client;
import static com.anecoz.br.network.shared.SharedNetwork.*;

public class ClientReceiver {

    public static void addAllCurrentOtherPlayers(int[] ids, String[] names, Vector2[] positions) {
        int count = ids.length;

        for (int i = 0; i < count; i++) {
            NetworkSystem.NetworkPlayerInfo obj = new NetworkSystem.NetworkPlayerInfo(positions[i], names[i]);
            NetworkSystem._pendingPlayersToAdd.put(ids[i], obj);
        }
    }

    public static void disconnectOtherPlayer(int id) {
        NetworkSystem._pendingPlayersToRemove.add(id);
    }

    public static void updateOtherPlayerPosition(Vector2 pos, int id) {
        NetworkSystem._pendingPositionUpdates.put(id, pos);
    }

    public static void updateOtherPlayerRotation(float rot, int id) {
        NetworkSystem._pendingRotationUpdates.put(id, rot);
    }

    public static void addOtherPlayer(int id, Vector2 pos, String displayName) {
        NetworkSystem.NetworkPlayerInfo obj = new NetworkSystem.NetworkPlayerInfo(pos, displayName);
        NetworkSystem._pendingPlayersToAdd.put(id, obj);
    }

    public static void updatePlayerHealth(UpdatePlayerHealth up, int id) {
        NetworkSystem._pendingHealthUpdates.put(id, up._health);
    }

    public static void spawnProjectile(SpawnProjectile spawn) {
        // Check type to know what blueprint to create
        switch (spawn._type) {
            case BULLET:
                BulletProjectileBlueprint blueprint = new BulletProjectileBlueprint();
                blueprint.setData(spawn._pos, spawn._forward, spawn._rotation);
                NetworkSystem._pendingProjectiles.add(blueprint);
                break;
            default:
                break;
        }
    }

    public static void addWeapon(AddWeapon add) {
        switch (add._type) {
            case RIFLE:
                RifleWeaponBlueprint blueprint = new RifleWeaponBlueprint();
                blueprint.setData(add._pos, add._ammo);
                NetworkSystem._pendingWeapons.add(blueprint);
                break;
            default:
                break;
        }
    }

    public static void removeItem(RemoveItem rem) {
        NetworkSystem._pendingItemsToRemove.add(rem._pos);
    }
}

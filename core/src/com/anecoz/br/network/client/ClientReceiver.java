package com.anecoz.br.network.client;

import com.anecoz.br.systems.NetworkSystem;
import com.badlogic.gdx.math.Vector2;

import static com.anecoz.br.network.client.ClientMasterHandler._client;
import static com.anecoz.br.network.shared.SharedNetwork.*;

public class ClientReceiver {

    public static void addAllCurrentOtherPlayers(int[] ids, String[] names, Vector2[] positions) {
        int count = ids.length;

        for (int i = 0; i < count; i++) {
            NetworkSystem._pendingPlayersToAdd.put(ids[i], positions[i]);
        }
    }

    public static void disconnectOtherPlayer(int id) {
        // TODO
    }

    public static void updateOtherPlayerPosition(Vector2 pos, int id) {
        NetworkSystem._pendingPositionUpdates.put(id, pos);
    }

    public static void addOtherPlayer(int id, Vector2 pos, String displayName) {
        NetworkSystem._pendingPlayersToAdd.put(id, pos);
    }
}

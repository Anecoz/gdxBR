package com.anecoz.br.network.client;

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

    public static void updatePlayerPos(Vector2 pos) {
        UpdatePlayerPosition up = new UpdatePlayerPosition();
        up._pos = pos;
        _client.sendTCP(up);
    }

    public static void disconnectPlayer() {
        DisconnectPlayer disc = new DisconnectPlayer();
        _client.sendTCP(disc);
    }
}

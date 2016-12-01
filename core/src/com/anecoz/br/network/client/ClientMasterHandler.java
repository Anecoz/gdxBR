package com.anecoz.br.network.client;


import static com.anecoz.br.network.shared.SharedNetwork.*;
import static com.anecoz.br.network.client.ClientReceiver.*;

import com.anecoz.br.network.shared.SharedNetwork;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

public class ClientMasterHandler {
    public static Client _client;

    public static void init() {
        _client = new Client();
        _client.start();

        SharedNetwork.register(_client);

        _client.addListener(new Listener() {

            public void connected(Connection connection) {
                System.out.println("Client connected!");
            }

            public void received(Connection connection, Object object) {
                if (object instanceof RegisterOtherPlayer) {
                    RegisterOtherPlayer reg = (RegisterOtherPlayer)object;
                    addOtherPlayer(reg._id, reg._pos, reg._displayName);
                }
                else if (object instanceof UpdateOtherPlayerPosition) {
                    UpdateOtherPlayerPosition up = (UpdateOtherPlayerPosition)object;
                    updateOtherPlayerPosition(up._pos, up._id);
                }
                else if (object instanceof RegisterCurrentOtherPlayers) {
                    RegisterCurrentOtherPlayers reg = (RegisterCurrentOtherPlayers)object;
                    addAllCurrentOtherPlayers(reg._ids, reg._displayNames, reg._positions);
                }
                else if (object instanceof OtherPlayerDisconnect) {
                    OtherPlayerDisconnect disc = (OtherPlayerDisconnect)object;
                    disconnectOtherPlayer(disc._id);
                }
                else if (object instanceof UpdateOtherPlayerRotation) {
                    UpdateOtherPlayerRotation up = (UpdateOtherPlayerRotation)object;
                    updateOtherPlayerRotation(up._rotation, up._id);
                }
                else if (object instanceof SpawnProjectile) {
                    SpawnProjectile spawn = (SpawnProjectile)object;
                    spawnProjectile(spawn);
                }
                else if (object instanceof UpdatePlayerHealth) {
                    UpdatePlayerHealth up = (UpdatePlayerHealth)object;
                    updatePlayerHealth(up, up._id);
                }
                else if (object instanceof AddWeapon) {
                    AddWeapon add = (AddWeapon)object;
                    addWeapon(add);
                }
            }

            public void disconnected(Connection connection) {
                System.out.println("Client disconnected :(");
            }
        });

        // Simply try to connect (server has to be running)
        try {
            _client.connect(5000, "localhost", SharedNetwork._port); //83.226.195.88
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

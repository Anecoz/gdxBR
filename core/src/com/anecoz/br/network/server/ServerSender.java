package com.anecoz.br.network.server;

import com.anecoz.br.network.shared.SharedNetwork.*;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;

import static com.anecoz.br.network.server.BRGameServer._server;

public class ServerSender {

    public static void sendNewRegister(int id, Vector2 pos, String displayName, GameConnection conn) {
        RegisterOtherPlayer reg = new RegisterOtherPlayer();
        reg._displayName = displayName;
        reg._id = id;
        reg._pos = pos;

        _server.sendToAllExceptTCP(conn.getID(), reg);
    }

    public static void sendUpdatePlayerHealth(UpdatePlayerHealth up, GameConnection conn) {
        up._id = conn._id;
        _server.sendToAllExceptTCP(conn.getID(), up);
    }

    public static void sendProjectileSpawn(SpawnProjectile spawn, GameConnection conn) {
        _server.sendToAllExceptTCP(conn.getID(), spawn);
    }

    public static void sendNewPosition(int id, Vector2 pos, GameConnection conn) {
        UpdateOtherPlayerPosition up = new UpdateOtherPlayerPosition();
        up._id = id;
        up._pos = pos;

        _server.sendToAllExceptTCP(conn.getID(), up);
    }

    public static void sendNewRotation(int id, float rot, GameConnection conn) {
        UpdateOtherPlayerRotation up = new UpdateOtherPlayerRotation();
        up._id = id;
        up._rotation = rot;

        _server.sendToAllExceptTCP(conn.getID(), up);
    }

    public static void sendAllPlayers(GameConnection playerConn) {
        RegisterCurrentOtherPlayers reg = new RegisterCurrentOtherPlayers();

        Connection[] connections = _server.getConnections();
        if (connections.length != 1) {
            int[] ids = new int[connections.length - 1];
            String[] names = new String[connections.length - 1];
            Vector2[] positions = new Vector2[connections.length - 1];

            int counter = 0;
            for (int i = 0; i < connections.length; i++) {
                GameConnection connection = (GameConnection)connections[i];
                if (connection._id != playerConn._id) {
                    ids[counter] = connection._id;
                    names[counter] = connection._displayName;
                    positions[counter] = connection._pos;
                    counter++;
                }
            }

            reg._ids = ids;
            reg._positions = positions;
            reg._displayNames = names;
            _server.sendToTCP(playerConn.getID(), reg);
        }
    }

    public static void disconnectPlayer(GameConnection conn) {
        OtherPlayerDisconnect disc = new OtherPlayerDisconnect();
        disc._id = conn._id;
        _server.sendToAllExceptTCP(conn.getID(), disc);
    }
}

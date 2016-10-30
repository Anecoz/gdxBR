package com.anecoz.br.network.server;

import com.anecoz.br.network.shared.SharedNetwork;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import com.anecoz.br.network.shared.SharedNetwork.*;

import java.io.IOException;

public class BRGameServer {

    private static int counter = 0;
    public static Server _server;

    private BRGameServer() {
        _server = new Server() {
            protected Connection newConnection() {
                return new GameConnection();
            }
        };

        SharedNetwork.register(_server);
        _server.addListener(new Listener() {

            public void received(Connection c, Object object) {
                GameConnection conn = (GameConnection) c;

                if (object instanceof RegisterPlayerToServer) {

                    if (conn._id != -1)
                        return;

                    RegisterPlayerToServer reg = (RegisterPlayerToServer)object;
                    conn._id = counter;
                    conn._pos = reg._initPos;
                    conn._displayName = reg._displayName;
                    counter++;
                    ServerSender.sendNewRegister(conn._id, conn._pos, conn._displayName, conn);
                    ServerSender.sendAllPlayers(conn);
                }
                else if (object instanceof UpdatePlayerPosition) {
                    if (conn._id == -1)
                        return;

                    int playerId = conn._id;

                    UpdatePlayerPosition up = (UpdatePlayerPosition)object;
                    ServerSender.sendNewPosition(playerId, up._pos, conn);
                }
                else if (object instanceof UpdatePlayerRotation) {
                    if (conn._id == -1)
                        return;

                    UpdatePlayerRotation up = (UpdatePlayerRotation)object;
                    ServerSender.sendNewRotation(conn._id, up._rotation, conn);
                }
                else if (object instanceof DisconnectPlayer) {
                    if (conn._id == -1)
                        return;

                    ServerSender.disconnectPlayer(conn);
                }
            }

            public void disconnected(Connection c) {
                GameConnection connection = (GameConnection) c;
                ServerSender.disconnectPlayer(connection);
            }
        });

        try {
            _server.bind(SharedNetwork._port);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        _server.start();
    }

    public static void main(String[] args) {
        new BRGameServer();
    }
}

package com.anecoz.br.network.server;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;

public class GameConnection extends Connection {
    public int _id = -1;
    public Vector2 _pos;
    public String _displayName;
}

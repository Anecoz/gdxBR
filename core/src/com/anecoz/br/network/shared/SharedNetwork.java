package com.anecoz.br.network.shared;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class SharedNetwork {
    static public final int _port = 54557;

    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(PROJECTILE_TYPE.class);
        kryo.register(WEAPON_TYPE.class);
        kryo.register(Vector2.class);
        kryo.register(RemoveItem.class);
        kryo.register(AddWeapon.class);
        kryo.register(SpawnProjectile.class);
        kryo.register(UpdatePlayerHealth.class);
        kryo.register(UpdatePlayerPosition.class);
        kryo.register(UpdatePlayerRotation.class);
        kryo.register(RegisterPlayerToServer.class);
        kryo.register(DisconnectPlayer.class);
        kryo.register(RegisterOtherPlayer.class);
        kryo.register(int[].class);
        kryo.register(Vector2[].class);
        kryo.register(String[].class);
        kryo.register(RegisterCurrentOtherPlayers.class);
        kryo.register(UpdateOtherPlayerPosition.class);
        kryo.register(UpdateOtherPlayerRotation.class);
        kryo.register(OtherPlayerDisconnect.class);
    }

    public enum PROJECTILE_TYPE {
        BULLET
    }

    public enum WEAPON_TYPE {
        RIFLE
    }

    // FROM:    CLIENT OR SERVER
    // TO:      SERVER OR CLIENT
    // desc:    Tells server/client to remove item at a location
    static public class RemoveItem {
        public Vector2 _pos;
    }

    // FROM:    CLIENT OR SERVER
    // TO:      SERVER OR CLIENT
    // desc:    Tells server/client to spawn a dropped weapon at a location in the world
    static public class AddWeapon {
        public WEAPON_TYPE _type;
        public int _ammo;
        public Vector2 _pos;
    }

    // FROM:    CLIENT OR SERVER
    // TO:      SERVER OR CLIENT
    // desc:    Tells server/client to spawn a projectile with some data
    static public class SpawnProjectile {
        public PROJECTILE_TYPE _type;
        public Vector2 _pos;
        public Vector2 _forward;
        public float _rotation;
    }

    // FROM:    CLIENT OR SERVER
    // TO:      SERVER OR CLIENT
    // desc:    Update player health
    static public class UpdatePlayerHealth {
        public int _id;
        public float _health;
    }

    // FROM:    CLIENT
    // TO:      SERVER
    // desc:    Tells the server about our new position
    static public class UpdatePlayerPosition {
        public Vector2 _pos;
    }

    // FROM:    CLIENT
    // TO:      SERVER
    // desc:    Tells the server about our new rotation
    static public class UpdatePlayerRotation {
        public float _rotation;
    }

    // FROM:    CLIENT
    // TO:      SERVER
    // desc:    Client sends this to server to register. Gets an id on server and is broadcast to all other players
    static public class RegisterPlayerToServer {
        public String _displayName;
        public Vector2 _initPos;
    }

    // FROM:    CLIENT
    // TO:      SERVER
    // desc:    Client sends this to server to notify that we have disconnected (for instance gone to main menu)
    static public class DisconnectPlayer {
        // We actually don't need anything in here, server will know what player it is by the id of his connection
        public String _displayName;
    }

    // FROM:    SERVER
    // TO:      CLIENT
    // desc:    Server sends this to all other players when a new player should be registered by them
    static public class RegisterOtherPlayer {
        public int _id;
        public Vector2 _pos;
        public String _displayName;
    }

    // FROM:    SERVER
    // TO:      CLIENT
    // desc:    Server sends this to a newly connected player, so that he gets all current ones aswell
    static public class RegisterCurrentOtherPlayers {
        public int[] _ids;
        public Vector2[] _positions;
        public String[] _displayNames;
    }

    // FROM:    SERVER
    // TO:      CLIENT
    // desc:    Sent to all (but yourself) to update other positions.
    static public class UpdateOtherPlayerPosition {
        public Vector2 _pos;
        public int _id;
    }

    // FROM:    SERVER
    // TO:      CLIENT
    // desc:    Sent to all (but yourself) to update other rotation.
    static public class UpdateOtherPlayerRotation {
        public float _rotation;
        public int _id;
    }

    // FROM:    SERVER
    // TO:      CLIENT
    // desc:    Sent whenever a player has disconnected from the server
    static public class OtherPlayerDisconnect {
        public int _id;
    }
}

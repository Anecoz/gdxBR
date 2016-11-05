package com.anecoz.br.components.network;

import com.badlogic.ashley.core.Component;

public class NetworkPlayerComponent implements Component {
    public int _id;
    public String _name;

    public NetworkPlayerComponent(int id, String name) {
        _id = id;
        _name = name;
    }
}

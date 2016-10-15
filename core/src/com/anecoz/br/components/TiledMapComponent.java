package com.anecoz.br.components;


import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class TiledMapComponent implements Component {
    public TiledMap _map;

    public TiledMapComponent(TiledMap map) {
        _map = map;
    }
}

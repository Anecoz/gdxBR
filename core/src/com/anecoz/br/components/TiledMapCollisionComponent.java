package com.anecoz.br.components;


import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class TiledMapCollisionComponent implements Component {
    public TiledMapTileLayer _tileLayer;

    public TiledMapCollisionComponent(TiledMapTileLayer tileLayer) {
        _tileLayer = tileLayer;
    }
}

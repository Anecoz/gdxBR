package com.anecoz.br.components;


import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PositionComponent implements Component {
    public Vector2 _pos;
    public Vector2 _centerPos;

    public PositionComponent(Vector2 pos) {
        _pos = pos;
        _centerPos = new Vector2(-1f, -1f);
    }
}

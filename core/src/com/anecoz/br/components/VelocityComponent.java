package com.anecoz.br.components;


import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class VelocityComponent implements Component {
    public Vector2 _vel;

    public VelocityComponent(Vector2 vel) {
        _vel = vel;
    }
}

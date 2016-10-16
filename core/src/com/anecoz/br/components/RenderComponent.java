package com.anecoz.br.components;


import com.badlogic.ashley.core.Component;

public class RenderComponent implements Component {
    public float _rotation;
    public float _scale;
    public int _bin;

    public RenderComponent(float rotation, float scale) {
        _rotation = rotation;
        _scale = scale;
        _bin = 0;
    }
}

package com.anecoz.br.components;


import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class TextComponent implements Component {
    public String _text;
    public Vector2 _pos;

    public TextComponent(String text, Vector2 pos) {
        _text = text;
        _pos = pos;
    }
}

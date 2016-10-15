package com.anecoz.br.components;


import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;

public class TextureComponent implements Component {
    public Texture _texture;

    public TextureComponent(String path) {
        _texture = new Texture(path);
    }

    public TextureComponent(Texture texture) {
        _texture = texture;
    }
}

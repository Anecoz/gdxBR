package com.anecoz.br.utils;

import com.anecoz.br.EntityManager;
import com.anecoz.br.components.RenderComponent;
import com.anecoz.br.components.TextureComponent;
import com.badlogic.gdx.math.Vector2;

public class RenderUtils {
    public static Vector2 getWorldDims(RenderComponent renComp, TextureComponent texComp) {
        float w = renComp._scale * texComp._texture.getWidth() * EntityManager.PIX_TO_WORLD_FACTOR;
        float h = renComp._scale * texComp._texture.getHeight() * EntityManager.PIX_TO_WORLD_FACTOR;

        return new Vector2(w, h);
    }
}

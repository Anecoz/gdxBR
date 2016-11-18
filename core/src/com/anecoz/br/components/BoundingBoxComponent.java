package com.anecoz.br.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BoundingBoxComponent implements Component{
    public Rectangle _boundingBox;

    public BoundingBoxComponent(Vector2 position){
        _boundingBox = new Rectangle(position.x, position.y, position.x + 1, position.y + 1); // 1 tile wide
    }


}

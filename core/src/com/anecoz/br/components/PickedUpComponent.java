package com.anecoz.br.components;


import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PickedUpComponent implements Component {
    public boolean _inHands;
    public Vector2 _inventorySlot;

    public PickedUpComponent(boolean inHands) {
        _inventorySlot = new Vector2(-1f, -1f);
        _inHands = inHands;
    }
}

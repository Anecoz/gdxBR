package com.anecoz.br.components;


import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PickedUpComponent implements Component {
    public boolean _inHands;
    public int _inventorySlot;

    public PickedUpComponent(boolean inHands, int slot) {
        _inventorySlot = slot;
        _inHands = inHands;
    }
}

package com.anecoz.br.components;


import com.badlogic.ashley.core.Component;

public class PickedUpComponent implements Component {
    public boolean _inHands;
    public int _inventorySlot;

    public PickedUpComponent(boolean inHands, int inventorySlot) {
        _inventorySlot = inventorySlot;
        _inHands = inHands;
    }
}

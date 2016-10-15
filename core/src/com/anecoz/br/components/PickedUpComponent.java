package com.anecoz.br.components;


import com.badlogic.ashley.core.Component;

public class PickedUpComponent implements Component {
    public boolean _inInventory;
    public int _inventorySlot;

    public PickedUpComponent(boolean inInventory, int inventorySlot) {
        _inventorySlot = inventorySlot;
        _inInventory = inInventory;
    }
}

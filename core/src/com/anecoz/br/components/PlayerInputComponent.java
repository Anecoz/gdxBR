package com.anecoz.br.components;


import com.badlogic.ashley.core.Component;

public class PlayerInputComponent implements Component {
    public boolean _isHoldingShootButton = false;
    public boolean _hasClickedShootButton = false;

    public PlayerInputComponent() {}
}

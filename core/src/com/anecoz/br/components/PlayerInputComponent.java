package com.anecoz.br.components;


import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PlayerInputComponent implements Component {
    public boolean _isHoldingShootButton = false;
    public boolean _hasClickedShootButton = false;
    public Vector2 _forward;
    public Vector2 _pos; //< ugh
    public float _rotation;

    public PlayerInputComponent() {}
}

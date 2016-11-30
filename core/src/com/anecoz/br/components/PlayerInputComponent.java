package com.anecoz.br.components;


import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PlayerInputComponent implements Component {
    public boolean _isHoldingMouseButton = false;
    public boolean _hasClickedMouseButton = false;
    public boolean _hasClickedReloadButton = false;
    public boolean _isDraggingItem = false;
    public Vector2 _oldMousePosition = new Vector2();
    public Vector2 _currentMousePosition = new Vector2();
    public Vector2 _forward;
    public float _rotation;

    public PlayerInputComponent() {}
}

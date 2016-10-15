package com.anecoz.br.components.weapon;


import com.badlogic.ashley.core.Component;

public class ShootingComponent implements Component {
    public int _ammunitionCount;
    public boolean _isFiring = false;
    public boolean _isAutomatic;

    public ShootingComponent(int ammunitionCount, boolean isAutomatic) {
        _ammunitionCount = ammunitionCount;
        _isAutomatic = isAutomatic;
    }
}

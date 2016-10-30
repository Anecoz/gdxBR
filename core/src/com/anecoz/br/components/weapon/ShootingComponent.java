package com.anecoz.br.components.weapon;


import com.badlogic.ashley.core.Component;

public class ShootingComponent implements Component {
    public int _ammunitionCount;        // Total available ammunition
    public int _magazineSize;           // How much fits in a mag
    public int _currentMagAmmo;         // How much is currently in the magazine
    public boolean _isAutomatic;

    public ShootingComponent(int ammunitionCount, int magazineSize, int currentMagAmmo, boolean isAutomatic) {
        _ammunitionCount = ammunitionCount;
        _isAutomatic = isAutomatic;
        _magazineSize = magazineSize;
        _currentMagAmmo = currentMagAmmo;
    }
}

package com.anecoz.br.components.weapon;


import com.anecoz.br.network.shared.SharedNetwork;
import com.badlogic.ashley.core.Component;

public class ShootingComponent implements Component {
    public int _ammunitionCount;        // Total available ammunition
    public int _magazineSize;           // How much fits in a mag
    public int _currentMagAmmo;         // How much is currently in the magazine
    public boolean _isAutomatic;
    public SharedNetwork.WEAPON_TYPE _type;

    public ShootingComponent(int ammunitionCount, int magazineSize, int currentMagAmmo, boolean isAutomatic, SharedNetwork.WEAPON_TYPE type) {
        _ammunitionCount = ammunitionCount;
        _isAutomatic = isAutomatic;
        _magazineSize = magazineSize;
        _currentMagAmmo = currentMagAmmo;
        _type = type;
    }
}

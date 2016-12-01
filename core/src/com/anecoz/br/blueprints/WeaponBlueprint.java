package com.anecoz.br.blueprints;

import com.anecoz.br.network.shared.SharedNetwork;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public interface WeaponBlueprint {
    void setData(Vector2 pos, int ammo);
    Vector2 getPos();

    SharedNetwork.WEAPON_TYPE getType();
    ArrayList<Component> getComponents();
}

package com.anecoz.br.blueprints;

import com.anecoz.br.network.shared.SharedNetwork;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public interface ProjectileBlueprint {
    void setData(Vector2 pos, Vector2 forward, float rotation);
    Vector2 getPos();
    Vector2 getForward();
    float getRotation();
    SharedNetwork.PROJECTILE_TYPE getType();
    ArrayList<Component> getComponents();
}

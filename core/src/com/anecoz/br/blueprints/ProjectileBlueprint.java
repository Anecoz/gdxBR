package com.anecoz.br.blueprints;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public interface ProjectileBlueprint {
    public void setData(Vector2 pos, Vector2 forward);
    public ArrayList<Component> getComponents();
}

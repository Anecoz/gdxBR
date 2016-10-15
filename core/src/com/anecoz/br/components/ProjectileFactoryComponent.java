package com.anecoz.br.components;


import com.anecoz.br.blueprints.ProjectileBlueprint;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class ProjectileFactoryComponent implements Component {
    public Vector2 _forward;
    public Vector2 _pos;

    public ProjectileBlueprint _blueprint;

    public ProjectileFactoryComponent(ProjectileBlueprint blueprint) {
        _blueprint = blueprint;
    }
}

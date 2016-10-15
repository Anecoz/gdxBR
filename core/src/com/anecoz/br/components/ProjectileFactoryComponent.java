package com.anecoz.br.components;


import com.anecoz.br.blueprints.ProjectileBlueprint;
import com.badlogic.ashley.core.Component;

public class ProjectileFactoryComponent implements Component {
    public ProjectileBlueprint _blueprint;

    public ProjectileFactoryComponent(ProjectileBlueprint blueprint) {
        _blueprint = blueprint;
    }
}

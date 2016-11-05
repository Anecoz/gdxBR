package com.anecoz.br.components.weapon;

import com.badlogic.ashley.core.Component;

public class ProjectileComponent implements Component {
    public int _damage;

    public ProjectileComponent(int damage) {
        _damage = damage;
    }
}

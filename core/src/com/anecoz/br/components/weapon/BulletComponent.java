package com.anecoz.br.components.weapon;

import com.badlogic.ashley.core.Component;

public class BulletComponent implements Component {
    public int _damage;

    public BulletComponent(int damage) {
        _damage = damage;
    }
}

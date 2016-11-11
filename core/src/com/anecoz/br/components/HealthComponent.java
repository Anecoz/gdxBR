package com.anecoz.br.components;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component {
    public float _health;

    public HealthComponent(float health) {
        _health = health;
    }
}

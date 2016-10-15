package com.anecoz.br.components;

import com.badlogic.ashley.core.Component;

public class TimerComponent implements Component {
    public int _frequency;
    public long _millisSinceLastActivation = -1;

    public TimerComponent(int frequency) {
        _frequency = frequency;
    }
}

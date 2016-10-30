package com.anecoz.br.components.weapon;

import com.anecoz.br.components.TimerComponent;

public class ReloadTimerComponent extends TimerComponent {
    public boolean _isReloading = false;

    public ReloadTimerComponent(int frequency) {
        super(frequency);
    }
}

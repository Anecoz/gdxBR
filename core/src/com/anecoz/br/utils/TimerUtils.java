package com.anecoz.br.utils;

import com.anecoz.br.components.TimerComponent;
import com.badlogic.gdx.utils.TimeUtils;

public class TimerUtils {

    public static boolean timerIsUp(TimerComponent timerComp) {
        return timerIsReset(timerComp) ||
                (TimeUtils.millis()) - timerComp._millisSinceLastActivation >=
                        (long)1000/((float)timerComp._frequency/60);
    }

    public static boolean timerIsReset(TimerComponent timerComp) {
        return timerComp._millisSinceLastActivation == -1;
    }

    public static void resetTimer(TimerComponent timerComp) {
        timerComp._millisSinceLastActivation = -1;
    }

    public static void updateTimer(TimerComponent timerComp) {
        timerComp._millisSinceLastActivation = TimeUtils.millis();
    }
}

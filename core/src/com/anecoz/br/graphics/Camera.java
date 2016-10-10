package com.anecoz.br.graphics;

import com.anecoz.br.level.Level;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class Camera extends OrthographicCamera {

    public Camera() {
        super();
    }

    public void updateCam(float delta, float targetPosX, float targetPosY) {
        //Creating a vector 3 which represents the target location most likely the player)
        Vector3 target = new Vector3(targetPosX, targetPosY, 0);
        final float speed = 1.0f - delta;
        //The result is roughly: old_position*0.9 + target * 0.1
        Vector3 cameraPosition = this.position;
        cameraPosition.scl(speed);
        target.scl(delta);
        cameraPosition.add(target);
        this.position.set(cameraPosition);
    }
}
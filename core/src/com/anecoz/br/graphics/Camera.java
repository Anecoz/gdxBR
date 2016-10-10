package com.anecoz.br.graphics;

import com.anecoz.br.level.Level;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class Camera extends OrthographicCamera {
    public static float WIN_SIZE_X = 20.0f;
    public static float WIN_SIZE_Y = 0.0f;
    public Camera() {
        super();
        init();
    }

    private void init() {
        float invAr = (float) 600 / (float) 800;  // Has to be changed to variable from config file. WIDTH, HEIGHT
        WIN_SIZE_Y = invAr * WIN_SIZE_X;
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

        checkBounds();
    }

    private void checkBounds() {
        Vector3 oldPosition = this.position;
        // Check camera with map bounds
        if(this.position.x < Level.getBounds().x) {
            this.position.x = oldPosition.x;
        }
        if(this.position.y < Level.getBounds().y) {
            this.position.y = oldPosition.y;
        }
        if((this.position.x + WIN_SIZE_X) > Level.getBounds().width) {
            this.position.x = oldPosition.x;
        }
        if((this.position.y + WIN_SIZE_Y) > Level.getBounds().height) {
            this.position.y = oldPosition.y;
        }
    }
}
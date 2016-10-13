package com.anecoz.br.graphics;

import com.anecoz.br.level.Level;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.badlogic.gdx.math.MathUtils.round;

public class Camera extends OrthographicCamera {
    public static float WIN_SIZE_X = 20.0f;
    public static float WIN_SIZE_Y = 0.0f;
    private Vector3 oldPosition;
    public Camera() {
        super();
        init();
    }

    private void init() {
        float invAr = (float) 600 / (float) 800;  // Has to be changed to variable from config file. WIDTH, HEIGHT
        WIN_SIZE_Y = invAr * WIN_SIZE_X;
    }

    public void updateCam(float delta, float targetPosX, float targetPosY) {
        if(!checkBounds()) {
            //Creating a vector 3 which represents the target location most likely the player)
            Vector3 target = new Vector3(targetPosX, targetPosY, 0);
            final float speed = 1.0f - delta;
            //The result is roughly: old_position*0.9 + target * 0.1
            Vector3 cameraPosition = this.position;
            cameraPosition.scl(speed);
            target.scl(delta);
            cameraPosition.add(target);
            //Clamping position values to map size, needed to prevent jitter caused from delta.
            cameraPosition.x = clamp(cameraPosition.x,
                    Level.getBounds().x + WIN_SIZE_X * 0.5f,
                    Level.getBounds().width - WIN_SIZE_X * 0.5f);
            cameraPosition.y = clamp(cameraPosition.y,
                    Level.getBounds().y + WIN_SIZE_Y * 0.5f,
                    Level.getBounds().height - WIN_SIZE_Y * 0.5f);
            cameraPosition.x = (float) Math.round(cameraPosition.x * 100) / 100;
            cameraPosition.y = (float) Math.round(cameraPosition.y * 100) / 100;
            this.position.set(cameraPosition);
            oldPosition = this.position;
        }
    }

    private boolean checkBounds() {
        // Check camera with map bounds
        if((this.position.x - WIN_SIZE_X * 0.5f) < Level.getBounds().x) {
            this.position.x = oldPosition.x;
            return true;
        }
        if((this.position.y + WIN_SIZE_Y * 0.5f) < Level.getBounds().y) {
            this.position.y = oldPosition.y;
            return true;
        }
        if((this.position.x + WIN_SIZE_X * 0.5f) > Level.getBounds().width) {
            this.position.x = oldPosition.x;
            return true;
        }
        if((this.position.y + WIN_SIZE_Y * 0.5f) > Level.getBounds().height) {
            this.position.y = oldPosition.y;
            return true;
        }
        return false;
    }
}
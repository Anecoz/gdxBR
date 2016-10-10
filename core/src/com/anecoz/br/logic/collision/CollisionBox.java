package com.anecoz.br.logic.collision;

import com.badlogic.gdx.math.Vector2;

// Used for collision checking, holds velocity and bounding box
public class CollisionBox {
    public float x;
    public float y;
    public float w;
    public float h;
    public Vector2 vel;

    public CollisionBox(float x, float y, float w, float h, Vector2 vel) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.vel = vel;
    }
}
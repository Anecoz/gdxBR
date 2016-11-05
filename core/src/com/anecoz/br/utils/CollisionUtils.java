package com.anecoz.br.utils;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class CollisionUtils {

    public static class CollisionBox {
        public float _x, _y, _w, _h;
        public Vector2 _vel;

        public CollisionBox(float x, float y, float w, float h, Vector2 vel) {
            _x = x;
            _y = y;
            _w = w;
            _h = h;
            _vel = vel;
        }
    }

    public static boolean AABBCollision(Rectangle b1, Rectangle b2) {
        return b1.x < b2.x + b2.width &&
                b1.x + b1.width > b2.x &&
                b1.y < b2.y + b2.height &&
                b1.height + b1.y > b2.y;
    }

    // Returns what factor you have to multiply incoming velocity by to get to collision
    public static float sweptAABBCollision(CollisionBox moving, Rectangle b2) {
        float xInvEntry, yInvEntry;
        float xInvExit, yInvExit;

        // find the distance between the objects on the near and far sides for both x and y
        if (moving._vel.x > 0.0f)
        {
            xInvEntry = b2.x - (moving._x + moving._w);
            xInvExit = (b2.x + b2.width) - moving._x;
        }
        else
        {
            xInvEntry = (b2.x + b2.width) - moving._x;
            xInvExit = b2.x - (moving._x + moving._w);
        }
        if (moving._vel.y > 0.0f)
        {
            yInvEntry = b2.y - (moving._y + moving._h);
            yInvExit = (b2.y + b2.height) - moving._y;
        }
        else
        {
            yInvEntry = (b2.y + b2.height) - moving._y;
            yInvExit = b2.y - (moving._y + moving._h);
        }

        // find time of collision and time of leaving for each axis (if statement is to prevent divide by zero)
        float xEntry, yEntry;
        float xExit, yExit;

        if (moving._vel.x == 0.0f)
        {
            xEntry = -999999999.0f;
            xExit = 999999999.0f;
        }
        else
        {
            xEntry = xInvEntry / moving._vel.x;
            xExit = xInvExit / moving._vel.x;
        }

        if (moving._vel.y == 0.0f)
        {
            yEntry = -999999999.0f;
            yExit = 999999999.0f;
        }
        else
        {
            yEntry = yInvEntry / moving._vel.y;
            yExit = yInvExit / moving._vel.y;
        }

        // find the earliest/latest times of collision
        float entryTime = Math.max(xEntry, yEntry);
        float exitTime = Math.min(xExit, yExit);

        // if there was no collision
        if (entryTime > exitTime || xEntry < 0.0f && yEntry < 0.0f || xEntry > 1.0f || yEntry > 1.0f)
        {
            //normal.x = 0.0f;
            //normal.y = 0.0f;
            return 1.0f;
        }
        else // if there was a collision
        {
            // calculate normal of collided surface
            /*if (xEntry > yEntry)
            {
                if (xInvEntry < 0.0f)
                {
                    normalx = 1.0f;
                    normaly = 0.0f;
                }
                else
                {
                    normalx = -1.0f;
                    normaly = 0.0f;
                }
            }
            else
            {
                if (yInvEntry < 0.0f)
                {
                    normalx = 0.0f;
                    normaly = 1.0f;
                }
                else
                {
                    normalx = 0.0f;
                    normaly = -1.0f;
                }
            }*/
            // return the time of collision
            return entryTime;
        }
    }
}

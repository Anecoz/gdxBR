package com.anecoz.br.blueprints;

import com.anecoz.br.components.PositionComponent;
import com.anecoz.br.components.RenderComponent;
import com.anecoz.br.components.TextureComponent;
import com.anecoz.br.components.VelocityComponent;
import com.anecoz.br.components.weapon.BulletComponent;
import com.anecoz.br.components.weapon.ProjectileComponent;
import com.anecoz.br.utils.ResourceHandler;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class BulletProjectileBlueprint implements ProjectileBlueprint {
    public Vector2 _pos;
    public Vector2 _forward;
    public float _rotation;
    private float _speed = 15.f;

    public BulletProjectileBlueprint() {

    }

    public void setData(Vector2 pos, Vector2 forward, float rotation) {
        _pos = pos;
        _forward = forward;
        _rotation = rotation;
    }

    public ArrayList<Component> getComponents() {
        ArrayList<Component> output = new ArrayList<Component>();

        output.add(new PositionComponent(_pos));
        output.add(new VelocityComponent(new Vector2(_forward.x * _speed, _forward.y * _speed)));
        output.add(new TextureComponent(ResourceHandler.BULLET_TEXTURE));
        output.add(new BulletComponent(15));
        output.add(new RenderComponent(_rotation, .3f));
        output.add(new ProjectileComponent());

        return output;
    }
}

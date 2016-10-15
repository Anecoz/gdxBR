package com.anecoz.br.systems;

import com.anecoz.br.*;
import com.anecoz.br.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class InputSystem extends EntitySystem {
    private float _speed = 3.f;
    private OrthographicCamera _cam;

    private ImmutableArray<Entity> _entities;

    private ComponentMapper<VelocityComponent> vc = ComponentMapper.getFor(VelocityComponent.class);
    private ComponentMapper<RenderComponent> rc = ComponentMapper.getFor(RenderComponent.class);
    private ComponentMapper<PositionComponent> pc = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<TextureComponent> tc = ComponentMapper.getFor(TextureComponent.class);

    public InputSystem(OrthographicCamera cam) {
        _cam = cam;
    }

    @Override
    public void addedToEngine(Engine engine) {
        _entities = engine.getEntitiesFor(Family.all(
                PlayerInputComponent.class, // To make sure we only handle player input
                VelocityComponent.class,    // To update the velocity
                RenderComponent.class,      // To set rotation and get scale
                PositionComponent.class,    // To update rotation (needed in the maths)
                TextureComponent.class)     // To get width and height to update rotation (needed in the maths)
                .get());
    }

    @Override
    public void update(float deltaTime) {
        VelocityComponent velComp;
        RenderComponent renComp;
        PositionComponent posComp;
        TextureComponent texComp;

        for (int i = 0; i < _entities.size(); i++) {
            Entity e = _entities.get(i);
            velComp = vc.get(e);
            renComp = rc.get(e);
            posComp = pc.get(e);
            texComp = tc.get(e);

            // Rotation
            float w = renComp._scale * texComp._texture.getWidth() * EntityManager.PIX_TO_WORLD_FACTOR;
            float h = renComp._scale * texComp._texture.getHeight() * EntityManager.PIX_TO_WORLD_FACTOR;
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.input.getY();
            float centerX = posComp._pos.x + w/2.0f;
            float centerY = posComp._pos.y + h/2.0f;
            Vector3 mouse = new Vector3(mouseX, mouseY, 0);
            _cam.unproject(mouse);
            Vector2 tmp = new Vector2(mouse.x - centerX, mouse.y - centerY);
            tmp.nor();
            float angle = tmp.angle();
            renComp._rotation = angle - 90;

            // Crude player input handling
            if (Gdx.input.isKeyPressed(Input.Keys.W))
                velComp._vel.y = _speed;
            else if (Gdx.input.isKeyPressed(Input.Keys.S))
                velComp._vel.y = -_speed;
            else
                velComp._vel.y = 0f;

            if (Gdx.input.isKeyPressed(Input.Keys.A))
                velComp._vel.x = -_speed;
            else if (Gdx.input.isKeyPressed(Input.Keys.D))
                velComp._vel.x = _speed;
            else
                velComp._vel.x = 0f;

            if (noKeysPressed()) {
                velComp._vel.x = 0f;
                velComp._vel.y = 0f;
            }
        }
    }

    private boolean noKeysPressed() {
        return !Gdx.input.isKeyPressed(Input.Keys.W) &&
                !Gdx.input.isKeyPressed(Input.Keys.S) &&
                !Gdx.input.isKeyPressed(Input.Keys.A) &&
                !Gdx.input.isKeyPressed(Input.Keys.D);
    }
}

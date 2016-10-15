package com.anecoz.br.systems;


import com.anecoz.br.components.CameraTargetComponent;
import com.anecoz.br.components.PositionComponent;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class CameraSystem extends EntitySystem {
    public static float WIN_SIZE_X = 20.0f;
    public static float WIN_SIZE_Y = 0.0f;

    private Vector3 _oldPosition;
    private OrthographicCamera _cam;

    private ImmutableArray<Entity> _entities;

    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);

    public CameraSystem(OrthographicCamera cam) {
        float invAr = (float) 600 / (float) 800;  // Has to be changed to variable from config file. WIDTH, HEIGHT
        WIN_SIZE_Y = invAr * WIN_SIZE_X;
        _cam = cam;
    }

    @Override
    public void addedToEngine(Engine engine) {
        _entities = engine.getEntitiesFor(Family.all(CameraTargetComponent.class, PositionComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        PositionComponent posComp;

        // Should usually only be one entity...
        for (int i = 0; i < _entities.size(); i++) {
            Entity e = _entities.get(i);
            posComp = pm.get(e);

            //Creating a vector 3 which represents the target location most likely the player)
            Vector3 target = new Vector3(posComp._pos.x, posComp._pos.y, 0);
            final float speed = 1.0f - deltaTime;
            //The result is roughly: old_position*0.9 + target * 0.1
            Vector3 cameraPosition = _cam.position;
            cameraPosition.scl(speed);
            target.scl(deltaTime);
            cameraPosition.add(target);
            //Clamping position values to map size, needed to prevent jitter caused from delta.
            cameraPosition.x = (float) Math.round(cameraPosition.x * 100) / 100;
            cameraPosition.y = (float) Math.round(cameraPosition.y * 100) / 100;
            _cam.position.set(cameraPosition);
            _oldPosition = _cam.position;
        }
    }
}

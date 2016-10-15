package com.anecoz.br.systems;


import com.anecoz.br.components.PositionComponent;
import com.anecoz.br.components.RenderComponent;
import com.anecoz.br.components.TextureComponent;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EntityRenderSystem extends EntitySystem {
    private ImmutableArray<Entity> _entities;

    private SpriteBatch _sb;
    private OrthographicCamera _cam;
    private float _pixToWorldFactor;

    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<TextureComponent> tm = ComponentMapper.getFor(TextureComponent.class);
    private ComponentMapper<RenderComponent> rm = ComponentMapper.getFor(RenderComponent.class);

    public EntityRenderSystem(SpriteBatch sb, OrthographicCamera cam, float factor) {
        _sb = sb;
        _cam = cam;
        _pixToWorldFactor = factor;
    }

    @Override
    public void addedToEngine(Engine engine) {
        _entities = engine.getEntitiesFor(Family.all(PositionComponent.class, TextureComponent.class, RenderComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        PositionComponent posComponent;
        TextureComponent texComponent;
        RenderComponent renComponent;

        _cam.update();
        _sb.begin();
        _sb.setProjectionMatrix(_cam.combined);

        for (int i = 0; i < _entities.size(); i++) {
            Entity e = _entities.get(i);

            posComponent = pm.get(e);
            texComponent = tm.get(e);
            renComponent = rm.get(e);

            float w = renComponent._scale * texComponent._texture.getWidth() * _pixToWorldFactor;
            float h = renComponent._scale * texComponent._texture.getHeight() * _pixToWorldFactor;

            _sb.draw(texComponent._texture,
                    posComponent._pos.x, posComponent._pos.y,
                    w/2.0f, h/2.0f,
                    w, h,
                    1.0f, 1.0f,
                    renComponent._rotation,
                    0, 0,
                    texComponent._texture.getWidth(), texComponent._texture.getHeight(),
                    false, false);
        }

        _sb.end();
    }
}

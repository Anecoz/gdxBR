package com.anecoz.br.systems;


import com.anecoz.br.*;
import com.anecoz.br.components.PositionComponent;
import com.anecoz.br.components.RenderComponent;
import com.anecoz.br.components.TextureComponent;
import com.anecoz.br.utils.RenderBinComparator;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EntityRenderSystem extends SortedIteratingSystem {
    private SpriteBatch _sb;
    private OrthographicCamera _cam;

    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<TextureComponent> tm = ComponentMapper.getFor(TextureComponent.class);
    private ComponentMapper<RenderComponent> rm = ComponentMapper.getFor(RenderComponent.class);

    public EntityRenderSystem(SpriteBatch sb, OrthographicCamera cam) {
        super(Family.all(PositionComponent.class, TextureComponent.class, RenderComponent.class).get(), new RenderBinComparator());
        _sb = sb;
        _cam = cam;
    }

    @Override
    protected void processEntity(Entity e, float deltaTime) {
        PositionComponent posComponent;
        TextureComponent texComponent;
        RenderComponent renComponent;

        posComponent = pm.get(e);
        texComponent = tm.get(e);
        renComponent = rm.get(e);

        float w = renComponent._scale * texComponent._texture.getWidth() * EntityManager.PIX_TO_WORLD_FACTOR;
        float h = renComponent._scale * texComponent._texture.getHeight() * EntityManager.PIX_TO_WORLD_FACTOR;

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

    // Override update method so that we don't have to call cam.update and sb.begin each entity update
    @Override
    public void update(float deltaTime) {
        _cam.update(); //< Want to do this only once
        _sb.begin();   //< Ditto
        _sb.setProjectionMatrix(_cam.combined); //< Ditto
        super.update(deltaTime); //< this will call processEntity above for each entity (but sorted!)
        _sb.end(); //< end, cause we only want to do it once
    }
}
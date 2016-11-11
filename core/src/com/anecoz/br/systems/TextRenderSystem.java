package com.anecoz.br.systems;


import com.anecoz.br.*;
import com.anecoz.br.components.HealthComponent;
import com.anecoz.br.components.PositionComponent;
import com.anecoz.br.components.TextComponent;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class TextRenderSystem extends EntitySystem {
    private ImmutableArray<Entity> _entities;
    private ImmutableArray<Entity> _healthEntities;
    private BitmapFont _font;
    private SpriteBatch _sb;
    private OrthographicCamera _cam;

    private ComponentMapper<TextComponent> tm = ComponentMapper.getFor(TextComponent.class);
    private ComponentMapper<HealthComponent> hm = ComponentMapper.getFor(HealthComponent.class);
    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);

    public TextRenderSystem(SpriteBatch sb, OrthographicCamera cam) {
        //_sb = sb;
        //_cam = cam;
        _sb = new SpriteBatch();
        _cam = new OrthographicCamera();
        _cam.setToOrtho(false, 1280, 720);
        _font = new BitmapFont(Gdx.files.internal("fonts/calibri.fnt"), Gdx.files.internal("fonts/calibri.png"), false);
        _font.getData().setScale(0.5f);
    }

    @Override
    public void addedToEngine(Engine engine) {
        _entities = engine.getEntitiesFor(Family.all(TextComponent.class).get());
        _healthEntities = engine.getEntitiesFor(Family.all(HealthComponent.class, PositionComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        TextComponent textComp;

        _cam.update();
        _sb.setProjectionMatrix(_cam.combined);
        _sb.begin();
        for (int i = 0; i < _entities.size(); i++) {
            Entity e = _entities.get(i);
            textComp = tm.get(e);

            _font.draw(_sb, textComp._text, textComp._pos.x, textComp._pos.y);
        }
        // Draw all health text
        for (Entity e : _healthEntities) {
            HealthComponent healthComp = hm.get(e);
            PositionComponent posComp = pm.get(e);

            String text = Float.toString(healthComp._health);
            Vector3 projected = EntityManager.getWorldCam().project(new Vector3(posComp._pos.x, posComp._pos.y, 0f));
            _font.draw(_sb, text, projected.x, projected.y + 100);
        }
        _sb.end();
    }
}
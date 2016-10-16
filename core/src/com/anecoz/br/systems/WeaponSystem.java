package com.anecoz.br.systems;

import com.anecoz.br.blueprints.ProjectileBlueprint;
import com.anecoz.br.components.*;
import com.anecoz.br.components.weapon.ShootingComponent;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

public class WeaponSystem extends EntitySystem {
    private ImmutableArray<Entity> _entities;
    private ImmutableArray<Entity> _inputEntities;
    private ImmutableArray<Entity> _playerEntities;

    private Engine _engine;

    private ComponentMapper<ShootingComponent> sm = ComponentMapper.getFor(ShootingComponent.class);
    private ComponentMapper<PlayerInputComponent> im = ComponentMapper.getFor(PlayerInputComponent.class);
    private ComponentMapper<TimerComponent> tm = ComponentMapper.getFor(TimerComponent.class);
    private ComponentMapper<PickedUpComponent> pm = ComponentMapper.getFor(PickedUpComponent.class);
    private ComponentMapper<TextComponent> textMap = ComponentMapper.getFor(TextComponent.class);
    private ComponentMapper<ProjectileFactoryComponent> fm = ComponentMapper.getFor(ProjectileFactoryComponent.class);
    private ComponentMapper<PositionComponent> posMapper = ComponentMapper.getFor(PositionComponent.class);

    public WeaponSystem() {

    }

    @Override
    public void addedToEngine(Engine engine) {
        _entities = engine.getEntitiesFor(Family.all(
                ShootingComponent.class,
                PickedUpComponent.class,
                TimerComponent.class,
                ProjectileFactoryComponent.class,
                TextComponent.class)
                .get());

        _inputEntities = engine.getEntitiesFor(Family.all(PlayerInputComponent.class).get());
        _playerEntities = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
        _engine = engine; //< for adding projectile entities that the factory creates
    }

    @Override
    public void update(float deltaTime) {
        Entity inputEntity = _inputEntities.first();
        PlayerInputComponent inputComponent = im.get(inputEntity);
        Entity playerEntity = _playerEntities.first();

        PositionComponent playerPosComp = posMapper.get(playerEntity);

        if (!inputComponent._isHoldingShootButton && !inputComponent._hasClickedShootButton)
            return;

        ShootingComponent shootComp;
        TimerComponent timerComp;
        PickedUpComponent pickUpComp;
        ProjectileFactoryComponent facComponent;
        TextComponent textComp;

        for (int i = 0; i < _entities.size(); i++) {
            Entity e = _entities.get(i);

            shootComp = sm.get(e);
            timerComp = tm.get(e);
            pickUpComp = pm.get(e);
            facComponent = fm.get(e);
            textComp = textMap.get(e);

            if (!pickUpComp._inHands)
                continue;

            if (shootComp._ammunitionCount <= 0)
                continue;

            if (shootComp._isAutomatic) {
                if (inputComponent._isHoldingShootButton) {
                    shoot(inputComponent, playerPosComp, timerComp, shootComp, facComponent);
                }
            }
            else {
                if (inputComponent._hasClickedShootButton) {
                    shoot(inputComponent, playerPosComp, timerComp, shootComp, facComponent);
                }
            }

            textComp._text = "Ammo: " + Integer.toString(shootComp._ammunitionCount);
        }
    }

    private void shoot(PlayerInputComponent inpComp,
                       PositionComponent playerPosComp,
                       TimerComponent timerComp,
                       ShootingComponent shootComp,
                       ProjectileFactoryComponent facComp) {
        if (timerComp._millisSinceLastActivation == -1 ||
                (TimeUtils.millis()) - timerComp._millisSinceLastActivation >= (long)1000/((float)timerComp._frequency/60)) {
            ProjectileBlueprint blueprint = facComp._blueprint;
            blueprint.setData(playerPosComp._centerPos, inpComp._forward, inpComp._rotation);
            ArrayList<Component> compList = blueprint.getComponents();

            Entity projectile = new Entity();
            for (Component comp : compList) {
                projectile.add(comp);
            }
            _engine.addEntity(projectile);

            shootComp._ammunitionCount--;
            timerComp._millisSinceLastActivation = TimeUtils.millis();
        }
    }
}
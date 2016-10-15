package com.anecoz.br.systems;

import com.anecoz.br.blueprints.ProjectileBlueprint;
import com.anecoz.br.components.ProjectileFactoryComponent;
import com.anecoz.br.components.PickedUpComponent;
import com.anecoz.br.components.PlayerInputComponent;
import com.anecoz.br.components.TimerComponent;
import com.anecoz.br.components.weapon.ShootingComponent;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

public class WeaponSystem extends EntitySystem {
    private ImmutableArray<Entity> _entities;
    private ImmutableArray<Entity> _inputEntities;

    private Engine _engine;

    private ComponentMapper<ShootingComponent> sm = ComponentMapper.getFor(ShootingComponent.class);
    private ComponentMapper<PlayerInputComponent> im = ComponentMapper.getFor(PlayerInputComponent.class);
    private ComponentMapper<TimerComponent> tm = ComponentMapper.getFor(TimerComponent.class);
    private ComponentMapper<PickedUpComponent> pm = ComponentMapper.getFor(PickedUpComponent.class);
    private ComponentMapper<ProjectileFactoryComponent> fm = ComponentMapper.getFor(ProjectileFactoryComponent.class);

    public WeaponSystem() {

    }

    @Override
    public void addedToEngine(Engine engine) {
        _entities = engine.getEntitiesFor(Family.all(
                ShootingComponent.class,
                PickedUpComponent.class,
                TimerComponent.class,
                ProjectileFactoryComponent.class)
                .get());

        _inputEntities = engine.getEntitiesFor(Family.all(PlayerInputComponent.class).get());
        _engine = engine; //< for adding bullets
    }

    @Override
    public void update(float deltaTime) {
        Entity inputEntity = _inputEntities.first();
        PlayerInputComponent inputComponent = im.get(inputEntity);

        if (!inputComponent._isHoldingShootButton && !inputComponent._hasClickedShootButton)
            return;

        ShootingComponent shootComp;
        TimerComponent timerComp;
        PickedUpComponent pickUpComp;
        ProjectileFactoryComponent facComponent;

        for (int i = 0; i < _entities.size(); i++) {
            Entity e = _entities.get(i);

            shootComp = sm.get(e);
            timerComp = tm.get(e);
            pickUpComp = pm.get(e);
            facComponent = fm.get(e);

            if (pickUpComp._inInventory)
                continue;

            if (shootComp._ammunitionCount <= 0)
                continue;

            if (shootComp._isAutomatic) {
                if (inputComponent._isHoldingShootButton) {
                    shoot(inputComponent, timerComp, shootComp, facComponent);
                }
            }
            else {
                if (inputComponent._hasClickedShootButton) {
                    shoot(inputComponent, timerComp, shootComp, facComponent);
                }
            }
        }
    }

    private void shoot(PlayerInputComponent inpComp, TimerComponent timerComp, ShootingComponent shootComp, ProjectileFactoryComponent facComp) {
        if (timerComp._millisSinceLastActivation == -1 ||
                (TimeUtils.millis()) - timerComp._millisSinceLastActivation >= (long)1000/((float)timerComp._frequency/60)) {
            System.out.println("Shooting with " + shootComp._ammunitionCount + " bullets left!!");

            ProjectileBlueprint blueprint = facComp._blueprint;
            blueprint.setData(inpComp._pos, inpComp._forward);
            ArrayList<Component> compList = blueprint.getComponents();

            Entity bullet = new Entity();
            for (Component comp : compList) {
                bullet.add(comp);
            }
            _engine.addEntity(bullet);

            shootComp._ammunitionCount--;
            timerComp._millisSinceLastActivation = TimeUtils.millis();
        }
    }
}
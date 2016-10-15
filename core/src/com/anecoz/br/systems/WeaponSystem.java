package com.anecoz.br.systems;

import com.anecoz.br.components.FactoryComponent;
import com.anecoz.br.components.PickedUpComponent;
import com.anecoz.br.components.PlayerInputComponent;
import com.anecoz.br.components.TimerComponent;
import com.anecoz.br.components.weapon.ShootingComponent;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.TimeUtils;

public class WeaponSystem extends EntitySystem {
    private ImmutableArray<Entity> _entities;
    private ImmutableArray<Entity> _inputEntities;

    private ComponentMapper<ShootingComponent> sm = ComponentMapper.getFor(ShootingComponent.class);
    private ComponentMapper<PlayerInputComponent> im = ComponentMapper.getFor(PlayerInputComponent.class);
    private ComponentMapper<TimerComponent> tm = ComponentMapper.getFor(TimerComponent.class);
    private ComponentMapper<PickedUpComponent> pm = ComponentMapper.getFor(PickedUpComponent.class);

    public WeaponSystem() {

    }

    @Override
    public void addedToEngine(Engine engine) {
        _entities = engine.getEntitiesFor(Family.all(
                ShootingComponent.class,
                PickedUpComponent.class,
                TimerComponent.class,
                FactoryComponent.class)
                .get());

        _inputEntities = engine.getEntitiesFor(Family.all(PlayerInputComponent.class).get());
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

        for (int i = 0; i < _entities.size(); i++) {
            Entity e = _entities.get(i);

            shootComp = sm.get(e);
            timerComp = tm.get(e);
            pickUpComp = pm.get(e);

            if (pickUpComp._inInventory)
                continue;

            if (shootComp._ammunitionCount <= 0)
                continue;

            if (shootComp._isAutomatic) {
                if (inputComponent._isHoldingShootButton) {
                    shoot(timerComp, shootComp);
                }
            }
            else {
                if (inputComponent._hasClickedShootButton) {
                    shoot(timerComp, shootComp);
                }
            }
        }
    }

    private void shoot(TimerComponent timerComp, ShootingComponent shootComp) {
        if (timerComp._millisSinceLastActivation == -1 ||
                (TimeUtils.millis()) - timerComp._millisSinceLastActivation >= (long)1000/((float)timerComp._frequency/60)) {
            System.out.println("Shooting with " + shootComp._ammunitionCount + " bullets left!!");
            shootComp._ammunitionCount--;
            timerComp._millisSinceLastActivation = TimeUtils.millis();
        }
    }
}
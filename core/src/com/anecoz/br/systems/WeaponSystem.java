package com.anecoz.br.systems;

import com.anecoz.br.blueprints.ProjectileBlueprint;
import com.anecoz.br.components.*;
import com.anecoz.br.components.weapon.ReloadTimerComponent;
import com.anecoz.br.components.weapon.RpmTimerComponent;
import com.anecoz.br.components.weapon.ShootingComponent;
import com.anecoz.br.network.client.ClientSender;
import com.anecoz.br.utils.TimerUtils;
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
    private ComponentMapper<RpmTimerComponent> tm = ComponentMapper.getFor(RpmTimerComponent.class);
    private ComponentMapper<ReloadTimerComponent> rm = ComponentMapper.getFor(ReloadTimerComponent.class);
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
                RpmTimerComponent.class,
                ReloadTimerComponent.class,
                ProjectileFactoryComponent.class,
                TextComponent.class)
                .get());

        _inputEntities = engine.getEntitiesFor(Family.all(PlayerInputComponent.class).get());
        _playerEntities = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
        _engine = engine; //< for adding projectile entities that the factory creates
    }

    @Override
    public void update(float deltaTime) {
        if (_inputEntities.size() > 0) {
            Entity inputEntity = _inputEntities.first();
            PlayerInputComponent inputComponent = im.get(inputEntity);
            Entity playerEntity = _playerEntities.first();

            PositionComponent playerPosComp = posMapper.get(playerEntity);

            ShootingComponent shootComp;
            RpmTimerComponent timerComp;
            ReloadTimerComponent reloadComp;
            PickedUpComponent pickUpComp;
            ProjectileFactoryComponent facComponent;
            TextComponent textComp;

            for (int i = 0; i < _entities.size(); i++) {
                Entity e = _entities.get(i);

                shootComp = sm.get(e);
                timerComp = tm.get(e);
                reloadComp = rm.get(e);
                pickUpComp = pm.get(e);
                facComponent = fm.get(e);
                textComp = textMap.get(e);

                if (!pickUpComp._inHands)
                    continue;

                if (!reloadComp._isReloading) {
                    if (shootComp._isAutomatic) {
                        if (inputComponent._isHoldingMouseButton) {
                            shoot(inputComponent, playerPosComp, timerComp, shootComp, facComponent);
                        }
                    }
                    else {
                        if (inputComponent._hasClickedMouseButton) {
                            shoot(inputComponent, playerPosComp, timerComp, shootComp, facComponent);
                        }
                    }
                }

                // Reload
                if (reloadComp._isReloading) {
                    updateReloadStatus(reloadComp, shootComp);
                }
                else if (inputComponent._hasClickedReloadButton) {
                    startReloading(reloadComp, shootComp);
                }

                textComp._text = "Ammo: " + Integer.toString(shootComp._currentMagAmmo) + "/" + Integer.toString(shootComp._ammunitionCount - shootComp._currentMagAmmo);
            }
        }
    }

    private void shoot(PlayerInputComponent inpComp,
                       PositionComponent playerPosComp,
                       RpmTimerComponent timerComp,
                       ShootingComponent shootComp,
                       ProjectileFactoryComponent facComp) {
        if (TimerUtils.timerIsUp(timerComp) && shootComp._currentMagAmmo > 0) {
            ProjectileBlueprint blueprint = facComp._blueprint;
            blueprint.setData(playerPosComp._centerPos.add(inpComp._forward), inpComp._forward, inpComp._rotation);
            ArrayList<Component> compList = blueprint.getComponents();

            Entity projectile = new Entity();
            for (Component comp : compList) {
                projectile.add(comp);
            }
            _engine.addEntity(projectile);

            shootComp._currentMagAmmo--;
            shootComp._ammunitionCount--;
            TimerUtils.updateTimer(timerComp);

            // Tell network we shot
            ClientSender.spawnProjectile(blueprint);
        }
    }

    private void startReloading(ReloadTimerComponent reloadComp, ShootingComponent shootComp) {
        if (shootComp._ammunitionCount > 0) {
            if (TimerUtils.timerIsReset(reloadComp)) {
                TimerUtils.updateTimer(reloadComp);
                reloadComp._isReloading = true;
            }
        }
    }

    private void updateReloadStatus(ReloadTimerComponent reloadComp,
                                    ShootingComponent shootComp) {
        // Can we even reload?
        if (shootComp._ammunitionCount > 0) {
            if (TimerUtils.timerIsUp(reloadComp)) {
                // Actual reload
                if (shootComp._ammunitionCount > shootComp._magazineSize) {
                    shootComp._currentMagAmmo = shootComp._magazineSize;
                }
                else {
                    shootComp._currentMagAmmo = shootComp._ammunitionCount;
                }
                TimerUtils.resetTimer(reloadComp);
                reloadComp._isReloading = false;
            }
        }
    }
}
package com.anecoz.br.blueprints;

import com.anecoz.br.components.*;
import com.anecoz.br.components.weapon.ReloadTimerComponent;
import com.anecoz.br.components.weapon.RpmTimerComponent;
import com.anecoz.br.components.weapon.ShootingComponent;
import com.anecoz.br.network.shared.SharedNetwork;
import com.anecoz.br.utils.ResourceHandler;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class RifleWeaponBlueprint implements WeaponBlueprint {
    private Vector2 _pos;
    private int _ammo;

    public RifleWeaponBlueprint() {

    }

    public void setData(Vector2 pos, int ammo) {
        _pos = pos;
        _ammo = ammo;
    }

    public Vector2 getPos() {
        return _pos;
    }

    public SharedNetwork.WEAPON_TYPE getType() {
        return SharedNetwork.WEAPON_TYPE.RIFLE;
    }

    public ArrayList<Component> getComponents() {
        ArrayList<Component> output = new ArrayList<Component>();

        int magCount = 40;
        if (_ammo < 40)
            magCount = _ammo;
        output.add(new ShootingComponent(_ammo, 40, magCount, true));
        output.add(new RpmTimerComponent(500));
        output.add(new ReloadTimerComponent(60));
        output.add(new ProjectileFactoryComponent(new BulletProjectileBlueprint()));
        output.add(new PositionComponent(_pos));
        output.add(new TextureComponent(ResourceHandler.RIFLE_TEXTURE));
        output.add(new RenderComponent(0f, 0.03125f, -1));
        output.add(new TextComponent("", new Vector2(10, 30)));
        output.add(new VisibilityComponent());
        output.add(new BoundingBoxComponent(_pos));

        return output;
    }
}

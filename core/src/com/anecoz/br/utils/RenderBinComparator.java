package com.anecoz.br.utils;

import com.anecoz.br.components.RenderComponent;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

import java.util.Comparator;

public class RenderBinComparator implements Comparator<Entity> {
    private ComponentMapper<RenderComponent> cm = ComponentMapper.getFor(RenderComponent.class);

    @Override
    public int compare(Entity e1, Entity e2) {
        if (cm.get(e1)._bin < cm.get(e2)._bin)
            return -1;
        else if (cm.get(e1)._bin == cm.get(e2)._bin)
            return 0;
        else
            return 1;
    }
}

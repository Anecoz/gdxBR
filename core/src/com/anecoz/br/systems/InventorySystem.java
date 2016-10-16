package com.anecoz.br.systems;


import com.anecoz.br.*;
import com.anecoz.br.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class InventorySystem extends EntitySystem{
    private OrthographicCamera _cam;

    private ImmutableArray<Entity> _invEntity;
    private ImmutableArray<Entity> _itemEntities;

    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<RenderComponent> rm = ComponentMapper.getFor(RenderComponent.class);
    private ComponentMapper<TextureComponent> tm = ComponentMapper.getFor(TextureComponent.class);
    private ComponentMapper<PickedUpComponent> pim = ComponentMapper.getFor(PickedUpComponent.class);

    public InventorySystem(OrthographicCamera camera){
        _cam = camera;
    }

    @Override
    public void addedToEngine(Engine engine) {
        _invEntity = engine.getEntitiesFor(Family.all(
                VisibilityComponent.class,
                InventoryComponent.class,
                TextureComponent.class,
                PositionComponent.class,
                RenderComponent.class).get());

        _itemEntities = engine.getEntitiesFor(Family.all(
                PickedUpComponent.class,
                VisibilityComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        PositionComponent posCompInvItems;
        RenderComponent rendCompItems;
        TextureComponent textCompItems;
        PickedUpComponent pickedComp;
        PositionComponent posCompInv;
        RenderComponent rendCompInv;
        TextureComponent textCompInv;

        Vector2 invOffset = new Vector2(3.5f, -2f);
        float x, y;
        int counterX = 0;
        int counterY = 0;

        // Inventory
        for (int i = 0; i < _invEntity.size(); i++) {
            Entity inv = _invEntity.get(i);
            posCompInv = pm.get(inv);
            rendCompInv = rm.get(inv);
            textCompInv = tm.get(inv);

            posCompInv._pos.set(_cam.position.x + invOffset.x, _cam.position.y + invOffset.y);

            int maxItemsPerRow = (int)(rendCompInv._scale * textCompInv._texture.getWidth()) / (int)(1f/EntityManager.PIX_TO_WORLD_FACTOR);

            // Items in inventory
            for (int j = 0; j < _itemEntities.size(); j++) {
                Entity item = _itemEntities.get(j);
                posCompInvItems = pm.get(item);
                rendCompItems = rm.get(item);
                textCompItems = tm.get(item);
                pickedComp = pim.get(item);

                if(counterX < maxItemsPerRow) {
                    x = posCompInv._pos.x + (rendCompItems._scale * textCompItems._texture.getWidth() * EntityManager.PIX_TO_WORLD_FACTOR) * counterX;
                    y = posCompInv._pos.y + (rendCompItems._scale * textCompItems._texture.getHeight() * EntityManager.PIX_TO_WORLD_FACTOR) * counterY;
                    posCompInvItems._pos.set(x, y);
                    pickedComp._inventorySlot.x = counterX;
                    pickedComp._inventorySlot.y = counterY;
                    counterX++;
                } else {
                    counterY++;
                    counterX = 0;
                    x = posCompInv._pos.x + (rendCompItems._scale * textCompItems._texture.getWidth() * EntityManager.PIX_TO_WORLD_FACTOR) * counterX;
                    y = posCompInv._pos.y + (rendCompItems._scale * textCompItems._texture.getHeight() * EntityManager.PIX_TO_WORLD_FACTOR) * counterY;
                    posCompInvItems._pos.set(x, y);
                    pickedComp._inventorySlot.x = counterX;
                    pickedComp._inventorySlot.y = counterY;
                }


            }
        }
    }
}

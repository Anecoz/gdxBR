package com.anecoz.br.systems;


import com.anecoz.br.*;
import com.anecoz.br.components.*;
import com.anecoz.br.utils.ResourceHandler;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class InventorySystem extends EntitySystem{
    private OrthographicCamera _cam;
    private Entity _selectedEntity;
    private PlayerInputComponent _playerInputComponent;

    private ImmutableArray<Entity> _inputEntities;
    private ImmutableArray<Entity> _invEntity;
    private ImmutableArray<Entity> _itemEntities;
    private ImmutableArray<Entity> _boundingBoxEntities;

    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<RenderComponent> rm = ComponentMapper.getFor(RenderComponent.class);
    private ComponentMapper<TextureComponent> tm = ComponentMapper.getFor(TextureComponent.class);
    private ComponentMapper<PickedUpComponent> pum = ComponentMapper.getFor(PickedUpComponent.class);
    private ComponentMapper<BoundingBoxComponent> bbm = ComponentMapper.getFor(BoundingBoxComponent.class);
    private ComponentMapper<VisibilityComponent> vm = ComponentMapper.getFor(VisibilityComponent.class);
    private ComponentMapper<PlayerInputComponent> pim = ComponentMapper.getFor(PlayerInputComponent.class);

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

        _boundingBoxEntities = engine.getEntitiesFor(Family.all(
                BoundingBoxComponent.class).get());

        _inputEntities = engine.getEntitiesFor(Family.all(PlayerInputComponent.class).get());
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
        BoundingBoxComponent boxComponent;

        Vector2 invOffset = new Vector2(3.5f, -2f);
        float x, y;
        int counterX = 0;
        int counterY = 0;

        // Get PlayerInputComponent
        if (_inputEntities.size() > 0) {
            Entity inputEntity = _inputEntities.first();
            _playerInputComponent = pim.get(inputEntity);
        }

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
                pickedComp = pum.get(item);
                boxComponent = bbm.get(item);

                if(counterX < maxItemsPerRow) {
                    x = posCompInv._pos.x + (rendCompItems._scale * textCompItems._texture.getWidth() * EntityManager.PIX_TO_WORLD_FACTOR) * counterX;
                    y = posCompInv._pos.y + (rendCompItems._scale * textCompItems._texture.getHeight() * EntityManager.PIX_TO_WORLD_FACTOR) * counterY;
                    posCompInvItems._pos.set(x, y);
                    boxComponent._boundingBox.setPosition(x, y);
                    pickedComp._inventorySlot.x = counterX;
                    pickedComp._inventorySlot.y = counterY;
                    counterX++;
                } else {
                    counterY++;
                    counterX = 0;
                    x = posCompInv._pos.x + (rendCompItems._scale * textCompItems._texture.getWidth() * EntityManager.PIX_TO_WORLD_FACTOR) * counterX;
                    y = posCompInv._pos.y + (rendCompItems._scale * textCompItems._texture.getHeight() * EntityManager.PIX_TO_WORLD_FACTOR) * counterY;
                    posCompInvItems._pos.set(x, y);
                    boxComponent._boundingBox.setPosition(x, y);
                    pickedComp._inventorySlot.x = counterX;
                    pickedComp._inventorySlot.y = counterY;
                }
            }
        }
        updateEquipment();
    }

    private void updateEquipment() {
        dragAndDrop();
    }

    private void dragAndDrop() {
        PickedUpComponent pickedUpComponent;
        PositionComponent positionComponent;
        BoundingBoxComponent boxComponent;
        Vector2 oldPosition;
        if(insideBoundingBox() && vm.has(_selectedEntity)) {
            positionComponent = pm.get(_selectedEntity);
            boxComponent = bbm.get(_selectedEntity);
            oldPosition = new Vector2(positionComponent._pos);
            Vector3 mPos = EntityManager.getWorldCam().unproject(new Vector3(_playerInputComponent._currentMousePosition.x,
                    _playerInputComponent._currentMousePosition.y, 0));
            if(_playerInputComponent._isHoldingMouseButton) {
                //drag
                positionComponent._pos.set(mPos.x, mPos.y);
                boxComponent._boundingBox.setPosition(mPos.x, mPos.y);
            }
            else {
                //reset
                positionComponent._pos.set(oldPosition);
                boxComponent._boundingBox.setPosition(oldPosition);
            }

            if(Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                removeEquippedWeapon();
                pickedUpComponent = pum.get(_selectedEntity);
                pickedUpComponent._inHands = true;
            }
        }
    }

    private boolean insideBoundingBox() {
        BoundingBoxComponent boxComponent;

        for (int i = 0; i < _boundingBoxEntities.size(); i++) {
            Entity e = _boundingBoxEntities.get(i);
            boxComponent = bbm.get(e);
            Vector3 mPos = EntityManager.getWorldCam().unproject(new Vector3(_playerInputComponent._currentMousePosition.x,
                    _playerInputComponent._currentMousePosition.y, 0));
            if (boxComponent._boundingBox.contains(mPos.x, mPos.y)) {
                _selectedEntity = e;
                return true;
            }
        }
        _selectedEntity = null;
        return false;
    }

    private void removeEquippedWeapon() {
        PickedUpComponent pUpComponent;
        for (int i = 0; i < _itemEntities.size(); i++) {
            Entity item = _itemEntities.get(i);
            pUpComponent = pum.get(item);
            pUpComponent._inHands = false;
        }
    }
}

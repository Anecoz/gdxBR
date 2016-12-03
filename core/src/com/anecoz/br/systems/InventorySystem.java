package com.anecoz.br.systems;


import com.anecoz.br.*;
import com.anecoz.br.components.*;
import com.anecoz.br.components.weapon.ShootingComponent;
import com.anecoz.br.network.client.ClientSender;
import com.anecoz.br.network.shared.SharedNetwork;
import com.anecoz.br.utils.ResourceHandler;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class InventorySystem extends EntitySystem{
    private OrthographicCamera _cam;
    private Entity _selectedEntity;
    private PlayerInputComponent _playerInputComponent;

    private final int SLOT_SIZE = 128; // Size of a slot in pixels (to scale images properly)
    private final int INV_SIZE = 4; // Actually 4x4

    private ImmutableArray<Entity> _inputEntities;
    private ImmutableArray<Entity> _invEntity;
    private ImmutableArray<Entity> _itemEntities;
    private ImmutableArray<Entity> _boundingBoxEntities;
    private ImmutableArray<Entity> _playerEntities;

    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<RenderComponent> rm = ComponentMapper.getFor(RenderComponent.class);
    private ComponentMapper<TextureComponent> tm = ComponentMapper.getFor(TextureComponent.class);
    private ComponentMapper<PickedUpComponent> pum = ComponentMapper.getFor(PickedUpComponent.class);
    private ComponentMapper<BoundingBoxComponent> bbm = ComponentMapper.getFor(BoundingBoxComponent.class);
    private ComponentMapper<VisibilityComponent> vm = ComponentMapper.getFor(VisibilityComponent.class);
    private ComponentMapper<PlayerInputComponent> pim = ComponentMapper.getFor(PlayerInputComponent.class);
    private ComponentMapper<ShootingComponent> sm = ComponentMapper.getFor(ShootingComponent.class);

    public InventorySystem(OrthographicCamera camera){
        priority = 9; // Do this before weapon system
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
        _playerEntities = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
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

        // Get PlayerInputComponent
        if (_inputEntities.size() > 0) {
            Entity inputEntity = _inputEntities.first();
            _playerInputComponent = pim.get(inputEntity);
        }

        // Inventory
        Vector2 invOffset = new Vector2(0f, 0f);
        for (int i = 0; i < _invEntity.size(); i++) {
            Entity inv = _invEntity.get(i);
            posCompInv = pm.get(inv);
            rendCompInv = rm.get(inv);
            textCompInv = tm.get(inv);

            posCompInv._pos.set(_cam.position.x + invOffset.x, _cam.position.y + invOffset.y);

            // Put items render comps in correct slot
            for (Entity invItem : _itemEntities)
            {
                posCompInvItems = pm.get(invItem);
                pickedComp = pum.get(invItem);
                if (pickedComp._isDragging) {
                    updateDraggedItem(invItem, pickedComp, posCompInvItems);
                    continue;
                }

                boxComponent = bbm.get(invItem);
                float adjustedSlotSize = (float)SLOT_SIZE * rendCompInv._scale;
                rendCompItems = rm.get(invItem);
                textCompItems = tm.get(invItem);
                float scale = adjustedSlotSize/(float)textCompItems._texture.getHeight();
                if (textCompItems._texture.getHeight() < adjustedSlotSize) {
                    scale = 1f/scale;
                }
                rendCompItems._scale = scale;

                int slotY = (int)Math.floor((double)pickedComp._inventorySlot / (double)INV_SIZE);
                int slotX = pickedComp._inventorySlot - INV_SIZE*slotY;
                posCompInvItems._pos.x = slotX * adjustedSlotSize * EntityManager.PIX_TO_WORLD_FACTOR + posCompInv._pos.x;
                posCompInvItems._pos.y = slotY * adjustedSlotSize * EntityManager.PIX_TO_WORLD_FACTOR + posCompInv._pos.y;
                boxComponent._boundingBox.setPosition(posCompInvItems._pos.x, posCompInvItems._pos.y);
            }

            updateEquipment();
        }
    }

    private void updateEquipment() {
        dragAndDrop();
    }

    private void updateDraggedItem(Entity item, PickedUpComponent pickComp, PositionComponent posComp) {
        if (!_playerInputComponent._isHoldingMouseButton) {
            if (_playerInputComponent._isDraggingItem) {
                if (!insideInventory()) {
                    // Drop at player's feet
                    Entity player = _playerEntities.first();
                    PositionComponent playerPos = pm.get(player);
                    RenderComponent renComp = rm.get(item);
                    ShootingComponent shootComp = sm.get(item);
                    BoundingBoxComponent bboxComp = bbm.get(item);
                    renComp._bin = -1;
                    posComp._pos.x = playerPos._pos.x;
                    posComp._pos.y = playerPos._pos.y;
                    bboxComp._boundingBox.setPosition(new Vector2(playerPos._pos));
                    item.remove(PickedUpComponent.class);
                    ClientSender.dropItem(new Vector2(playerPos._pos.x, playerPos._pos.y), shootComp._ammunitionCount, SharedNetwork.WEAPON_TYPE.RIFLE);
                }
            }
            pickComp._isDragging = false;
            _playerInputComponent._isDraggingItem = false;
        }
        else {
            Vector3 mPos = EntityManager.getWorldCam().unproject(new Vector3(_playerInputComponent._currentMousePosition.x,
                    _playerInputComponent._currentMousePosition.y, 0));
            posComp._pos.x = mPos.x;
            posComp._pos.y = mPos.y;
        }
    }

    private void dragAndDrop() {
        if (_playerInputComponent._isDraggingItem)
            return;
        PickedUpComponent pickedUpComponent;
        if (insideInventory() && insideBoundingBox() && vm.has(_selectedEntity)) {
            if (_playerInputComponent._isHoldingMouseButton) {
                pickedUpComponent = pum.get(_selectedEntity);
                pickedUpComponent._isDragging = true;
                _playerInputComponent._isDraggingItem = true;
            }
        }
    }

    private boolean insideInventory() {
        Entity inventory = _invEntity.first();
        PositionComponent posComp = pm.get(inventory);
        RenderComponent renComp = rm.get(inventory);
        TextureComponent texComp = tm.get(inventory);

        Rectangle bbox = new Rectangle();
        bbox.setPosition(posComp._pos.x, posComp._pos.y);
        bbox.setHeight(texComp._texture.getHeight() * renComp._scale * EntityManager.PIX_TO_WORLD_FACTOR);
        bbox.setWidth(texComp._texture.getWidth() * renComp._scale * EntityManager.PIX_TO_WORLD_FACTOR);
        Vector3 mPos = EntityManager.getWorldCam().unproject(new Vector3(_playerInputComponent._currentMousePosition.x,
                _playerInputComponent._currentMousePosition.y, 0));
        return bbox.contains(mPos.x, mPos.y);

    }

    private boolean insideBoundingBox() {
        BoundingBoxComponent boxComponent;

        for (int i = 0; i < _boundingBoxEntities.size(); i++) {
            Entity e = _boundingBoxEntities.get(i);
            if (!bbm.has(e))
                continue;
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

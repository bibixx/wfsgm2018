package com.mygdx.game;

import com.badlogic.gdx.utils.ArrayMap;

public class EntityContainer {

    private ArrayMap<String, Entity> entityContainer;

    EntityContainer(){
        entityContainer = new ArrayMap<String, Entity>();
    }

    void addEntity(String entityName, Entity entity){
        entity.getBody().setUserData(entityName);
        entityContainer.put(entityName, entity);
    }

    Entity getEntity(String entityName){
        return entityContainer.get(entityName);
    }

    ArrayMap.Keys<String> getKeys() {
        return entityContainer.keys();
    }

    ArrayMap.Values<Entity> getValues() {
        return entityContainer.values();
    }
}

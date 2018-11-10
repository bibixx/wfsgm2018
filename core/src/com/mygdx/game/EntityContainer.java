package com.mygdx.game;

import com.badlogic.gdx.utils.ArrayMap;

public class EntityContainer {

    private ArrayMap<String, Entity> entityContainer;

    EntityContainer(){
        entityContainer = new ArrayMap<String, Entity>();
    }

    void addEntity(String entityName, Entity entity){
        entityContainer.put(entityName, entity);
    }

    Entity getEntity(String entityName){
        return entityContainer.get(entityName);
    }
}

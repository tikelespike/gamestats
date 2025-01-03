package com.tikelespike.gamestats.businesslogic.entities;

/**
 * Should be implemented by all classes that have instances registered in the system (especially if persisted in the
 * data layer).
 */
public interface HasId {

    /**
     * Returns the unique identifier of this object. The identifer is used to identify this instance within the system.
     *
     * @return the unique identifier of this object.
     */
    Long getId();
}

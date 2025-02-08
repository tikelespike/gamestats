package com.tikelespike.gamestats.businesslogic.entities;

/**
 * Should be implemented by all classes that have a version counter. The version is used to detect concurrent
 * modifications of the object, implementing optimistic locking. For example, if two users try to update the same object
 * at the same time, the version counter will be used to detect that one of the updates is based on outdated data.
 */
public interface HasVersion {

    /**
     * Returns the version counter of this object. The version is used to detect concurrent modifications of the object,
     * implementing optimistic locking.
     *
     * @return the version of this object.
     */
    Long getVersion();
}

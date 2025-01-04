package com.tikelespike.gamestats.data.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * Base class for all entities in the application. An entity is a database layer object that is persisted and retrieved
 * from the database. This class provides a unique identifier for each entity.
 */
@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Creates a new entity with uninitialized fields. This constructor is used by the JPA provider to create a new
     * instance of this entity from the database.
     */
    protected AbstractEntity() {
    }

    /**
     * Creates a new entity.
     *
     * @param id unique identifier of the entity
     */
    protected AbstractEntity(Long id) {
        this.id = id;
    }

    /**
     * Returns the unique numerical identifier of this entity. The identifier is used to identify this instance within
     * the database.
     *
     * @return the unique identifier of this entity
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique numerical identifier of this entity. This method is used by the JPA provider to set the
     * identifier of a new entity when it is saved to the database. Should not be called by application code.
     *
     * @param id the new unique identifier of this entity
     */
    protected void setId(Long id) {
        this.id = id;
    }
}

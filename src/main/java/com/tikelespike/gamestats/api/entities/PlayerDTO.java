package com.tikelespike.gamestats.api.entities;

public class PlayerDTO {

    private long id;
    private String name;
    private PlayerExperienceLevelDTO experienceLevel;

    public PlayerDTO() {
    }

    public PlayerDTO(long id, String name, PlayerExperienceLevelDTO experienceLevel) {
        this.id = id;
        this.name = name;
        this.experienceLevel = experienceLevel;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerExperienceLevelDTO getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(PlayerExperienceLevelDTO experienceLevel) {
        this.experienceLevel = experienceLevel;
    }
}

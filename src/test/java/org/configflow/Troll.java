package org.configflow;

/**
 *
 */
public class Troll {
    private String name;
    private int hitpoints;

    private Troll() {
    }

    public Troll(String name, int hitpoints) {
        this.name = name;
        this.hitpoints = hitpoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public void setHitpoints(int hitpoints) {
        this.hitpoints = hitpoints;
    }
}

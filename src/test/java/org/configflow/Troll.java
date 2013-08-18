package org.configflow;

/**
 *
 */
public class Troll {
    private String name;
    private int hitpoints;
    private boolean terrible;

    private Troll() {
    }

    public Troll(String name, int hitpoints) {
        this.name = name;
        this.hitpoints = hitpoints;
    }

    @Desc("Name of this enemy")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Desc("Health of the enemy")
    public int getHitpoints() {
        return hitpoints;
    }

    public void setHitpoints(int hitpoints) {
        this.hitpoints = hitpoints;
    }

    @Desc("Wether this enemy is extra terrible")
    public boolean isTerrible() {
        return terrible;
    }

    public void setTerrible(boolean terrible) {
        this.terrible = terrible;
    }
}

package org.configflow;

import java.util.ArrayList;
import java.util.List;

/**
 * Used for testing
 */
public class LevelConf {

    private String levelName = "";
    private double levelLength = 100;
    private float speed = 3.42f;
    private List<Troll> enemies = new ArrayList<Troll>();
    private Troll finalBoss;

    public LevelConf() {
    }



    public LevelConf(String levelName, double levelLength) {
        this.levelName = levelName;
        this.levelLength = levelLength;
    }

    @Desc("Title of the level")
    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    @Desc("Number of rooms in the level")
    public double getLevelLength() {
        return levelLength;
    }

    public void setLevelLength(double levelLength) {
        this.levelLength = levelLength;
    }

    @Desc("Enemy at the end of the level")
    public Troll getFinalBoss() {
        return finalBoss;
    }

    public void setFinalBoss(Troll finalBoss) {
        this.finalBoss = finalBoss;
    }

    @Desc("Enemies spawned in the level")
    public List<Troll> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Troll> enemies) {
        this.enemies = enemies;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}

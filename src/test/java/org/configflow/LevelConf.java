package org.configflow;

import java.util.ArrayList;
import java.util.List;

/**
 * Used for testing
 */
public class LevelConf {

    private String levelName = "";
    private float levelLength = 100;
    private List<Troll> enemies = new ArrayList<Troll>();
    private Troll finalBoss;

    public LevelConf() {
    }



    public LevelConf(String levelName, float levelLength) {
        this.levelName = levelName;
        this.levelLength = levelLength;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public float getLevelLength() {
        return levelLength;
    }

    public void setLevelLength(float levelLength) {
        this.levelLength = levelLength;
    }

    public Troll getFinalBoss() {
        return finalBoss;
    }

    public void setFinalBoss(Troll finalBoss) {
        this.finalBoss = finalBoss;
    }

    public List<Troll> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Troll> enemies) {
        this.enemies = enemies;
    }
}

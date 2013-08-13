package org.configflow;

/**
 * Used for testing
 */
public class LevelConf {

    private String levelName = "";
    private float levelLength = 100;

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
}

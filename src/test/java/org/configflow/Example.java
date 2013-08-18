package org.configflow;

import org.configflow.ui.EditorListener;

/**
 *
 */
public class Example {

    public static void main(String[] args) {

        LevelConf levelConf = new LevelConf("TestLevel", 13.3);
        levelConf.getEnemies().add(new Troll("Mr Can-on Foder", 8));
        levelConf.getEnemies().add(new Troll("Mr Cross-bow Foder", 6));
        levelConf.setFinalBoss(new Troll("Mr Big Foder", 100));

        Configurator configurator = new PlainConfigurator(LevelConf.class, Troll.class);
        configurator.openEditor(levelConf, new EditorListener() {
            @Override public void onEdited(Object confObject, Prop property) {
                System.out.println("Edited: " + property.getName() + " of " + confObject + ", value is: " + property.getValue(confObject));
            }
        });
    }
}

package org.configflow;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 *
 */
public class ConfigTest {

    private LevelConf levelConf;
    private Configurator configurator;


    @Before
    public void setUp() throws Exception {
        levelConf = new LevelConf();
        configurator = new PlainConfigurator();
    }

    @Test
    public void testFindProperties() {
        configurator.registerType(LevelConf.class);

        Collection<String> propNames = configurator.getPropertyNames(levelConf);

        assertTrue("Should contain correct properties", propNames.contains("LevelName"));
        assertTrue("Should contain correct properties", propNames.contains("LevelLength"));
        assertTrue("Should contain correct properties", propNames.contains("Enemies"));
        assertEquals("Should have correct number of props", 4, propNames.size());
    }

    @Test
    public void testGetAndSetProperties() throws Exception {
        configurator.registerType(LevelConf.class);

        LevelConf conf = new LevelConf();

        assertEquals("", configurator.getProperty(conf, "LevelName"));
        configurator.setProperty(conf, "LevelName", "FunnyLevel");
        assertEquals("Setting property should work", "FunnyLevel", configurator.getProperty(conf, "LevelName"));
    }

    @Test
    public void testNestedClasses() throws Exception {
        configurator.registerType(LevelConf.class);
        configurator.registerType(Troll.class);

        LevelConf conf = new LevelConf();

        configurator.setProperty(conf, "FinalBoss", new Troll("Aggor", 100));
        final Object finalBoss = configurator.getProperty(conf, "FinalBoss");
        configurator.setProperty(finalBoss, "Hitpoints", 200);
        assertEquals("FinalBoss should have been changed", 200, conf.getFinalBoss().getHitpoints());

    }

    @Test
    public void testSaveAndLoad() throws Exception {
        LevelConf conf = setupTestObj();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1000);
        configurator.saveConf(outputStream,  conf);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        LevelConf loadedConf = configurator.loadConf(inputStream, LevelConf.class);

        assertSimilar(conf, loadedConf);

    }

    @Test
    public void testCopy() throws Exception {
        final LevelConf conf = setupTestObj();

        final LevelConf copy = configurator.deepCopy(conf);

        assertSimilar(conf, copy);

    }

    private LevelConf setupTestObj() {
        configurator.registerType(LevelConf.class);
        configurator.registerType(Troll.class);

        LevelConf conf = new LevelConf("FirstLevel", 10);
        conf.setFinalBoss(new Troll("Igor", 100));
        conf.getEnemies().add(new Troll("MiniTroll", 10));
        return conf;
    }

    private void assertSimilar(LevelConf conf, LevelConf loadedConf) {
        final String msg = "Save and load should give same object values";
        assertEquals(msg, conf.getLevelName(), loadedConf.getLevelName());
        assertEquals(msg, conf.getLevelLength(), loadedConf.getLevelLength(), 0.001);
        assertEquals(msg, conf.getFinalBoss().getName(), loadedConf.getFinalBoss().getName());
        assertEquals(msg, conf.getFinalBoss().getHitpoints(), loadedConf.getFinalBoss().getHitpoints());
        assertEquals(msg, conf.getEnemies().get(0).getName(), loadedConf.getEnemies().get(0).getName());
        assertEquals(msg, conf.getEnemies().get(0).getHitpoints(), loadedConf.getEnemies().get(0).getHitpoints());

        final String msg2 = "Object instance should be different";
        assertNotEquals(msg2, conf, loadedConf);
        assertNotEquals(msg2, conf.getFinalBoss(), loadedConf.getFinalBoss());
    }

    // TODO: Test opening UI for editing the conf class

    // TODO: Test editing conf object with list of other conf objects

    // TODO: Test that unregistered conf objects can not be created or queried



}

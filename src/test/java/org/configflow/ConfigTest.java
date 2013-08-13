package org.configflow;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class ConfigTest {

    private LevelConf conf;


    @Before
    public void setUp() throws Exception {
        conf = new LevelConf();

    }

    @Test
    public void testFindProperties() {
        Configurator<LevelConf> configurator = new Configurator<LevelConf>(LevelConf.class);

        List<String> propNames = configurator.getPropertyNames();

        assertTrue("Should contain correct properties", propNames.contains("levelName"));
        assertTrue("Should contain correct properties", propNames.contains("levelLength"));
    }

    // TODO: Test loading and saving a configuration object

    // TODO: Test getting and setting properties through the configurator

    // TODO: Test working with nested classes through the configurator

    // TODO: Test opening UI for editing the conf class

    // TODO: Test copying conf object to local library

    // TODO: Test saving and loading local library of conf objects

    // TODO: Test editing conf object with list of other conf objects

    // TODO: Test that unregistered conf objects can not be created

}

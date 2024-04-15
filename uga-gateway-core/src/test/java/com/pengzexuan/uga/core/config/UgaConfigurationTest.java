package com.pengzexuan.uga.core.config;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UgaConfigurationTest {

// System configuration loader
    @Test
    public void generateUgaId_test() {

        List<UgaConfiguration> configurations = new ArrayList<>(2000);

        for (int i = 0; i < 2000; i++) {
            UgaConfiguration configuration = new UgaConfiguration();
            configurations.add(configuration);
        }
        Set<String> ugaIdSet = new HashSet<>(2000);
        configurations.forEach(configuration -> {
            String ugaId = configuration.getUgaId();
            System.err.println(ugaId);
            ugaIdSet.add(ugaId);
        });
        Assert.assertEquals(2000, ugaIdSet.size());
    }
}

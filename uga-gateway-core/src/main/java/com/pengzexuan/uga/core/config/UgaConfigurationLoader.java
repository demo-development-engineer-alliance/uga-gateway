package com.pengzexuan.uga.core.config;

import com.pengzexuan.uga.common.utils.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

@Slf4j
@SuppressWarnings({"unused"})
public class UgaConfigurationLoader {

    private static final String CONFIG_ENV_PREFIX = "UGA_";

    private static final String CONFIG_JVM_PREFIX = "uga.";

    private static final String CONFIG_FILE = "uga.properties";

    private static final UgaConfigurationLoader INSTANCE = new UgaConfigurationLoader();

    private final UgaConfiguration ugaConfiguration = new UgaConfiguration();

    private UgaConfigurationLoader() {
    }

    public static UgaConfigurationLoader getInstance() {
        return INSTANCE;
    }

    public static UgaConfiguration getUgaConfig() {
        return INSTANCE.ugaConfiguration;
    }

    public UgaConfiguration loadConfig(String... args) {

        //	1. Minimum priority: Configuration file
        InputStream is = UgaConfigurationLoader.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
        if(is != null) {
            Properties properties = new Properties();
            try {
                properties.load(is);
                PropertiesUtils.properties2Object(properties, ugaConfiguration);
            } catch (IOException e) {
                log.error("#UgaConfigurationLoader# loadConfig config file: {} is error", CONFIG_FILE, e);
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    //	ignore
                }
            }
        }

        //	2. Second to last priority, environment variables
        {
            Map<String, String> env = System.getenv();
            Properties properties = new Properties();
            properties.putAll(env);
            PropertiesUtils.properties2Object(properties, ugaConfiguration, CONFIG_ENV_PREFIX);
        }

        //	3. JVM parameters
        {
            Properties properties = System.getProperties();
            PropertiesUtils.properties2Object(properties, ugaConfiguration, CONFIG_JVM_PREFIX);
        }

        //	4. Operating parameters: --xxx=xxx--enable=true--port=1234
        {
            if(args != null && args.length > 0) {
                Properties properties = new Properties();
                for(String arg : args) {
                    if(arg.startsWith("--") && arg.contains("=")) {
                        properties.put(arg.substring(2, arg.indexOf("=")), arg.substring(arg.indexOf("=") + 1));
                    }
                }
                PropertiesUtils.properties2Object(properties, ugaConfiguration);
            }
        }

        return ugaConfiguration;
    }
}

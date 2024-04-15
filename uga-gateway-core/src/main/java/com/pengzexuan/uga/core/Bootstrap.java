package com.pengzexuan.uga.core;

import com.pengzexuan.uga.core.config.UgaConfiguration;
import com.pengzexuan.uga.core.config.UgaConfigurationLoader;
import com.pengzexuan.uga.core.container.UgaContainer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Bootstrap {
    public static void main(String... args) {
        String ugaLogo = "\n" +
                "\t\t██╗   ██╗  ██████╗   █████╗ \n" +
                "\t\t██║   ██║ ██╔════╝  ██╔══██╗\n" +
                "\t\t██║   ██║ ██║  ███╗ ███████║\n" +
                "\t\t██║   ██║ ██║   ██║ ██╔══██║\n" +
                "\t\t╚██████╔╝ ╚██████╔╝ ██║  ██║\n" +
                "\t\t ╚═════╝   ╚═════╝  ╚═╝  ╚═╝ (Unified Generalized API Gateway, UGA)\n";
        log.info("starting....\n{}\n" , ugaLogo);
        UgaConfiguration configuration = UgaConfigurationLoader.getInstance().loadConfig(args);
        UgaContainer container = new UgaContainer(configuration);
        container.start();
        Runtime.getRuntime().addShutdownHook(new Thread(container::shutdown));
    }
}
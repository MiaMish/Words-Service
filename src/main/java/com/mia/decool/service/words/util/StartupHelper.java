package com.mia.decool.service.words.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;


@Slf4j
public final class StartupHelper {

    public static void logApplicationStartup(Environment env) {
        String protocol = "http";
        String serverPort = env.getProperty("server.port");
        log.info("""

                        ----------------------------------------------------------
                        \tApplication '{}' is running! Access URLs:
                        \tBase: \t\t{}://localhost:{}/
                        \tSwagger: \t{}://localhost:{}/swagger-ui.html
                        ----------------------------------------------------------""",
                env.getProperty("spring.application.name"),
                protocol,
                serverPort,
                protocol,
                serverPort);
    }
}

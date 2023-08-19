package com.mia.decool.service.words;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class SpringAsyncConfig {

    // This bean is used by the WordsDaoImpl class to execute the append() method in a separate thread.
    @Bean(name = "appendWordsThreadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
        // Note: for real prod application, adjust the thread pool size and other attributes accordingly.
        // In addition, you might want to consider other thread pool implementations, or use config files for dynamic configuration.
        // For now, we just use the default values.
        return new ThreadPoolTaskExecutor();
    }



}

package faang.school.achievement.config.context;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorPool {

    @Value("${thread-pool.size}")
    private int poolSize;

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(poolSize);
    }
}
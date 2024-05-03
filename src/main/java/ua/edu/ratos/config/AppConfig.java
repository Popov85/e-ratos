package ua.edu.ratos.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.session.data.redis.config.ConfigureNotifyKeyspaceEventsAction;
import org.springframework.session.data.redis.config.ConfigureRedisAction;

@Configuration
public class AppConfig {

    /**
     * Used for easier DTO - Entity, Entity - DTO mapping
     * @return ModelMapper
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /**
     * This config tells Spring not to use CONFIG Redis command to configure remote AWS Redis cluster.
     * AWS Elastic Cache Redis impl does not allow CONFIG command for security reasons,
     * and hence it must be allowed manually via AWS console!
     * @link <a href="https://github.com/Popov85/e-ratos/wiki/Deployment-prod-(AWS)">Project Wiki</a>
     * @return ConfigureRedisAction
     */
    @Bean
    @Profile("prod")
    public ConfigureRedisAction configureRedisActionProd() {
        // This tells Spring not to configure Redis keyspace notifications
        return ConfigureRedisAction.NO_OP;
    }

    @Bean
    @Profile("!prod")
    public ConfigureRedisAction configureRedisActionStage() {
        return new ConfigureNotifyKeyspaceEventsAction();
    }
}

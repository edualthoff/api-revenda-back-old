package br.ml.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableAsync
public class ThreadConfig {
	private static final Logger log = LoggerFactory.getLogger(ThreadConfig.class);

	/**
	 * Configuração da Thread do Scheduler - Spring.. O padrão do Poll é 1
	 * Alterado para 2 
	 * @return
	 */
	@Bean
	public ThreadPoolTaskScheduler taskScheduler(){
	    ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
	    taskScheduler.setPoolSize(1);
	    return  taskScheduler;
	}
	
    @Bean("baseCrawlerAnaliseThread")
    public ThreadPoolTaskExecutor baseCrawlerAnaliseThread() {
    	log.debug("Thread baseCrawlerAnaliseThread..");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(20);
        executor.setThreadNamePrefix("baseCrawlerAnaliseThread");
        executor.initialize();

        return executor;
    }
    
    @Bean("baseAnaliseThread")
    public ThreadPoolTaskExecutor baseAnaliseThread() {
    	log.debug("Thread baseAnaliseThread..");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("baseAnaliseThread");
        executor.initialize();

        return executor;
    }
}

package com.softvan.config;

import com.softvan.entity.Student;
import com.softvan.job.ItemProcessor;
import com.softvan.job.ItemReader;
import com.softvan.job.ItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ItemReader itemReader;
    private final ItemProcessor itemProcessor;
    private final ItemWriter itemWriter;

    @SuppressWarnings("unchecked")
    @Bean
    public Step step() throws Exception {
        return stepBuilderFactory.get("step").<Student, Student>chunk(1000)
                .reader(itemReader.reader())
                .processor(itemProcessor.asyncItemProcessor())
                .writer(itemWriter.writer())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job CsvBatch() throws Exception {
        return jobBuilderFactory.get("csv_batch")
                .incrementer(new RunIdIncrementer())
                .start(step())
                .build();

    }

    //a TaskExecutor implementation (we use SimpleAsyncTaskExecutor in this example) and reference it in your step.
    // When you execute the statement job, Spring creates a threadpool of 5 threads,
    // executing each chunk in a different thread or 5 chunks in parallel
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //Is the minimum number of threads that remain active at any given point of time.
        // If you don’t provide a value explicitly then it will have default value as 1
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        //Is the maximum number of threads that can be created .
        //The maxPoolSize relies on queueCapacity because ThreadPoolTaskExecutor creates a new thread only if the number of items in the queue exceeds queue capacity.
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("redis-");
        executor.initialize();
        return executor;
    }
}

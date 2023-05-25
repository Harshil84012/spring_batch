package com.softvan.job;

import com.softvan.entity.Student;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class ItemProcessor {

    public org.springframework.batch.item.ItemProcessor<Student, Student> itemProcessor() {
        return (Student student) -> {

            return student;
        };

    }

    @Bean
    public AsyncItemProcessor asyncItemProcessor() throws Exception {
        AsyncItemProcessor<Student, Student> asyncItemProcessor = new AsyncItemProcessor<>();
        asyncItemProcessor.setDelegate(itemProcessor());
        asyncItemProcessor.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return asyncItemProcessor;
    }
}

package com.softvan.job;

import com.softvan.entity.Student;
import com.softvan.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class ItemWriter {

    private final StudentRepository studentRepository;

    @Bean
    public AsyncItemWriter<Student> writer() {
        AsyncItemWriter<Student> asyncItemWriter = new AsyncItemWriter<>();
        asyncItemWriter.setDelegate(items -> items.forEach(studentRepository::save));
        return asyncItemWriter;
    }
}

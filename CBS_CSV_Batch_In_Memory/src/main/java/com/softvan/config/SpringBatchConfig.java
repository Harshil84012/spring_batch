package com.softvan.config;

import com.softvan.entity.Organization;
import com.softvan.repository.OrganizationRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
@AllArgsConstructor
@EnableBatchProcessing
public class SpringBatchConfig {

    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    private OrganizationRepository organizationRepository;

    @Bean
    public FlatFileItemReader<Organization> reader() { // class to read data from csv file
        FlatFileItemReader<Organization> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new FileSystemResource("/home/dev1024/Downloads/CBS_CSV_Batch (2)/CBS_CSV_Batch/src/main/resources/Organization.csv"));
        flatFileItemReader.setName("csvReader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());
        flatFileItemReader.setStrict(true);
        return flatFileItemReader;
    }

    public LineMapper<Organization> lineMapper() { // typically used to map lines read from a file to domain objects on a per line basis.
        DefaultLineMapper<Organization> defaultLineMapper = new DefaultLineMapper<>(); //you can get fields by index or name
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(",");
        delimitedLineTokenizer.setStrict(false);
        delimitedLineTokenizer.setNames("Id", "name", "webSite", "country", "description", "founded", "industry");
        // this class will map the csv file to the organization object
        BeanWrapperFieldSetMapper<Organization> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Organization.class);
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        return defaultLineMapper;
    }

    @Bean
    public OrganizationProcessor organizationProcessor() {
        return new OrganizationProcessor();
    }

    @Bean
    public RepositoryItemWriter<Organization> writer() { //It provides CRUD operations for JobLauncher , Job , and Step instantiations.
        RepositoryItemWriter<Organization> repositoryItemWriter = new RepositoryItemWriter<>();
        repositoryItemWriter.setRepository(organizationRepository);
        repositoryItemWriter.setMethodName("save");
        return repositoryItemWriter;
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("csv-step").<Organization, Organization>chunk(10)
                .reader(reader()).processor(organizationProcessor()).writer(writer())
                .taskExecutor(taskExecutor()).
                build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("importOrganizationInfo").flow(step1()).end().build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
        simpleAsyncTaskExecutor.setConcurrencyLimit(10);
        return simpleAsyncTaskExecutor;
    }
}


package uz.urinov.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
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
import org.springframework.transaction.PlatformTransactionManager;
import uz.urinov.student.Student;
import uz.urinov.student.StudentRepository;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final StudentRepository repository;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;


    @Bean
    public FlatFileItemReader<Student> reader() {
        FlatFileItemReader<Student> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/student.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    @Bean
    public StudentProcessor processor() {
        return new StudentProcessor();
    }


    @Bean
    public RepositoryItemWriter<Student> writer() {
        RepositoryItemWriter<Student> writer = new RepositoryItemWriter<>();
        writer.setRepository(repository);
        writer.setMethodName("save");
        return writer;
    }


    @Bean
    public Step importStep() {
        return new StepBuilder("csvImport", jobRepository)
                .<Student, Student>chunk(1000, platformTransactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job importJob() {
        return new JobBuilder("importStudent", jobRepository)
                .start(importStep())
                .build();

    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }

    private LineMapper<Student> lineMapper() {
        DefaultLineMapper<Student> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "firstName", "lastName", "age");

        BeanWrapperFieldSetMapper<Student> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Student.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

}

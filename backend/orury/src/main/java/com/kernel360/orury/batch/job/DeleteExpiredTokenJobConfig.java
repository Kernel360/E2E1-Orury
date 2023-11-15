package com.kernel360.orury.batch.job;

import com.kernel360.orury.domain.user.db.RefreshTokenEntity;
import com.kernel360.orury.domain.user.db.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author : hyungjoon cho
 * @date : 2023/11/14
 * @description : 만료된 refresh 토큰을 삭제해주는 배치
 */

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DeleteExpiredTokenJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @PersistenceContext
    private EntityManager entityManager;

    // Job
    @Bean
    public Job deleteExpiredTokensJob(Step deleteExpiredTokensStep) {
        return jobBuilderFactory.get("deleteExpiredTokensJob")
                .incrementer(new RunIdIncrementer())
                .start(deleteExpiredTokensStep)
                .build();
    }

    // Step
    @Bean
    public Step deleteExpiredTokensStep(JpaPagingItemReader<RefreshTokenEntity> reader, ItemWriter<RefreshTokenEntity> writer) {
        return stepBuilderFactory.get("deleteExpiredTokensStep")
                .<RefreshTokenEntity, RefreshTokenEntity>chunk(1)
                .reader(reader)
                .writer(writer)
                .build();
    }

    // ItemReader
    @StepScope
    @Bean
    public JpaPagingItemReader<RefreshTokenEntity> expiredTokenReader() {
        String jpqlQuery = "select t " +
                "             from RefreshTokenEntity t" +
                "            where t.expirationDate < :currentDate";

        Map<String, Object> parameterValues = Collections.singletonMap("currentDate", LocalDateTime.now());

        JpaPagingItemReader<RefreshTokenEntity> reader = new JpaPagingItemReader<>();
        reader.setQueryString(jpqlQuery);
        reader.setParameterValues(parameterValues);
        reader.setEntityManagerFactory(entityManagerFactory);

        return reader;
    }

    // ItemWriter
    @StepScope
    @Bean
    public ItemWriter<RefreshTokenEntity> expiredTokenWriter() {
        return items -> {
            for (RefreshTokenEntity item : items) {
                Long id = item.getId();
                entityManager.remove(entityManager.contains(item) ? item : entityManager.merge(item));
                log.info("삭제된 토큰 : " + id);
            }
        };
    }

}

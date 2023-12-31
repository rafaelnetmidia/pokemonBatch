package com.pokemon.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemon.reader.Pokemon;
import com.pokemon.reader.PokemonApiReader;
import com.pokemon.reader.PokemonApiReaderAdapter;
import com.pokemon.writer.PokemonFileWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

@Configuration
public class BatchConfig {

    @Bean
    public ItemReader<List<Pokemon>> reader() {
        return new PokemonApiReaderAdapter(new PokemonApiReader());
    }

    @Bean
    public ItemWriter<Map<String, Object>> writer() {
        return new PokemonFileWriter();
    }

    @Bean
    public Job job(JobRepository jobRepository, Step step){
        return new JobBuilder("job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public Step step(ItemReader<List<Pokemon>> reader, ItemWriter<Map<String, Object>> writer, JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("step", jobRepository)
                .<List<Pokemon>, Map<String, Object>>chunk(1, platformTransactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }


}

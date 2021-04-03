package com.digibooster.spring.batch.config;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemReader;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class TestConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public JobExecutionContextListenerTest contextListenerTest() {
		System.out.println("jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
		return new JobExecutionContextListenerTest();
	}

	@Bean
	public ItemReader reader() {
		return new ItemReader() {
			
			Iterator<String> values =  Arrays.asList("val1", "val2").iterator();

			public Object read()
					throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
				// TODO Auto-generated method stub
				return (values.hasNext()) ? values.next() : null;
			}

		

		};
	}

	@Bean
	public ItemWriter writer() {
		return new ItemWriter() {

			public void write(List items) throws Exception {
			}
		};
	}

	@Bean
	public Job testJob(@Autowired Step step1) {
		return jobBuilderFactory
				.get("importUserJob")
				.incrementer(new RunIdIncrementer())
				.flow(step1)
				.end()
				.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory
				.get("step1")
				.chunk(10)
				.reader(reader())
				.writer(writer())
				.build();
	}

}

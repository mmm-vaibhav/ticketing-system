package com.ticketing.notification.configs;

import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import com.ticketing.common.events.BaseEvent;
import com.ticketing.common.events.UserCreatedEvent;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;

@EnableKafka
@Configuration
public class KafkaConfig {

	private final MeterRegistry meterRegistry;
	
	private Counter dlqCounter;
	private Counter retryCounter;


	public KafkaConfig(MeterRegistry meterRegistry) {
		this.meterRegistry = meterRegistry;
	}


	@PostConstruct
	public void init() {
		dlqCounter = meterRegistry.counter("events_dlq_total");
		retryCounter = meterRegistry.counter("events_retried_total");
	}

	
	@Bean
	public DefaultErrorHandler errorHandler(KafkaTemplate<String, Object> template) {

	    DeadLetterPublishingRecoverer recoverer =
	        new DeadLetterPublishingRecoverer(template,
	            (record, ex) -> {
	                dlqCounter.increment();
	                return new TopicPartition(record.topic() + "-dlt", record.partition());
	            });

	    DefaultErrorHandler handler = new DefaultErrorHandler(recoverer, new FixedBackOff(5000L, 3));

	    handler.setRetryListeners((record, ex, deliveryAttempt) -> {
	    	System.out.println("Retry incrementing..");
	        retryCounter.increment(); // ✅ correct place
	        ex.printStackTrace();
	    });

	    return handler;
	}

//	@Bean
//	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
//			ConsumerFactory<String, String> consumerFactory, DefaultErrorHandler errorHandler) {
//		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//		factory.setConsumerFactory(consumerFactory);
//		factory.setCommonErrorHandler(errorHandler);
//		return factory;
//	}
	
//	@Bean
//	public ConsumerFactory<String, BaseEvent<UserCreatedEvent>> consumerFactory() {
//
//	    JsonDeserializer<BaseEvent<UserCreatedEvent>> deserializer =
//	            new JsonDeserializer<>();
//
//	    deserializer.addTrustedPackages("*");
//	    deserializer.setUseTypeHeaders(false);
//
//	    return new DefaultKafkaConsumerFactory<>(
//	            Map.of(
//	                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
//	                ConsumerConfig.GROUP_ID_CONFIG, "notification-group-v2",
//	                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
//	            ),
//	            new StringDeserializer(),
//	            deserializer
//	    );
//	}
	
	
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, BaseEvent<UserCreatedEvent>> kafkaListenerContainerFactory(
	        ConsumerFactory<String, BaseEvent<UserCreatedEvent>> consumerFactory,
	        DefaultErrorHandler errorHandler) {

	    ConcurrentKafkaListenerContainerFactory<String, BaseEvent<UserCreatedEvent>> factory =
	            new ConcurrentKafkaListenerContainerFactory<>();

	    factory.setConsumerFactory(consumerFactory);
	    factory.setCommonErrorHandler(null);

	    return factory;
	}

}
package com.tudux.libraryeventproducer.producer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tudux.libraryeventproducer.domain.LibraryEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LibraryEventProducer {
	
	@Autowired
	KafkaTemplate<Integer, String> kafkaTemplate;
	
	@Autowired
	ObjectMapper objectMapper;
	
	String topic = "library-events";
	
	public SendResult<Integer, String> sendLibraryEventSynchronus(LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
		
		Integer key = libraryEvent.getLibraryEventId();
		String value = objectMapper.writeValueAsString(libraryEvent);
		SendResult<Integer, String> sendResult=null;
		
		try {
			sendResult = kafkaTemplate.sendDefault(key, value).get(1, TimeUnit.SECONDS);
		} catch (ExecutionException | InterruptedException e) {
			log.error("ExecutionException/InterruptedException Sending the Message and the exception is {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Exception Sending the Message and the exception is {}", e.getMessage());
			throw e;
		}
						
		return sendResult;
		
	}
	

}

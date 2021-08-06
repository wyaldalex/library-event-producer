package com.tudux.libraryeventproducer.controller;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tudux.libraryeventproducer.domain.LibraryEvent;
import com.tudux.libraryeventproducer.producer.LibraryEventProducer;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LibraryEventsController {
	
	@Autowired
	LibraryEventProducer libraryEventProducer;
	
	@PostMapping("/v1/libraryevent")
	public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent libraryEvent) {
		System.out.println(libraryEvent.toString());
		//System.out.println(LibraryEvent.ge);
		//invoke kafka producer
		return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
	}
	
	@PostMapping("/v1/libraryevent/sync")
	public ResponseEntity<LibraryEvent> postLibraryEventSync(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
		System.out.println(libraryEvent.toString());
		//invoke kafka producer
		log.info("before sendLibraryEvent");
		SendResult<Integer, String> sendResult = libraryEventProducer.sendLibraryEventSynchronus(libraryEvent);
		return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
	}
	
	@PostMapping("/v1/libraryevent/v1/async")
	public ResponseEntity<LibraryEvent> postLibraryEventAsync1(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
		System.out.println(libraryEvent.toString());
		//invoke kafka producer
        //invoke kafka producer
        libraryEventProducer.sendLibraryEventAsync1(libraryEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
	}	

	@PostMapping("/v1/libraryevent/v2/async")
	public ResponseEntity<LibraryEvent> postLibraryEventAsync2(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
        //invoke kafka producer
        libraryEventProducer.sendLibraryEvent_Async2(libraryEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
	}	
	

}

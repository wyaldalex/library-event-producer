package com.tudux.libraryeventproducer.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tudux.libraryeventproducer.domain.Book;
import com.tudux.libraryeventproducer.domain.LibraryEvent;
import com.tudux.libraryeventproducer.domain.LibraryEventType;
import com.tudux.libraryeventproducer.producer.LibraryEventProducer;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@WebMvcTest(LibraryEventsController.class)
@AutoConfigureMockMvc
public class LibraryEventControllerUnitTest {
	
    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    //This is done to simulate the event producer class  behavior
    @MockBean
    LibraryEventProducer libraryEventProducer;
    
    @Test
    void postLibraryEvent() throws Exception {
    	
    	//given
    	Book book = Book.builder()
    			.bookId(123)
    			.bookAuthor("Test Author")
    			.bookName("Test Book Name")
    			.build();
    	

    	LibraryEvent  libraryEvent = LibraryEvent.builder()
    			.libraryEventId(null)
    			.book(null)
    			.libraryEventType(LibraryEventType.NEW)
    			.build();
    	
        String json = objectMapper.writeValueAsString(libraryEvent);
        //doNothing().when(libraryEventProducer).sendLibraryEventSynchronus(isA(LibraryEvent.class));
        doReturn(new  SendResult<Integer, String>(null, null)).when(libraryEventProducer).sendLibraryEventSynchronus(isA(LibraryEvent.class));
        
        //when
        mockMvc.perform(post("/v1/libraryevent/sync")
        .content(json)
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    	
    }

	

}

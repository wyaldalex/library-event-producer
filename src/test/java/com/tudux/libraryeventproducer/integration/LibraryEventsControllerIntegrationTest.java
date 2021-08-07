package com.tudux.libraryeventproducer.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;

import com.tudux.libraryeventproducer.domain.Book;
import com.tudux.libraryeventproducer.domain.LibraryEvent;
import com.tudux.libraryeventproducer.domain.LibraryEventType;

//This library for some reason never gets recognized by eclipse auto completion..
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = {"library-events"}, partitions = 3)
//Test properties is used to override the kafka broker ports
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
"spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"})
public class LibraryEventsControllerIntegrationTest {
	
    @Autowired
    TestRestTemplate restTemplate;
    
    @Test
    void postLibraryEvent () {
    	
    	//give
    	Book book = Book.builder()
    			.bookId(123)
    			.bookAuthor("Test Author")
    			.bookName("Test Book Name")
    			.build();
    	

    	LibraryEvent  libraryEvent = LibraryEvent.builder()
    			.libraryEventId(null)
    			.book(book)
    			.libraryEventType(LibraryEventType.NEW)
    			.build();
    	
    	HttpHeaders headers = new HttpHeaders();
    	headers.set("content-type", MediaType.APPLICATION_JSON.toString());
    	HttpEntity<LibraryEvent> request  = new HttpEntity<>(libraryEvent,headers);
    	
    	//when
    	ResponseEntity<LibraryEvent> responseEntity = restTemplate.exchange("/v1/libraryevent", HttpMethod.POST,request, LibraryEvent.class);
    	
        //then
        assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
    			
    	
    	
    }

}

package com.ritesh.libraryevents.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ritesh.libraryevents.domain.LibraryEvent;
import com.ritesh.libraryevents.domain.LibraryEventType;
import com.ritesh.libraryevents.producer.LibraryEventProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LibraryEventsController {

    @Autowired
    LibraryEventProducer libraryEventProducer;

    @PostMapping("/v1/libraryevent")
    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException {

        // Invoke kafka producer
        log.info("Before sendLibraryEvent");
        libraryEvent.setLibraryEventType(LibraryEventType.NEW);
        //libraryEventProducer.sendLibraryEvent(libraryEvent); // This is approach -1, default topic from autoconfig
        //SendResult<Integer, String> sendResult = libraryEventProducer.sendLibraryEventSynchronus(libraryEvent); // This is approach -2, default topic from autoconfig
        libraryEventProducer.sendLibraryEventsWithTopic(libraryEvent); // This is approach 3, passing specific topic
        //log.info("SendResults is : {} ", sendResult.toString());
        log.info("After sendLibraryEvent");
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }
}

package com.takeaway.test.event.services.impl;

import com.takeaway.test.common.messages.EventMessage;
import com.takeaway.test.event.model.entities.EventLog;
import com.takeaway.test.event.repositories.EventLogRepository;
import com.takeaway.test.event.services.EventsConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 23/02/2019
 */
@EnableBinding(Sink.class)
@Slf4j
@Service
public class EventsConsumerImpl implements EventsConsumer {
    @Autowired
    EventLogRepository eventLogRepository;

    @StreamListener(target = Sink.INPUT)
    @Override
    public void processEventMessage(EventMessage event) {
        log.info("UUID: {}, Event: {}", event.getUuid(), event.getEvent());
        eventLogRepository.save(EventLog.builder()
                .uuid(event.getUuid())
                .eventType(event.getEvent())
                .build());
    }
}

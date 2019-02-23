package com.takeaway.test.event.services.impl;

import com.takeaway.test.common.messages.EventMessage;
import com.takeaway.test.event.model.entities.EventLog;
import com.takeaway.test.event.model.web.EventLogResponseItem;
import com.takeaway.test.event.repositories.EventLogRepository;
import com.takeaway.test.event.services.EventLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

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
public class EventLogServiceImpl implements EventLogService {
    private final EventLogRepository eventLogRepository;

    @Autowired
    public EventLogServiceImpl(EventLogRepository eventLogRepository) {
        this.eventLogRepository = eventLogRepository;
    }

    @Override
    public Collection<EventLogResponseItem> listEvents(String uuid) {
        Sort reverseOrder = new Sort(Sort.Direction.DESC, "_id");
        Collection<EventLog> eventLogs = eventLogRepository.findEventLogsByUuid(uuid, reverseOrder);
        return eventLogs.stream()
                .map(el -> EventLogResponseItem.builder()
                    .uuid(el.getUuid())
                    .action(el.getAction())
                    .timestamp(el.getTimestamp())
                    .build())
                .collect(Collectors.toList());
    }

    @StreamListener(target = Sink.INPUT)
    @Override
    public void processEventMessage(EventMessage event) {
        log.debug("UUID: {}, Action: {}", event.getUuid(), event.getAction());
        eventLogRepository.insert(EventLog.builder()
                .uuid(event.getUuid())
                .action(event.getAction())
                .timestamp(event.getTimestamp())
                .build());
    }
}

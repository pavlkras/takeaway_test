package com.takeaway.test.event.services.impl;

import com.takeaway.test.common.messages.Action;
import com.takeaway.test.common.messages.EventMessage;
import com.takeaway.test.event.Application;
import com.takeaway.test.event.model.entities.EventLog;
import com.takeaway.test.event.model.web.EventLogResponseItem;
import com.takeaway.test.event.repositories.EventLogRepository;
import com.takeaway.test.event.services.EventLogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class EventLogServiceImplTest {
    @Autowired
    private EventLogService eventLogService;

    @MockBean
    private EventLogRepository eventLogRepository;

    @Test
    public void listEvents_noEventsFound_emptyList() {
        final String uuid = UUID.randomUUID().toString();
        Mockito.when(eventLogRepository.findEventLogsByUuid(any(String.class), any(Sort.class))).thenReturn(new ArrayList<>());

        Collection<EventLogResponseItem> eventLogs = eventLogService.listEvents(uuid);
        assertTrue(eventLogs.isEmpty());
    }

    @Test
    public void listEvents_eventsFound_listWithItems() {
        final String uuid = UUID.randomUUID().toString();
        List<EventLog> eventLogList = Collections.nCopies(2, EventLog.builder().uuid(uuid).action(Action.UPDATE).timestamp(LocalDateTime.now()).build());
        Mockito.when(eventLogRepository.findEventLogsByUuid(any(String.class), any(Sort.class))).thenReturn(eventLogList);

        Collection<EventLogResponseItem> eventLogs = eventLogService.listEvents(uuid);
        assertEquals(2, eventLogs.size());
    }

    @Test
    public void processEventMessage() throws Exception {
        EventMessage eventMessage = EventMessage.builder()
                .uuid("test_uuid")
                .action(Action.CREATE)
                .timestamp(LocalDateTime.of(2019, 1, 1, 10, 0, 0))
                .build();
        eventLogService.processEventMessage(eventMessage);
        Mockito.verify(eventLogRepository).insert(argThat((EventLog eventLog) ->
                eventLog.getUuid().equals("test_uuid") &&
                eventLog.getAction().equals(Action.CREATE) &&
                eventLog.getTimestamp().equals(LocalDateTime.of(2019, 1, 1, 10, 0, 0))));
    }
}
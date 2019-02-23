package com.takeaway.test.event.controllers;

import com.takeaway.test.event.model.entities.EventLog;
import com.takeaway.test.event.repositories.EventLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 23/02/2019
 */

@RestController
@RequestMapping("/events")
public class EventLogController {
    @Autowired
    private EventLogRepository eventLogRepository;

    @GetMapping("/{uuid}")
    public ResponseEntity<Collection<EventLog>> listEventLogs(@PathVariable String uuid) {
        Sort sort = new Sort(Sort.Direction.DESC, "_id");
        return ResponseEntity.ok(eventLogRepository.findEventLogsByUuid(uuid, sort));
    }
}

package com.takeaway.test.event.controllers;

import com.takeaway.test.event.model.entities.EventLog;
import com.takeaway.test.event.repositories.EventLogRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
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
@Api(value = "Events", description = "Operations related to Event logs")
public class EventLogController {
    private final EventLogRepository eventLogRepository;

    @Autowired
    public EventLogController(EventLogRepository eventLogRepository) {
        this.eventLogRepository = eventLogRepository;
    }

    @ApiOperation(value = "List logs in reverse order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetched")
    })
    @GetMapping(value = "/{uuid}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<EventLog>> listEventLogs(@ApiParam(value = "UUID of requested resource", required = true) @PathVariable String uuid) {
        Sort sort = new Sort(Sort.Direction.DESC, "_id");
        return ResponseEntity.ok(eventLogRepository.findEventLogsByUuid(uuid, sort));
    }
}

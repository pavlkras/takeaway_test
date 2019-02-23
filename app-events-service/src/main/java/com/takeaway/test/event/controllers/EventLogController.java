package com.takeaway.test.event.controllers;

import com.takeaway.test.event.model.web.EventLogResponseItem;
import com.takeaway.test.event.services.EventLogService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final EventLogService eventLogService;

    @Autowired
    public EventLogController(EventLogService eventLogService) {
        this.eventLogService = eventLogService;
    }

    @ApiOperation(value = "List logs in reverse order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetched")
    })
    @GetMapping(value = "/{uuid}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<EventLogResponseItem>> listEventLogs(@ApiParam(value = "UUID of requested resource", required = true) @PathVariable String uuid) {
        Collection<EventLogResponseItem> response = eventLogService.listEvents(uuid);
        return ResponseEntity.ok(response);
    }
}

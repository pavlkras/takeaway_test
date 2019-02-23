package com.takeaway.test.event.model.entities;

import com.takeaway.test.common.messages.EventType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 23/02/2019
 */

@Getter
@Setter
@Builder
public class EventLog {
    private String uuid;
    private EventType eventType;
    private LocalDateTime timestamp;
}

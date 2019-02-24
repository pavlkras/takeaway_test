package com.takeaway.test.event.model.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.takeaway.test.common.messages.Action;
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
@Builder
@JsonDeserialize(builder = EventLog.EventLogBuilder.class)
public class EventLog {
    private final String uuid;
    private final Action action;
    private final LocalDateTime timestamp;

    @JsonPOJOBuilder(withPrefix = "")
    public static class EventLogBuilder {

    }
}

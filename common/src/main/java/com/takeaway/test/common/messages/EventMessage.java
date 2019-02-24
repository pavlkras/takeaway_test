package com.takeaway.test.common.messages;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

import java.time.LocalDateTime;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 22/02/2019
 */

@Getter
@Builder
@JsonDeserialize(builder = EventMessage.EventMessageBuilder.class)
public class EventMessage {
    private final String uuid;
    private final Action action;
    @Builder.Default
    private final LocalDateTime timestamp = LocalDateTime.now();

    @JsonPOJOBuilder(withPrefix = "")
    public static class EventMessageBuilder {

    }
}

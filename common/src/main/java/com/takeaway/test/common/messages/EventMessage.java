package com.takeaway.test.common.messages;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 22/02/2019
 */

@Getter
@Setter
@Builder
public class EventMessage {
    private String uuid;
    private EventType event;
}
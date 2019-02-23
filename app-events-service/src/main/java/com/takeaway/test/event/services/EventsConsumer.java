package com.takeaway.test.event.services;

import com.takeaway.test.common.messages.EventMessage;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 23/02/2019
 */

public interface EventsConsumer {
    void processEventMessage(EventMessage event);
}

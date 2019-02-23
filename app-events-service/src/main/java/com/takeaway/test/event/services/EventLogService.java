package com.takeaway.test.event.services;

import com.takeaway.test.common.messages.EventMessage;
import com.takeaway.test.event.model.web.EventLogResponseItem;

import java.util.Collection;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 23/02/2019
 */

public interface EventLogService {
    Collection<EventLogResponseItem> listEvents(String uuid);
    void processEventMessage(EventMessage event);

}

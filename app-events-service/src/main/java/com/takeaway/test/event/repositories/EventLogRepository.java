package com.takeaway.test.event.repositories;

import com.takeaway.test.event.model.entities.EventLog;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 23/02/2019
 */

public interface EventLogRepository extends MongoRepository<EventLog, String> {
}

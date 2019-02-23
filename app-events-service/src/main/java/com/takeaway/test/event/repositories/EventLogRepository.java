package com.takeaway.test.event.repositories;

import com.takeaway.test.event.model.entities.EventLog;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 23/02/2019
 */

public interface EventLogRepository extends MongoRepository<EventLog, String> {
    List<EventLog> findEventLogsByUuid(String uuid, Sort sort);
}

package com.takeaway.test.event.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 23/02/2019
 */

@Configuration
@EnableMongoRepositories("com.takeaway.test.event.repositories")
public class MongoConfig {
}

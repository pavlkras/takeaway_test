package com.takeaway.test.event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 20/02/2019
 */

@SpringBootApplication(scanBasePackages = "com.takeaway.test.event")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

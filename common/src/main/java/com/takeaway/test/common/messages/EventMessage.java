package com.takeaway.test.common.messages;

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
 * @since 22/02/2019
 */

@Getter
@Setter
@Builder
public class EventMessage {
    private String uuid;
    private Action action;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}

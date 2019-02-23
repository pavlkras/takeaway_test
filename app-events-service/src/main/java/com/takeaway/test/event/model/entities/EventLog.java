package com.takeaway.test.event.model.entities;

import com.takeaway.test.common.messages.EventType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@Setter
@Builder
@ApiModel(description = "Response model providing event log data")
public class EventLog {
    @ApiModelProperty(required = true,
            value = "UUID of employee requested",
            readOnly = true)
    private String uuid;
    @ApiModelProperty(required = true,
            value = "Type of event",
            readOnly = true)
    private EventType eventType;
    @ApiModelProperty(required = true,
            value = "Timestamp of event",
            readOnly = true)
    private LocalDateTime timestamp;
}

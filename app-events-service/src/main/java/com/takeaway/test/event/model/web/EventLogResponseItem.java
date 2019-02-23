package com.takeaway.test.event.model.web;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.takeaway.test.common.converters.LocalDateTimeSerializer;
import com.takeaway.test.common.messages.Action;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

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
@Builder
@ApiModel(description = "Response model providing event log data")
public class EventLogResponseItem {
    @ApiModelProperty(required = true,
            value = "UUID of employee requested",
            readOnly = true)
    private String uuid;
    @ApiModelProperty(required = true,
            value = "Type of action",
            readOnly = true)
    private Action action;
    @ApiModelProperty(required = true,
            value = "Timestamp of event",
            readOnly = true)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime timestamp;
}

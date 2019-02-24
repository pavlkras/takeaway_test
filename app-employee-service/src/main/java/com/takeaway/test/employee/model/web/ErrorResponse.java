package com.takeaway.test.employee.model.web;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

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
@JsonDeserialize(builder = ErrorResponse.ErrorResponseBuilder.class)
@ApiModel(description = "Response entity when error happens")
public class ErrorResponse {
    @ApiModelProperty(required = true,
            value = "Http Status string representation",
            readOnly = true)
    private final HttpStatus status;
    @ApiModelProperty(required = true,
            value = "Error readable message",
            readOnly = true)
    private final String message;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ErrorResponseBuilder {
    }
}

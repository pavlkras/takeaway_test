package com.takeaway.test.employee.model.web;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 22/02/2019
 */

@Data
public class CreateEmployeeRequest {
    @Email
    @NotNull
    @Size(min = 3, max = 254)
    private String email;

    @NotNull
    @Size(min = 1, max = 100)
    @NotBlank
    private String firstName;

    @NotNull
    @Size(min = 1, max = 100)
    @NotBlank
    private String lastName;

    @NotNull
    @Positive
    private Integer departmentId;

    @NotNull
    @Past
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthDay;
}

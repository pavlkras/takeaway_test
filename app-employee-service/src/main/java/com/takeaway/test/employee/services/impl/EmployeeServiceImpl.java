package com.takeaway.test.employee.services.impl;

import com.takeaway.test.common.exceptions.PersistenceException;
import com.takeaway.test.common.messages.EventMessage;
import com.takeaway.test.common.messages.EventType;
import com.takeaway.test.employee.dao.EmployeeDao;
import com.takeaway.test.employee.model.entities.Employee;
import com.takeaway.test.employee.model.web.CreateEmployeeRequest;
import com.takeaway.test.employee.model.web.UpdateEmployeeRequest;
import com.takeaway.test.employee.services.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 22/02/2019
 */

@Slf4j
@EnableBinding(Source.class)
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeDao dao;
    private final Source rabbitMqSource;

    @Autowired
    public EmployeeServiceImpl(EmployeeDao dao, Source rabbitMqSource) {
        this.dao = dao;
        this.rabbitMqSource = rabbitMqSource;
    }

    @Override
    public Employee createEmployee(CreateEmployeeRequest request) throws PersistenceException {
        Employee employee = dao.create(Employee.builder()
                            .email(request.getEmail())
                            .firstName(request.getFirstName())
                            .lastName(request.getLastName())
                            .birthday(request.getBirthDay())
                            .departmentId(request.getDepartmentId())
                            .build());

        sendEventMessage(employee.getUuid(), EventType.CREATE);

        return employee;
    }

    @Override
    public Employee getEmployee(String uuid, Boolean returnExtended) throws PersistenceException {
        return dao.read(uuid, returnExtended);
    }

    @Override
    public Employee updateEmployee(UpdateEmployeeRequest request) throws PersistenceException {
        Employee employee = dao.update(Employee.builder()
                            .uuid(request.getUuid())
                            .email(request.getEmail())
                            .firstName(request.getFirstName())
                            .lastName(request.getLastName())
                            .birthday(request.getBirthDay())
                            .departmentId(request.getDepartmentId())
                            .build());

        sendEventMessage(employee.getUuid(), EventType.UPDATE);

        return employee;
    }

    @Override
    public String deleteEmployee(String uuid) throws PersistenceException {
        String deletedUuid = dao.delete(uuid);

        sendEventMessage(deletedUuid, EventType.DELETE);

        return deletedUuid;
    }

    private void sendEventMessage(String uuid, EventType eventType) {
        EventMessage message = EventMessage.builder()
                .uuid(uuid)
                .event(eventType)
                .build();
        boolean success = rabbitMqSource.output().send(MessageBuilder.withPayload(message).build());

        if (!success) {
            log.warn("Failed to send message for employee '{}' with event type '{}'", uuid, eventType);
        } else {
            log.debug("Sent message for employee '{}' with event type '{}'", uuid, eventType);
        }
    }
}

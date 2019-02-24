package com.takeaway.test.employee.services.impl;

import com.takeaway.test.common.exceptions.ConstraintException;
import com.takeaway.test.common.exceptions.PersistenceException;
import com.takeaway.test.common.exceptions.ResourceNotFoundException;
import com.takeaway.test.common.messages.Action;
import com.takeaway.test.common.messages.EventMessage;
import com.takeaway.test.employee.Application;
import com.takeaway.test.employee.dao.EmployeeDao;
import com.takeaway.test.employee.model.entities.Employee;
import com.takeaway.test.employee.model.web.CreateEmployeeRequest;
import com.takeaway.test.employee.model.web.UpdateEmployeeRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class EmployeeServiceImplTest {
    @Autowired
    private EmployeeServiceImpl employeeService;

    @MockBean
    private EmployeeDao dao;

    @MockBean
    private Source rabbitMqSource;

    @Mock
    private MessageChannel messageChannel;

    @Before
    public void setUp() {
        Mockito.when(rabbitMqSource.output()).thenReturn(messageChannel);
    }

    @Test
    public void createEmployee_validEmployee_success() throws PersistenceException {
        Mockito.when(dao.create(any(Employee.class))).thenReturn(Employee.builder().build());
        Employee employee = employeeService.createEmployee(new CreateEmployeeRequest());
        Mockito.verify(messageChannel).send(argThat((Message message) ->
                ((EventMessage)message.getPayload()).getAction().equals(Action.CREATE)));
        assertNotNull(employee);
    }

    @Test(expected = ConstraintException.class)
    public void createEmployee_invalidEmployee_throwException() throws PersistenceException {
        Mockito.when(dao.create(any(Employee.class))).thenThrow(ConstraintException.class);
        employeeService.createEmployee(new CreateEmployeeRequest());
        Mockito.verify(messageChannel, Mockito.never()).send(any());
    }

    @Test
    public void getEmployee_validUuid_success() throws PersistenceException {
        Mockito.when(dao.read(any(String.class), eq(false))).thenReturn(Employee.builder().build());
        Employee employee = employeeService.getEmployee("valid_uuid", false);
        assertNotNull(employee);
    }

    @Test
    public void getEmployee_validUuidExtended_success() throws PersistenceException {
        Mockito.when(dao.read(any(String.class), eq(true))).thenReturn(Employee.builder().build());
        Employee employee = employeeService.getEmployee("valid_uuid", true);
        assertNotNull(employee);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getEmployee_nonExistingUuid_throwException() throws PersistenceException {
        Mockito.when(dao.read(any(String.class), eq(true))).thenThrow(ResourceNotFoundException.class);
        employeeService.getEmployee("valid_uuid", true);
    }

    @Test
    public void updateEmployee_validRequest_success() throws PersistenceException {
        Mockito.when(dao.update(any(Employee.class))).thenReturn(Employee.builder().build());
        Employee employee = employeeService.updateEmployee(new UpdateEmployeeRequest());
        Mockito.verify(messageChannel).send(argThat((Message message) ->
                ((EventMessage)message.getPayload()).getAction().equals(Action.UPDATE)));
        assertNotNull(employee);
    }

    @Test(expected = ConstraintException.class)
    public void updateEmployee_invalidRequest_throwException() throws PersistenceException {
        Mockito.when(dao.update(any(Employee.class))).thenThrow(ConstraintException.class);
        employeeService.updateEmployee(new UpdateEmployeeRequest());
        Mockito.verify(messageChannel, Mockito.never()).send(any());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateEmployee_nonExistingUuid_throwException() throws PersistenceException {
        Mockito.when(dao.update(any(Employee.class))).thenThrow(ResourceNotFoundException.class);
        employeeService.updateEmployee(new UpdateEmployeeRequest());
        Mockito.verify(messageChannel, Mockito.never()).send(any());
    }

    @Test
    public void deleteEmployee_existingUuid_success() throws PersistenceException {
        Mockito.when(dao.delete(eq("uuid"))).thenReturn("uuid");
        String response = employeeService.deleteEmployee("uuid");
        Mockito.verify(messageChannel).send(argThat((Message message) ->
                ((EventMessage)message.getPayload()).getAction().equals(Action.DELETE)));
        assertEquals("uuid", response);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteEmployee_nonExistingUuid_throwException() throws PersistenceException {
        Mockito.when(dao.delete(any(String.class))).thenThrow(ResourceNotFoundException.class);
        employeeService.deleteEmployee("");
        Mockito.verify(messageChannel, Mockito.never()).send(any());
    }

}
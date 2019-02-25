package com.takeaway.test.employee.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.takeaway.test.common.messages.Action;
import com.takeaway.test.common.messages.EventMessage;
import com.takeaway.test.employee.Application;
import com.takeaway.test.employee.dao.impl.DepartmentDaoImpl;
import com.takeaway.test.employee.dao.impl.EmployeeDaoImpl;
import com.takeaway.test.employee.model.entities.Department;
import com.takeaway.test.employee.model.entities.Employee;
import com.takeaway.test.employee.model.web.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@WebAppConfiguration
@Transactional
public class EmployeesControllerTest {
    private static final String URI = "/employee";
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private DepartmentDaoImpl departmentDao;

    @Autowired
    private EmployeeDaoImpl employeeDao;

    @Autowired
    private Source rabbitMqSource;

    @Autowired
    private MessageCollector collector;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void createEmployee_validData_success() throws Exception {
        Department department = departmentDao.create(Department.builder().name("dept_name").build());
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setEmail("create_test@mail.il");
        request.setLastName("last_name");
        request.setFirstName("first_name");
        request.setBirthDay(LocalDate.of(2012, 1, 1));
        request.setDepartmentId(department.getId());
        MvcResult result = mvc.perform(post(URI + "/create").contentType(MediaType.APPLICATION_JSON).content(mapToJson(request))).andReturn();
        assertEquals(201, result.getResponse().getStatus());

        Queue<Message<?>> messages = collector.forChannel(rabbitMqSource.output());
        Message<?> message = messages.poll();
        EventMessage eventMessage = mapFromJson((String)message.getPayload(), EventMessage.class);
        assertEquals(Action.CREATE, eventMessage.getAction());
    }

    @Test
    public void createEmployee_authFailed_failed() throws Exception {
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setEmail("create_test@mail.il");
        request.setLastName("last_name");
        request.setFirstName("first_name");
        request.setBirthDay(LocalDate.of(2012, 1, 1));
        request.setDepartmentId(1);
        MvcResult result = mvc.perform(post(URI + "/create").contentType(MediaType.APPLICATION_JSON).content(mapToJson(request))).andReturn();
        assertEquals(401, result.getResponse().getStatus());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void createEmployee_validDataExtended_success() throws Exception {
        Department department = departmentDao.create(Department.builder().name("dept_name").build());
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setEmail("create_test@mail.il");
        request.setLastName("last_name");
        request.setFirstName("first_name");
        request.setBirthDay(LocalDate.of(2012, 1, 1));
        request.setDepartmentId(department.getId());
        MvcResult result = mvc.perform(post(URI + "/create").param("extended", "true").contentType(MediaType.APPLICATION_JSON).content(mapToJson(request))).andReturn();
        assertEquals(201, result.getResponse().getStatus());
        assertEquals("create_test@mail.il", mapFromJson(result.getResponse().getContentAsString(), EmployeeExtendedResponse.class).getEmail());

        Queue<Message<?>> messages = collector.forChannel(rabbitMqSource.output());
        Message<?> message = messages.poll();
        EventMessage eventMessage = mapFromJson((String)message.getPayload(), EventMessage.class);
        assertEquals(Action.CREATE, eventMessage.getAction());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void createEmployee_duplicateEmail_failed() throws Exception {
        Department department = departmentDao.create(Department.builder().name("dept_name").build());
        Employee employee = Employee.builder()
                .departmentId(department.getId())
                .birthday(LocalDate.of(2000, 1, 1))
                .lastName("last_test")
                .firstName("first_test")
                .email("duplicate_test@mail.il")
                .build();
        Employee created = employeeDao.create(employee);

        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setEmail("duplicate_test@mail.il");
        request.setLastName("last_name");
        request.setFirstName("first_name");
        request.setBirthDay(LocalDate.of(2012, 1, 1));
        request.setDepartmentId(department.getId());
        MvcResult result = mvc.perform(post(URI + "/create").param("extended", "true").contentType(MediaType.APPLICATION_JSON).content(mapToJson(request))).andReturn();
        assertEquals(400, result.getResponse().getStatus());
        assertEquals("Non unique resource provided", mapFromJson(result.getResponse().getContentAsString(), ErrorResponse.class).getMessage());

        Queue<Message<?>> messages = collector.forChannel(rabbitMqSource.output());
        assertTrue(messages.isEmpty());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void createEmployee_invalidContent_failed() throws Exception {
        Department department = departmentDao.create(Department.builder().name("dept_name").build());
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setEmail("invalidemail");
        request.setLastName("last_name");
        request.setFirstName("first_name");
        request.setBirthDay(LocalDate.of(2012, 1, 1));
        request.setDepartmentId(department.getId());
        MvcResult result = mvc.perform(post(URI + "/create").contentType(MediaType.APPLICATION_JSON).content(mapToJson(request))).andReturn();
        assertEquals(400, result.getResponse().getStatus());

        Queue<Message<?>> messages = collector.forChannel(rabbitMqSource.output());
        assertTrue(messages.isEmpty());
    }

    @Test
    public void getEmployee_existingUuid_success() throws Exception {
        Department department = departmentDao.create(Department.builder().name("dept_name").build());
        Employee employee = Employee.builder()
                .departmentId(department.getId())
                .birthday(LocalDate.of(2000, 1, 1))
                .lastName("last_test")
                .firstName("first_test")
                .email("test@email.il")
                .build();
        Employee created = employeeDao.create(employee);

        MvcResult result = mvc.perform(get(URI + "/" + created.getUuid())).andReturn();
        EmployeeExtendedResponse response = mapFromJson(result.getResponse().getContentAsString(), EmployeeExtendedResponse.class);
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(created.getUuid(), response.getUuid());
        assertNull(response.getDepartment());
    }

    @Test
    public void getEmployee_existingUuidExtended_success() throws Exception {
        Department department = departmentDao.create(Department.builder().name("dept_name").build());
        Employee employee = Employee.builder()
                .departmentId(department.getId())
                .birthday(LocalDate.of(2000, 1, 1))
                .lastName("last_test")
                .firstName("first_test")
                .email("test@email.il")
                .build();
        Employee created = employeeDao.create(employee);

        MvcResult result = mvc.perform(get(URI + "/" + created.getUuid()).param("extended", "true")).andReturn();
        EmployeeExtendedResponse response = mapFromJson(result.getResponse().getContentAsString(), EmployeeExtendedResponse.class);
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(created.getUuid(), response.getUuid());
        assertNotNull(response.getDepartment());
    }

    @Test
    public void getEmployee_nonExistingId_failed() throws Exception {
        MvcResult result = mvc.perform(get(URI + "/nonexistinguuid")).andReturn();
        assertEquals(404, result.getResponse().getStatus());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void updateEmployee_existingUuid_success() throws Exception {
        Department department = departmentDao.create(Department.builder().name("dept_name").build());
        Employee employee = Employee.builder()
                .departmentId(department.getId())
                .birthday(LocalDate.of(2000, 1, 1))
                .lastName("last_test")
                .firstName("first_test")
                .email("test@email.il")
                .build();
        Employee created = employeeDao.create(employee);
        UpdateEmployeeRequest request = new UpdateEmployeeRequest();
        request.setUuid(created.getUuid());
        request.setEmail("updated_email@mail.il");
        MvcResult result = mvc.perform(post(URI + "/update").contentType(MediaType.APPLICATION_JSON).content(mapToJson(request))).andReturn();
        EmployeeExtendedResponse response = mapFromJson(result.getResponse().getContentAsString(), EmployeeExtendedResponse.class);
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(created.getUuid(), response.getUuid());
        assertEquals("updated_email@mail.il", response.getEmail());

        Queue<Message<?>> messages = collector.forChannel(rabbitMqSource.output());
        Message<?> message = messages.poll();
        EventMessage eventMessage = mapFromJson((String)message.getPayload(), EventMessage.class);
        assertEquals(Action.UPDATE, eventMessage.getAction());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void updateEmployee_nonExistingUuid_failed() throws Exception {
        UpdateEmployeeRequest request = new UpdateEmployeeRequest();
        request.setUuid("nonexisting_uuid");
        request.setEmail("updated_email@mail.il");
        MvcResult result = mvc.perform(post(URI + "/update").contentType(MediaType.APPLICATION_JSON).content(mapToJson(request))).andReturn();
        assertEquals(404, result.getResponse().getStatus());

        Queue<Message<?>> messages = collector.forChannel(rabbitMqSource.output());
        assertTrue(messages.isEmpty());
    }

    @Test
    public void updateEmployee_authFailed_failed() throws Exception {
        UpdateEmployeeRequest request = new UpdateEmployeeRequest();
        request.setUuid("nonexisting_uuid");
        request.setEmail("updated_email@mail.il");
        MvcResult result = mvc.perform(post(URI + "/update").contentType(MediaType.APPLICATION_JSON).content(mapToJson(request))).andReturn();
        assertEquals(401, result.getResponse().getStatus());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void deleteEmployee_existingUuid_success() throws Exception {
        Department department = departmentDao.create(Department.builder().name("dept_name").build());
        Employee employee = Employee.builder()
                .departmentId(department.getId())
                .birthday(LocalDate.of(2000, 1, 1))
                .lastName("last_test")
                .firstName("first_test")
                .email("test@email.il")
                .build();
        Employee created = employeeDao.create(employee);
        MvcResult result = mvc.perform(delete(URI + "/" + created.getUuid())).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(created.getUuid(), result.getResponse().getContentAsString());

        Queue<Message<?>> messages = collector.forChannel(rabbitMqSource.output());
        Message<?> message = messages.poll();
        EventMessage eventMessage = mapFromJson((String)message.getPayload(), EventMessage.class);
        assertEquals(Action.DELETE, eventMessage.getAction());
    }

    @Test
    public void deleteEmployee_authFailed_failed() throws Exception {
        MvcResult result = mvc.perform(delete(URI + "/some-uuid")).andReturn();
        assertEquals(401, result.getResponse().getStatus());
    }

    private <T> T mapFromJson(String json, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        return objectMapper.readValue(json, clazz);
    }

    private <T> String mapToJson(T entity) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(entity);
    }

}
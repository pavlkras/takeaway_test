package com.takeaway.test.event.controllers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.takeaway.test.common.messages.Action;
import com.takeaway.test.event.Application;
import com.takeaway.test.event.model.entities.EventLog;
import com.takeaway.test.event.repositories.EventLogRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@WebAppConfiguration
public class EventLogControllerTest {
    private static final String URI = "/events/";
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private EventLogRepository eventLogRepository;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void listEventLogs_notPersistedUserEvents_emptyArrayResponse() throws Exception {
        final String uuid = "nonexistentid";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI + uuid)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("[]", content);
    }

    @Test
    public void listEventLogs_persistedUserEvents_oneItemResponse() throws Exception {
        final String uuid = UUID.randomUUID().toString();
        EventLog eventLog = EventLog.builder()
                .uuid(uuid)
                .action(Action.CREATE)
                .timestamp(LocalDateTime.now())
                .build();
        eventLogRepository.insert(eventLog);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI + uuid)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        EventLog[] eventLogList = mapFromJson(content, EventLog[].class);

        assertEquals(1, eventLogList.length);
    }

    @Test
    public void listEventLogs_persistedUserEvents_twoItemsResponse() throws Exception {
        final String uuid = UUID.randomUUID().toString();
        EventLog eventLog = EventLog.builder()
                .uuid(uuid)
                .action(Action.CREATE)
                .timestamp(LocalDateTime.now())
                .build();
        eventLogRepository.insert(eventLog);
        eventLogRepository.insert(eventLog);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI + uuid)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        EventLog[] eventLogList = mapFromJson(content, EventLog[].class);

        assertEquals(2, eventLogList.length);
    }


    private <T> T mapFromJson(String json, Class<T> clazz) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        return objectMapper.readValue(json, clazz);
    }
}
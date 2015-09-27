package com.sandh.billsplitter.mvc;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.context.WebApplicationContext;
import static org.hamcrest.Matchers.*;

import java.net.URI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


/**
 * Created by user on 21/09/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class BillControllerTests {
    private MockMvc mockMvc;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void badMethod() throws Exception {
        mockMvc.perform(get("/bill/add/"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void badRequest() throws Exception {
        mockMvc.perform(put(new URI("/bill/add/")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createBill() throws Exception {
        mockMvc.perform(put(new URI("/bill/create/")))
               .andExpect(status().isOk());
    }

    @Test
    public void addFoodToBill() throws Exception {
        MvcResult result = mockMvc.perform(put(new URI("/bill/create/"))).andReturn();
        String content = result.getResponse().getContentAsString();
        mockMvc.perform(put(new URI("/bill/add/?food=ghormeh&price=11.0&billId="+content)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..items[0].price", is(11.0)));
    }
}
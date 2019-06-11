package com.zenika.academy.WoodBlockToys.bloc.woodessence;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "dev")
@SpringBootTest()
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WoodEssenceControllerTest {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    // This object will be magically initialized by the initFields method below.
    private JacksonTester<WoodEssence> jsonWoodEssence;

    private JacksonTester<Iterable<WoodEssence>> jsonWoodEssenceIterable;


    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void badMessageTest() throws Exception {
        // given
        WoodEssence woodEssence = WoodEssence.builder().build();

        // when
        MockHttpServletResponse postAccountResponse = mvc.perform(
                post("/woodEssences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWoodEssence.write(woodEssence).getJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(postAccountResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void basicTest() throws Exception {
        //given
        WoodEssence woodEssence = WoodEssence.builder()
                .type("oak")
                .price(0.0009)
                .build();

        WoodEssence updatedWoodEssence = WoodEssence.builder()
                .type("oak")
                .price(0.0015)
                .build();


        //when
        MockHttpServletResponse postWoodEssenceResponse = mvc.perform(
                post("/woodEssences")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonWoodEssence.write(woodEssence).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        WoodEssence returnedWoodEssence = jsonWoodEssence.parseObject(postWoodEssenceResponse.getContentAsString());

        MockHttpServletResponse getResponse = mvc.perform(
                get("/woodEssences/" + returnedWoodEssence.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse listResponse = mvc.perform(
                get("/woodEssences")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse putResponse = mvc.perform(
                put("/woodEssences/" + returnedWoodEssence.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonWoodEssence.write(updatedWoodEssence).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse getAfterUpdateResponse = mvc.perform(
                get("/woodEssences/" + returnedWoodEssence.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse deleteResponse = mvc.perform(
                delete("/woodEssences/" + returnedWoodEssence.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        // then
        assertThat(postWoodEssenceResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(getResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(listResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(putResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(getAfterUpdateResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(deleteResponse.getStatus()).isEqualTo(HttpStatus.ACCEPTED.value());

        woodEssence.setId(returnedWoodEssence.getId());
        updatedWoodEssence.setId(returnedWoodEssence.getId());

        assertThat(returnedWoodEssence).isEqualTo(woodEssence);


        assertThat(jsonWoodEssence.parseObject(postWoodEssenceResponse.getContentAsString()))
                .isEqualTo(woodEssence);

        assertThat(jsonWoodEssence.parseObject(getResponse.getContentAsString()))
                .isEqualTo(woodEssence);

        assertThat(jsonWoodEssenceIterable.parseObject(listResponse.getContentAsString()))
                .isEqualTo(List.of(woodEssence));

        assertThat(jsonWoodEssence.parseObject(putResponse.getContentAsString()))
                .isEqualTo(updatedWoodEssence);

        assertThat(jsonWoodEssence.parseObject(getAfterUpdateResponse.getContentAsString()))
                .isEqualTo(updatedWoodEssence);

    }

    @Test
    public void addExistentWoodEssenceTest() throws Exception {
        //given
        WoodEssence woodEssence = WoodEssence.builder()
                .type("oak")
                .price(0.0009)
                .build();
        //when
        MockHttpServletResponse postWoodEssenceResponse = mvc.perform(
                post("/woodEssences")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonWoodEssence.write(woodEssence).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse postTheSameWoodEssenceResponse = mvc.perform(
                post("/woodEssences")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonWoodEssence.write(woodEssence).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        //then
        assertThat(postWoodEssenceResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(postTheSameWoodEssenceResponse.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void updateNotExistentWoodEssence() throws Exception {
        //given
        WoodEssence woodEssence = WoodEssence.builder()
                .type("oak")
                .price(0.0009)
                .build();
        //when
        MockHttpServletResponse postWoodEssenceResponse = mvc.perform(
                post("/woodEssences")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonWoodEssence.write(woodEssence).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse putResponse = mvc.perform(
                put("/woodEssences/2")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonWoodEssence.write(woodEssence).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        //then

        assertThat(postWoodEssenceResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(putResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void deleteNotExistentWoodEssence() throws Exception {
        //given
        WoodEssence woodEssence = WoodEssence.builder()
                .type("oak")
                .price(0.0009)
                .build();
        //when
        MockHttpServletResponse postWoodEssenceResponse = mvc.perform(
                post("/woodEssences")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonWoodEssence.write(woodEssence).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse deleteResponse = mvc.perform(
                delete("/woodEssences/2")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonWoodEssence.write(woodEssence).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        //then

        assertThat(postWoodEssenceResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(deleteResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void getNotExistentWoodEssence() throws Exception {
        //given
        WoodEssence woodEssence = WoodEssence.builder()
                .type("oak")
                .price(0.0009)
                .build();
        //when
        MockHttpServletResponse postWoodEssenceResponse = mvc.perform(
                post("/woodEssences")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonWoodEssence.write(woodEssence).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse putResponse = mvc.perform(
                get("/woodEssences/2")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonWoodEssence.write(woodEssence).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        //then

        assertThat(postWoodEssenceResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(putResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

}
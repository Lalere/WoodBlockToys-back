package com.zenika.academy.WoodBlockToys.barrel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenika.academy.WoodBlockToys.bloc.woodessence.WoodEssence;
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
public class BarrelControllerTest {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    // This object will be magically initialized by the initFields method below.
    private JacksonTester<Barrel> jsonBarrel;

    private JacksonTester<Iterable<Barrel>> jsonBarrelIterable;

    private JacksonTester<WoodEssence> jsonWoodEssence;


    @Before
    public void setup() throws Exception {
        JacksonTester.initFields(this, new ObjectMapper());
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
        WoodEssence oak = WoodEssence.builder().price(900).type("oak").build();
        WoodEssence pine = WoodEssence.builder().price(300).type("pine").build();
        WoodEssence beech = WoodEssence.builder().price(600).type("beech").build();
        WoodEssence mahogany = WoodEssence.builder().price(40000).type("mahogany").build();

        MockHttpServletResponse postOakResponse = mvc.perform(
                post("/woodEssences")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonWoodEssence.write(oak).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse postPineResponse = mvc.perform(
                post("/woodEssences")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonWoodEssence.write(pine).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse postBeechResponse = mvc.perform(
                post("/woodEssences")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonWoodEssence.write(beech).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse postMahoganyResponse = mvc.perform(
                post("/woodEssences")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonWoodEssence.write(mahogany).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();
    }

    @Test
    public void badMessageTest() throws Exception {
        // given
        Barrel barrel = Barrel.builder().build();

        // when
        MockHttpServletResponse postAccountResponse = mvc.perform(
                post("/barrels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBarrel.write(barrel).getJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(postAccountResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void basicTest() throws Exception {
        //given
        Barrel barrel = Barrel.builder()
                .price(50d)
                .numberOfPieces(3)
                .finish("raw")
                .build();


        //when
        MockHttpServletResponse postBarrelResponse = mvc.perform(
                post("/barrels")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonBarrel.write(barrel).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        Barrel returnedBarrel = jsonBarrel.parseObject(postBarrelResponse.getContentAsString());

        MockHttpServletResponse getResponse = mvc.perform(
                get("/barrels/" + returnedBarrel.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse listResponse = mvc.perform(
                get("/barrels")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();


        MockHttpServletResponse getAfterUpdateResponse = mvc.perform(
                get("/barrels/" + returnedBarrel.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse deleteResponse = mvc.perform(
                delete("/barrels/" + returnedBarrel.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        // then
        assertThat(postBarrelResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(getResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(listResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(getAfterUpdateResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(deleteResponse.getStatus()).isEqualTo(HttpStatus.ACCEPTED.value());


        assertThat(jsonBarrel.parseObject(getResponse.getContentAsString()))
                .isEqualTo(returnedBarrel);

        assertThat(jsonBarrelIterable.parseObject(listResponse.getContentAsString()))
                .isEqualTo(List.of(returnedBarrel));
    }


    @Test
    public void deleteNotExistentBarrel() throws Exception {
        //given
        Barrel barrel = Barrel.builder()
                .price(50d)
                .numberOfPieces(3)
                .finish("raw")
                .build();

        //when
        MockHttpServletResponse postBarrelResponse = mvc.perform(
                post("/barrels")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonBarrel.write(barrel).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse deleteResponse = mvc.perform(
                delete("/barrels/2")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonBarrel.write(barrel).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        //then

        assertThat(postBarrelResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(deleteResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void getNotExistentBarrel() throws Exception {
        //given
        Barrel barrel = Barrel.builder()
                .price(50d)
                .numberOfPieces(3)
                .finish("raw")
                .build();

        //when
        MockHttpServletResponse postBarrelResponse = mvc.perform(
                post("/barrels")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonBarrel.write(barrel).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse getResponse = mvc.perform(
                get("/barrels/2")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonBarrel.write(barrel).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        //then

        assertThat(postBarrelResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(getResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void aberrantWishedBarrelValues() throws Exception {
        //given
        Barrel barrel = Barrel.builder()
                .price(50d)
                .numberOfPieces(30000)
                .finish("raw")
                .build();

        //when
        MockHttpServletResponse postBarrelResponse = mvc.perform(
                post("/barrels")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonBarrel.write(barrel).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        //then
        assertThat(postBarrelResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
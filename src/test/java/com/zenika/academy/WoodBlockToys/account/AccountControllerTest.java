package com.zenika.academy.WoodBlockToys.account;

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
public class AccountControllerTest {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    // This object will be magically initialized by the initFields method below.
    private JacksonTester<Account> jsonAccount;

    private JacksonTester<Iterable<Account>> jsonAccountIterable;


    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void badMessageTest() throws Exception {
        // given
        Account account = Account.builder().build();

        // when
        MockHttpServletResponse postAccountResponse = mvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAccount.write(account).getJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(postAccountResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void basicTest() throws Exception {
        //given
        Account account = Account.builder()
                .firstname("toto")
                .lastname("tata")
                .address("71 rue de toto")
                .email("toto@gmail.com")
                .password("123456")
                .phone("01.02.03.04.05")
                .build();

        Account updatedAccount = Account.builder()
                .firstname("toto")
                .lastname("tata")
                .address("71 rue Boutros")
                .email("toto@gmail.com")
                .password("123456")
                .phone("01.02.03.04.05")
                .build();


        //when
        MockHttpServletResponse postAccountResponse = mvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonAccount.write(account).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        Account returnedAccount = jsonAccount.parseObject(postAccountResponse.getContentAsString());

        MockHttpServletResponse getResponse = mvc.perform(
                get("/accounts/" + returnedAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse listResponse = mvc.perform(
                get("/accounts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse putResponse = mvc.perform(
                put("/accounts/" + returnedAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonAccount.write(updatedAccount).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse getAfterUpdateResponse = mvc.perform(
                get("/accounts/" + returnedAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse deleteResponse = mvc.perform(
                delete("/accounts/" + returnedAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        // then
        assertThat(postAccountResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(getResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(listResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(putResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(getAfterUpdateResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(deleteResponse.getStatus()).isEqualTo(HttpStatus.ACCEPTED.value());

        account.setId(returnedAccount.getId());
        updatedAccount.setId(returnedAccount.getId());

        assertThat(returnedAccount).isEqualTo(account);


        assertThat(jsonAccount.parseObject(postAccountResponse.getContentAsString()))
                .isEqualTo(account);

        assertThat(jsonAccount.parseObject(getResponse.getContentAsString()))
                .isEqualTo(account);

        assertThat(jsonAccountIterable.parseObject(listResponse.getContentAsString()))
                .isEqualTo(List.of(account));

        assertThat(jsonAccount.parseObject(putResponse.getContentAsString()))
                .isEqualTo(updatedAccount);

        assertThat(jsonAccount.parseObject(getAfterUpdateResponse.getContentAsString()))
                .isEqualTo(updatedAccount);

    }

    @Test
    public void addExistentAccountTest() throws Exception {
        //given
        Account account = Account.builder()
                .firstname("toto")
                .lastname("tata")
                .address("71 rue de toto")
                .email("toto@gmail.com")
                .password("123456")
                .phone("01.02.03.04.05")
                .build();
        //when
        MockHttpServletResponse postAccountResponse = mvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonAccount.write(account).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse postTheSameAccountResponse = mvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonAccount.write(account).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        //then
        assertThat(postAccountResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(postTheSameAccountResponse.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void updateNotExistentAccount() throws Exception {
        //given
        Account account = Account.builder()
                .firstname("toto")
                .lastname("tata")
                .address("71 rue de toto")
                .email("toto@gmail.com")
                .password("123456")
                .phone("01.02.03.04.05")
                .build();
        //when
        MockHttpServletResponse postAccountResponse = mvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonAccount.write(account).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse putResponse = mvc.perform(
                put("/accounts/2")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonAccount.write(account).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        //then

        assertThat(postAccountResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(putResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void deleteNotExistentAccount() throws Exception {
        //given
        Account account = Account.builder()
                .firstname("toto")
                .lastname("tata")
                .address("71 rue de toto")
                .email("toto@gmail.com")
                .password("123456")
                .phone("01.02.03.04.05")
                .build();
        //when
        MockHttpServletResponse postAccountResponse = mvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonAccount.write(account).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse deleteResponse = mvc.perform(
                delete("/accounts/2")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonAccount.write(account).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        //then

        assertThat(postAccountResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(deleteResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void getNotExistentAccount() throws Exception {
        //given
        Account account = Account.builder()
                .firstname("toto")
                .lastname("tata")
                .address("71 rue de toto")
                .email("toto@gmail.com")
                .password("123456")
                .phone("01.02.03.04.05")
                .build();
        //when
        MockHttpServletResponse postAccountResponse = mvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonAccount.write(account).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse putResponse = mvc.perform(
                get("/accounts/2")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonAccount.write(account).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        //then

        assertThat(postAccountResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(putResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
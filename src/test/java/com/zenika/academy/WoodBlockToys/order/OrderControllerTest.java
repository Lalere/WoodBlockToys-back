package com.zenika.academy.WoodBlockToys.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenika.academy.WoodBlockToys.account.Account;
import com.zenika.academy.WoodBlockToys.barrel.Barrel;
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
public class OrderControllerTest {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    // This object will be magically initialized by the initFields method below.
    private JacksonTester<Order> jsonOrder;
    private JacksonTester<Account> jsonAccount;
    private JacksonTester<Barrel> jsonBarrel;
    private JacksonTester<Iterable<Order>> jsonOrderIterable;
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
        Order order = Order.builder().build();

        // when
        MockHttpServletResponse postAccountResponse = mvc.perform(
                post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOrder.write(order).getJson())
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

        Barrel barrel = Barrel.builder()
                .price(50d)
                .numberOfPieces(10)
                .finish("raw")
                .build();


        //when
        MockHttpServletResponse postAccountResponse = mvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonAccount.write(account).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        Account returnedAccount = jsonAccount.parseObject(postAccountResponse.getContentAsString());
        account.setId(returnedAccount.getId());

        MockHttpServletResponse postBarrelResponse = mvc.perform(
                post("/barrels")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonBarrel.write(barrel).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();


        Barrel returnedBarrel = jsonBarrel.parseObject(postBarrelResponse.getContentAsString());
        barrel.setId(returnedBarrel.getId());


        Order order = Order.builder()
                .price(returnedBarrel.getPrice())
                .account(account)
                .barrel(returnedBarrel)
                .build();

        Order updatedOrder = Order.builder()
                .price(returnedBarrel.getPrice())
                .status(OrderStatus.IN_CONSTRUCTION)
                .account(account)
                .barrel(returnedBarrel)
                .build();

        MockHttpServletResponse postOrderResponse = mvc.perform(
                post("/orders")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonOrder.write(order).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        Order returnedOrder = jsonOrder.parseObject(postOrderResponse.getContentAsString());

        MockHttpServletResponse getResponse = mvc.perform(
                get("/orders/" + returnedOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse listResponse = mvc.perform(
                get("/orders")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();


        MockHttpServletResponse putResponse = mvc.perform(
                put("/orders/" + returnedOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonOrder.write(updatedOrder).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse getAfterUpdateResponse = mvc.perform(
                get("/orders/" + returnedOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse deleteResponse = mvc.perform(
                delete("/orders/" + returnedOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        // then
        assertThat(postAccountResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(postBarrelResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(postOrderResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(getResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(listResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(putResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(getAfterUpdateResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(deleteResponse.getStatus()).isEqualTo(HttpStatus.ACCEPTED.value());

        order.setId(returnedOrder.getId());
        updatedOrder.setId(returnedOrder.getId());

        assertThat(returnedOrder).isEqualTo(order);


        assertThat(jsonOrder.parseObject(postOrderResponse.getContentAsString()))
                .isEqualTo(order);

        assertThat(jsonOrder.parseObject(getResponse.getContentAsString()))
                .isEqualTo(order);

        assertThat(jsonOrderIterable.parseObject(listResponse.getContentAsString()))
                .isEqualTo(List.of(order));

        assertThat(jsonOrder.parseObject(putResponse.getContentAsString()))
                .isEqualTo(updatedOrder);

        assertThat(jsonOrder.parseObject(getAfterUpdateResponse.getContentAsString()))
                .isEqualTo(updatedOrder);

    }

    @Test
    public void addExistentOrderTest() throws Exception {
        //given
        Account account = Account.builder()
                .firstname("toto")
                .lastname("tata")
                .address("71 rue de toto")
                .email("toto@gmail.com")
                .password("123456")
                .phone("01.02.03.04.05")
                .build();

        Barrel barrel = Barrel.builder()
                .price(50d)
                .numberOfPieces(10)
                .finish("raw")
                .build();


        //when
        MockHttpServletResponse postAccountResponse = mvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonAccount.write(account).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        Account returnedAccount = jsonAccount.parseObject(postAccountResponse.getContentAsString());
        account.setId(returnedAccount.getId());

        MockHttpServletResponse postBarrelResponse = mvc.perform(
                post("/barrels")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonBarrel.write(barrel).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();


        Barrel returnedBarrel = jsonBarrel.parseObject(postBarrelResponse.getContentAsString());
        barrel.setId(returnedBarrel.getId());

        Order order = Order.builder()
                .price(barrel.getPrice())
                .account(account)
                .barrel(barrel)
                .build();

        MockHttpServletResponse postOrderResponse = mvc.perform(
                post("/orders")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonOrder.write(order).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse postTheSameOrderResponse = mvc.perform(
                post("/orders")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonOrder.write(order).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        //then
        assertThat(postAccountResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(postBarrelResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(postOrderResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(postTheSameOrderResponse.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void updateNotExistentOrder() throws Exception {
        //given
        Account account = Account.builder()
                .firstname("toto")
                .lastname("tata")
                .address("71 rue de toto")
                .email("toto@gmail.com")
                .password("123456")
                .phone("01.02.03.04.05")
                .build();

        Barrel barrel = Barrel.builder()
                .price(50d)
                .numberOfPieces(10)
                .finish("raw")
                .build();


        //when
        MockHttpServletResponse postAccountResponse = mvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonAccount.write(account).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        Account returnedAccount = jsonAccount.parseObject(postAccountResponse.getContentAsString());
        account.setId(returnedAccount.getId());

        MockHttpServletResponse postBarrelResponse = mvc.perform(
                post("/barrels")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonBarrel.write(barrel).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();


        Barrel returnedBarrel = jsonBarrel.parseObject(postBarrelResponse.getContentAsString());
        barrel.setId(returnedBarrel.getId());

        Order order = Order.builder()
                .price(barrel.getPrice())
                .account(account)
                .barrel(barrel)
                .build();

        MockHttpServletResponse postOrderResponse = mvc.perform(
                post("/orders")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonOrder.write(order).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse putResponse = mvc.perform(
                put("/orders/2")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonOrder.write(order).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        //then
        assertThat(postAccountResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(postBarrelResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(postOrderResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(putResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void deleteNotExistentOrder() throws Exception {
        //given
        Account account = Account.builder()
                .firstname("toto")
                .lastname("tata")
                .address("71 rue de toto")
                .email("toto@gmail.com")
                .password("123456")
                .phone("01.02.03.04.05")
                .build();

        Barrel barrel = Barrel.builder()
                .price(50d)
                .numberOfPieces(3)
                .finish("raw")
                .build();


        //when
        MockHttpServletResponse postAccountResponse = mvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonAccount.write(account).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        Account returnedAccount = jsonAccount.parseObject(postAccountResponse.getContentAsString());
        account.setId(returnedAccount.getId());

        MockHttpServletResponse postBarrelResponse = mvc.perform(
                post("/barrels")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonBarrel.write(barrel).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();


        Barrel returnedBarrel = jsonBarrel.parseObject(postBarrelResponse.getContentAsString());
        barrel.setId(returnedBarrel.getId());

        Order order = Order.builder()
                .price(barrel.getPrice())
                .account(account)
                .barrel(barrel)
                .build();

        MockHttpServletResponse postOrderResponse = mvc.perform(
                post("/orders")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonOrder.write(order).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse deleteResponse = mvc.perform(
                delete("/orders/2")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonOrder.write(order).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        //then
        assertThat(postAccountResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(postBarrelResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(postOrderResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(deleteResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void getNotExistentOrder() throws Exception {
        //given
        Account account = Account.builder()
                .firstname("toto")
                .lastname("tata")
                .address("71 rue de toto")
                .email("toto@gmail.com")
                .password("123456")
                .phone("01.02.03.04.05")
                .build();

        Barrel barrel = Barrel.builder()
                .price(50d)
                .numberOfPieces(3)
                .finish("raw")
                .build();


        //when
        MockHttpServletResponse postAccountResponse = mvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonAccount.write(account).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        Account returnedAccount = jsonAccount.parseObject(postAccountResponse.getContentAsString());
        account.setId(returnedAccount.getId());

        MockHttpServletResponse postBarrelResponse = mvc.perform(
                post("/barrels")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonBarrel.write(barrel).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();


        Barrel returnedBarrel = jsonBarrel.parseObject(postBarrelResponse.getContentAsString());
        barrel.setId(returnedBarrel.getId());

        Order order = Order.builder()
                .price(barrel.getPrice())
                .account(account)
                .barrel(barrel)
                .build();

        MockHttpServletResponse postOrderResponse = mvc.perform(
                post("/orders")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonOrder.write(order).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        MockHttpServletResponse putResponse = mvc.perform(
                get("/orders/2")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonOrder.write(order).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        //then
        assertThat(postAccountResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(postBarrelResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(postOrderResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(putResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void orderPrice() throws Exception {
        //given
        Account account = Account.builder()
                .firstname("toto")
                .lastname("tata")
                .address("71 rue de toto")
                .email("toto@gmail.com")
                .password("123456")
                .phone("01.02.03.04.05")
                .build();

        Barrel barrel = Barrel.builder()
                .price(50d)
                .numberOfPieces(3)
                .finish("raw")
                .build();


        //when
        MockHttpServletResponse postAccountResponse = mvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonAccount.write(account).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        Account returnedAccount = jsonAccount.parseObject(postAccountResponse.getContentAsString());
        account.setId(returnedAccount.getId());

        MockHttpServletResponse postBarrelResponse = mvc.perform(
                post("/barrels")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonBarrel.write(barrel).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();


        Barrel returnedBarrel = jsonBarrel.parseObject(postBarrelResponse.getContentAsString());
        barrel.setId(returnedBarrel.getId());
        barrel.setPrice(returnedBarrel.getPrice());

        Order order = Order.builder()
                .price(barrel.getPrice())
                .account(account)
                .barrel(barrel)
                .build();

        MockHttpServletResponse postOrderResponse = mvc.perform(
                post("/orders")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonOrder.write(order).getJson())
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        Order returnedOrder = jsonOrder.parseObject(postOrderResponse.getContentAsString());
        order.setId(returnedOrder.getId());


        //then
        assertThat(postAccountResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(postBarrelResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(postOrderResponse.getStatus()).isEqualTo(HttpStatus.OK.value());

        assertThat(jsonOrder.parseObject(postOrderResponse.getContentAsString()))
                .isEqualTo(order);
        Order finalOrder = jsonOrder.parseObject(postOrderResponse.getContentAsString());
        Barrel finalBarrel = jsonBarrel.parseObject(postBarrelResponse.getContentAsString());

        assertThat(finalOrder.getBarrel().getPrice()).isEqualTo(finalBarrel.getPrice());
        assertThat(finalOrder.getPrice()).isLessThanOrEqualTo(barrel.getPrice());
    }
}
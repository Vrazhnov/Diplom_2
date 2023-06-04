package praktikum.order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.user.UserData;
import praktikum.user.UserService;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class OrderCreationTest {
    private OrderData orderData;
    private OrderService orderService;
    private UserData userData;
    private UserService userService;
    private Response response;
    private String accessToken;


    @Before
    public void setUp() {
        userData = new UserData(RandomStringUtils.randomAlphabetic(6, 10) + "@ya.ru", RandomStringUtils.randomAlphabetic(6, 10), RandomStringUtils.randomAlphabetic(6, 10));
        userService = new UserService();
        response = userService.createUser(userData);
        accessToken = response.then().extract().body().path("accessToken");
        orderService = new OrderService();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userService.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание заказа с ингридиентами и авторизацей пользователя")
    public void createOrderWithIngredientsAndAuth() {
        orderData = new OrderData(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa76"));
        orderService
                .createOrderWithAuth(orderData, accessToken)
                .then()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с ингридиентами без авторизации пользователя")
    public void createOrderWithIngredientsAndWithoutAuth() {
        orderData = new OrderData(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa76"));
        orderService
                .createOrderWithoutAuth(orderData)
                .then()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингридиентов с авторизацей пользователя")
    public void createOrderWithoutIngredientsAndAuth() {
        orderData = new OrderData(List.of());
        orderService
                .createOrderWithAuth(orderData, accessToken)
                .then()
                .statusCode(400)
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа без ингридиентов без авторизации пользователя")
    public void createOrderWithoutIngredientsAndWithoutAuth() {
        orderData = new OrderData(List.of());
        orderService
                .createOrderWithoutAuth(orderData)
                .then()
                .statusCode(400)
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с некорректным хэшем ингридиента с авторизацей пользователя")
    public void createOrderWithIncorrectIngredientsAndAuth() {
        orderData = new OrderData(List.of("fk8678er738owjqijg"));
        orderService
                .createOrderWithAuth(orderData, accessToken)
                .then()
                .statusCode(500);
    }
}

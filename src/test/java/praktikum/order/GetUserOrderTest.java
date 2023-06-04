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
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetUserOrderTest {

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
        orderData = new OrderData(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa76"));
        orderService = new OrderService();
        orderService.createOrderWithAuth(orderData, accessToken);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userService.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Получение заказов пользователя с авторизацией")
    public void getUserOrdersWithAuth() {
        orderService
                .getUserOrdersWithAuth(accessToken)
                .then()
                .statusCode(200)
                .and()
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Получение заказов пользователя без авторизации")
    public void getUserOrdersWithoutAuth() {
        orderService
                .getUserOrdersWithoutAuth()
                .then()
                .statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));
    }

}

package praktikum.order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.config.Config;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;

public class OrderService {

    @Step("Получение списка ингредиентов")
    public ArrayList getIngredients() {
        ArrayList<String> ingredients = given()
                .when()
                .get(Config.BASE_URI + Config.INGREDIENTS_URL)
                .then()
                .extract()
                .path("data._id");
        return ingredients;
    }

    @Step("Создание заказа с авторизацией")
    public Response createOrderWithAuth(OrderData orderData, String accessToken) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .body(orderData)
                .post(Config.BASE_URI + Config.ORDERS);
    }

    @Step("Создание заказа без авторизациии")
    public Response createOrderWithoutAuth(OrderData orderData) {
        return given()
                .header("Content-Type", "application/json")
                .and()
                .when()
                .body(orderData)
                .when()
                .post(Config.BASE_URI + Config.ORDERS);
    }

    @Step("Получение списка заказов пользователя с авторизацией")
    public Response getUserOrdersWithAuth(String accessToken) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .get(Config.BASE_URI + Config.ORDERS);
    }

    @Step("Получение списка заказов пользователя с авторизацией")
    public Response getUserOrdersWithoutAuth() {
        return given()
                .header("Content-Type", "application/json")
                .get(Config.BASE_URI + Config.ORDERS);
    }
}

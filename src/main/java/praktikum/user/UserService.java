package praktikum.user;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.config.Config;

import static io.restassured.RestAssured.given;

public class UserService {

    @Step("Создание пользователя")
    public Response createUser(UserData userData) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(userData)
                .when()
                .post(Config.BASE_URI + Config.REGISTER_PATH);
    }

    @Step("Удаление пользователя")
    public void deleteUser(String accessToken) {
        given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .delete(Config.BASE_URI + Config.USER_PATH + accessToken)
                .then();
    }

    @Step("Логин пользователя c токеном")
    public Response loginUser(UserData userData){
        return given()
                .header("Content-type", "application/json")
                .body(userData)
                .when()
                .post(Config.BASE_URI + Config.LOGIN_PATH);
    }

    @Step("Логин пользователя без токена")
    public Response loginUser(UserData userData, String accessToken){
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(userData)
                .when()
                .post(Config.BASE_URI + Config.LOGIN_PATH);
    }

    @Step("Изменение данных пользователя с авторизацией")
    public Response changeUserDataWithAuth(UserData UserData, String accessToken){
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .and()
                .when()
                .body(UserData)
                .when()
                .patch(Config.BASE_URI + Config.USER_PATH);
    }
    @Step("Изменение данных пользователя без авторизации")
    public Response changeUserDataWithoutAuth(UserData UserData){
        return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .body(UserData)
                .when()
                .patch(Config.BASE_URI + Config.USER_PATH);
    }
}

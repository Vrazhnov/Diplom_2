package praktikum.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class ChangeUserDataTest {

    private UserService userService;
    private UserData userData;
    private Response response;
    private String accessToken;

    @Before
    public void setUp() {
        userData = new UserData(RandomStringUtils.randomAlphabetic(6,10) + "@ya.ru", RandomStringUtils.randomAlphabetic(6,10), RandomStringUtils.randomAlphabetic(6,10));
        userService = new UserService();
        response = userService.createUser(userData);
        accessToken = response.then().extract().body().path("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userService.deleteUser(accessToken);
        }
}

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void changeUserDataWithAuthTest() {
        userService
                .changeUserDataWithAuth(userData, accessToken)
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void changeUserDataWithoutAuthTest() {
        userService
                .changeUserDataWithoutAuth(userData)
                .then()
                .assertThat()
                .body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }

}

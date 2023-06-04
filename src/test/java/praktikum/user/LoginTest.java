package praktikum.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class LoginTest {

    private UserService userService;
    private UserData userData;
    private Response response;
    private String accessToken;

    @Before
    public void setUp() {
        userData = new UserData(RandomStringUtils.randomAlphabetic(6, 10) + "@ya.ru", RandomStringUtils.randomAlphabetic(6, 10), RandomStringUtils.randomAlphabetic(6, 10));
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
    @DisplayName("Логин под существующим пользователем")
    public void loginByExistentUser() {
        userService
                .loginUser(userData)
                .then()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    public void loginByNonexistentUser() {
        userData = new UserData(RandomStringUtils.randomAlphabetic(6, 10) + "@ya.ru", RandomStringUtils.randomAlphabetic(6, 10), RandomStringUtils.randomAlphabetic(6, 10));
        userService
                .loginUser(userData)
                .then()
                .statusCode(401)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

}

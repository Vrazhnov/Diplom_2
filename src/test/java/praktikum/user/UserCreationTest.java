package praktikum.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class UserCreationTest {

    private UserService userService;
    private UserData userData;
    private Response response;
    private String accessToken;

    @Before
    public void setUp() {
        userService = new UserService();
    }

    @After
    public void tearDown() {
        response = userService.loginUser(userData);
        accessToken = response.then().extract().body().path("accessToken");
        if (accessToken != null) {
            userService.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание нового пользователя с уникальным логином")
    public void createNewUser() {
        userData = new UserData(RandomStringUtils.randomAlphabetic(6, 10) + "@ya.ru", RandomStringUtils.randomAlphabetic(6, 10), RandomStringUtils.randomAlphabetic(6, 10));
        userService
                .createUser(userData)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание уже существующего пользователя")
    public void createNewUserWithExistentLogin() {
        userData = new UserData(RandomStringUtils.randomAlphabetic(6, 10) + "@ya.ru", RandomStringUtils.randomAlphabetic(6, 10), RandomStringUtils.randomAlphabetic(6, 10));
        userService
                .createUser(userData);
        userService
                .createUser(userData)
                .then()
                .assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без заполнения обязательного поля email")
    public void createNewUserWithoutEmail() {
        userData = new UserData(null, RandomStringUtils.randomAlphabetic(6, 10), RandomStringUtils.randomAlphabetic(6, 10));
        userService
                .createUser(userData)
                .then()
                .assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без заполнения обязательного поля пароль")
    public void createNewUserWithoutPassword() {
        userData = new UserData(RandomStringUtils.randomAlphabetic(6, 10) + "@ya.ru", null, RandomStringUtils.randomAlphabetic(6, 10));
        userService
                .createUser(userData)
                .then()
                .assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без заполнения обязательного поля Имя")
    public void createNewUserWithoutName() {
        userData = new UserData(RandomStringUtils.randomAlphabetic(6, 10) + "@ya.ru", RandomStringUtils.randomAlphabetic(6, 10), null);
        userService
                .createUser(userData)
                .then()
                .assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

}
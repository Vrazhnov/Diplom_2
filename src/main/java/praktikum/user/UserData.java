package praktikum.user;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

public class UserData {

    public String email;
    public String password;
    public String name;

    public UserData() {

    }

    public UserData(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static UserData createdUser() {
        final String name = RandomStringUtils.randomAlphabetic(6,10);
        final String email = RandomStringUtils.randomAlphabetic(6, 10) + "@ya.ru";
        final String password = RandomStringUtils.randomAlphabetic(6, 10);
        return new UserData(name, email, password);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }
}

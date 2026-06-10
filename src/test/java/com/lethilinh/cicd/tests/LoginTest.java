package com.lethilinh.cicd.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.lethilinh.cicd.pages.LoginPage;

public class LoginTest {
    // Sử dụng ThreadLocal để cô lập WebDriver và LoginPage của mỗi luồng (thread)
    // khi chạy song song
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<LoginPage> loginPage = new ThreadLocal<>();

    public WebDriver getDriver() {
        return driver.get();
    }

    public LoginPage getLoginPage() {
        return loginPage.get();
    }

    @BeforeMethod
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        // Tự động kích hoạt chế độ Headless khi chạy trên GitHub Actions (môi trường
        // CI)
        if (System.getenv("CI") != null) {
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        }

        WebDriver webDriver = new ChromeDriver(options);
        webDriver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(10));
        webDriver.get("https://www.saucedemo.com/");

        driver.set(webDriver);
        loginPage.set(new LoginPage(webDriver));
    }

    @Test
    public void testSuccessfulLogin() {
        getLoginPage().enterCredentials("standard_user", "secret_sauce");
        getLoginPage().clickLogin();
        Assert.assertTrue(getLoginPage().isLoginSuccessful(), "Đăng nhập thất bại!");
    }

    @Test
    public void testFailedLogin() {
        getLoginPage().enterCredentials("standard_user", "wrong_password"); // Cố tình dùng sai mật khẩu để tạo lỗi 🔴
        getLoginPage().clickLogin();
        Assert.assertTrue(getLoginPage().isLoginSuccessful(), "Đăng nhập đáng lẽ phải thành công!");
    }

    @AfterMethod
    public void tearDown() {
        WebDriver webDriver = getDriver();
        if (webDriver != null) {
            webDriver.quit();
        }
        driver.remove();
        loginPage.remove();
    }
}

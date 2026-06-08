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
    private WebDriver driver;
    private LoginPage loginPage;

    @BeforeMethod
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        // Tự động kích hoạt chế độ Headless khi chạy trên GitHub Actions (môi trường CI)
        if (System.getenv("CI") != null) {
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        }
        
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(10));
        driver.get("https://www.saucedemo.com/");
        loginPage = new LoginPage(driver);
    }

        @Test
    public void testStandardUserLogin() {
        loginPage.enterCredentials("standard_user", "secret_sauce");
        loginPage.clickLogin();
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Đăng nhập bằng standard_user thất bại!");
    }

    @Test
    public void testProblemUserLogin() {
        loginPage.enterCredentials("problem_user", "secret_sauce");
        loginPage.clickLogin();
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Đăng nhập bằng problem_user thất bại!");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

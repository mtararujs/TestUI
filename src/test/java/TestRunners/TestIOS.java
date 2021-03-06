package TestRunners;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import pages.FacebookLandingPage;
import pages.GoogleLandingPage;
import pages.LandingPage;
import testUI.Configuration;

import static testUI.UIOpen.open;

public class TestIOS {
    private FacebookLandingPage facebookLandingPage = new FacebookLandingPage();

    @Test
    @DisplayName("IOS browser test case")
    public void testIOSBrowser() {
        Configuration.iOSTesting = true;
        open("https://www.facebook.com");
        facebookLandingPage.getSafariFacebookEmailDiv().click();
        facebookLandingPage.getSafariFacebookEmailInput()
                .given().waitFor(5).untilIsVisible()
                .when().sendKeys("email@email.com")
                .then().shouldHave().theAttribute("value")
                .and().shouldHave().value("email@email.com")
                .and().shouldHave().attribute("value").not().equalTo("whatever");
        facebookLandingPage.getSafariFacebookEmailInput()
                .then().waitFor(5).untilHasAttribute("value").equalTo("email@email.com");
        facebookLandingPage.getSafariFacebookPasswordInput()
                .given().sendKeys("password")
                .then().shouldHave().value("password")
                .then().clear();
    }
}

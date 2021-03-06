package testUI.elements;

import com.codeborne.selenide.SelenideElement;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

public interface ElementActions {

    UIElement setSelenideElement(By selenideElement);

    UIElement setiOSElement(By iOSElement);

    UIElement setAndroidElement(By element);

    UIElement setAndroidElement(String element);

    UIElement setiOSElement(String iOSElementAccId);

    UIElement click();

    Dimension getSize();

    Point getLocation();

    WaitFor waitFor(int Seconds);

    String getText();

    UIElement sendKeys(CharSequence charSequence);

    UIElement scrollIntoView();

    UIElement swipe(int XCoordinate, int YCoordinate);

    UIElement swipeRight();

    UIElement swipeLeft();

    MobileElement getMobileElement();

    UIElement clear();

    String getCssValue(String cssValue);

    String getValue();

    String getName();

    String getAttribute(String Attribute);

    boolean isVisible();

    boolean isEnabled();

    boolean Exists();

    ShouldBe shouldHave();

    ShouldBe shouldBe();

    ShouldBe should();

    UIElement and();

    UIElement given();

    UIElement then();

    UIElement when();

    SelenideElement getSelenideElement();
}

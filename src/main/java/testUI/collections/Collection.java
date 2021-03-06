package testUI.collections;

import testUI.elements.UIElement;
import com.codeborne.selenide.ElementsCollection;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;

public interface Collection {

    UICollection setSelenideCollection(By SelenideElement);

    UICollection setIOSCollection(By iOSElement);

    UICollection setIOSCollection(String accesibilityId);

    UICollection setAndroidCollection(By element);

    UICollection setAndroidCollection(String element);

    ElementsCollection getSelenideCollection();

    UIElement get(int i);

    UIElement first();

    int size();

    Dimension getSize();

    UIElement findByVisible();

    UIElement findByText(String text);

    UIElement findByEnabled();

    String asString();

    UICollection and();

    UICollection given();

    UICollection then();

    UICollection when();
}

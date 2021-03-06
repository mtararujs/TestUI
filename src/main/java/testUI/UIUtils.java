package testUI;

import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static testUI.TestUIDriver.*;
import static testUI.TestUIDriver.setDriver;
import static testUI.Utils.AppiumHelps.sleep;
import static com.codeborne.selenide.Selenide.open;
import static org.hamcrest.MatcherAssert.assertThat;

public class UIUtils {
    private static Logger logger = LoggerFactory.getLogger(UIUtils.class);
    private volatile static List<AppiumDriverLocalService> service = new ArrayList<>();
    private static List<String> Device = new ArrayList<>();
    private static List<String> DeviceName = new ArrayList<>();
    private static List<String> IOSDevices = new ArrayList<>();

    public static void setDevice(String dev, String deviceName) {
        Device.add(dev);
        DeviceName.add(deviceName);
    }

    public static void setiOSDevice(String dev) {
        IOSDevices.add(dev);
    }

    public static List<String> getIOSDevices() {
        return IOSDevices;
    }

    protected static List<AppiumDriverLocalService> getServices() {
        return service;
    }

    protected static AppiumDriverLocalService getService(int index) {
        return service.get(index);
    }

    public static void setService(AppiumDriverLocalService service) {
        UIUtils.service.add(service);
    }

    public static String getDevice() {
        return Device.get(Configuration.driver - 1);
    }

    public static String getDeviceName() {
        return DeviceName.get(Configuration.driver - 1);
    }

    public static List<String> getDevicesNames() {
        return DeviceName;
    }

    public static List<String> getDevices() {
        return Device;
    }

    public static void putLog(String log) {
        logger.info(log);
    }

    public static void putErrorLog(String log) {
        logger.error(log);
    }

    public static void putAllureParameter(String name, String log) {
        logger.info(name + " " + log);
        if (Configuration.useAllure) {
            Allure.parameter(name, log);
        }
    }

    private static void setUpSelenideVariables() {
        SelenideConfig defaults = new SelenideConfig();
        com.codeborne.selenide.Configuration.headless = Configuration.headless;
        com.codeborne.selenide.Configuration.baseUrl = Configuration.baseUrl;
        com.codeborne.selenide.Configuration.startMaximized = Configuration.startMaximized;
        com.codeborne.selenide.Configuration.browser = Configuration.browser;
        com.codeborne.selenide.Configuration.browserBinary = Configuration.browserBinary.isEmpty() ? defaults.browserBinary() : Configuration.browserBinary;
        com.codeborne.selenide.Configuration.browserCapabilities = Configuration.selenideBrowserCapabilities == null ? defaults.browserCapabilities() :
                Configuration.selenideBrowserCapabilities;
        com.codeborne.selenide.Configuration.assertionMode = Configuration.assertionMode == null ? defaults.assertionMode() : Configuration.assertionMode;
        com.codeborne.selenide.Configuration.browserVersion = Configuration.browserVersion.isEmpty() ? defaults.browserVersion() : Configuration.browserVersion;
        com.codeborne.selenide.Configuration.browserSize = Configuration.browserSize.isEmpty() ? defaults.browserSize() : Configuration.browserSize;
        com.codeborne.selenide.Configuration.fastSetValue = Configuration.fastSetValue;
        com.codeborne.selenide.Configuration.remote = Configuration.remote.isEmpty() ? defaults.remote() : Configuration.remote;
        com.codeborne.selenide.Configuration.browserPosition = Configuration.browserPosition.isEmpty() ? defaults.remote() : Configuration.browserPosition;
    }

    protected static void startSelenideDriver(String urlOrRelativeUrl) {
        System.clearProperty("webdriver.chrome.driver");
        setUpSelenideVariables();
        open(urlOrRelativeUrl);
    }

    protected static void startFirstBrowserDriver(DesiredCapabilities desiredCapabilities, String urlOrRelativeUrl) {
        String url = Configuration.appiumUrl.isEmpty() ? "http://127.0.0.1:" + Configuration.usePort.get(0) + "/wd/hub" : Configuration.appiumUrl;
        for (int i = 0; i < 2 ; i++) {
            try {
                putLog("Starting appium driver...");
                if (getDrivers().size() == 0) {
                    setDriver(new AppiumDriver(new URL(url), desiredCapabilities) {
                    });
                } else {
                    setDriver(new AppiumDriver(new URL(url), desiredCapabilities) {
                    }, 0);
                }
                Configuration.driver = 1;
                getDriver().get(urlOrRelativeUrl);
                break;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Could not create driver! retrying...");
                sleep(500);
                if (i == 1) {
                    throw new Error(e);
                }
            }
        }
    }

    protected static void startBrowserDriver(DesiredCapabilities desiredCapabilities, String urlOrRelativeUrl) {
        String url = Configuration.appiumUrl.isEmpty() ? "http://127.0.0.1:" + Configuration.usePort.get(Configuration.usePort.size()-1) + "/wd/hub" :
                Configuration.appiumUrl;
        for (int i = 0; i < 2 ; i++) {
            try {
                setDriver(new AppiumDriver(new URL(url),
                        desiredCapabilities) {
                });
                Configuration.driver = getDrivers().size();
                getDriver().get(urlOrRelativeUrl);
                break;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Could not create driver! retrying...");
                sleep(500);
                if (i == 1) {
                    e.printStackTrace();
                    throw new Error();
                }
            }
        }
    }

    protected static void startFirstDriver(DesiredCapabilities desiredCapabilities) {
        String url = Configuration.appiumUrl.isEmpty() ? "http://127.0.0.1:" + Configuration.usePort.get(0) + "/wd/hub" : Configuration.appiumUrl;
        for (int i = 0; i < 2 ; i++) {
            try {
                if (getDrivers().size() == 0) {
                    setDriver(new AppiumDriver(new URL(url), desiredCapabilities) {
                    });
                } else {
                    setDriver(new AppiumDriver(new URL(url), desiredCapabilities) {
                    }, 0);
                }
                break;
            } catch (Exception e) {
                System.err.println("Could not create driver! retrying...");
                sleep(500);
                if (i == 1) {
                    System.err.println("Could not create driver! check that the devices are correctly connected and in debug mode");
                    throw new Error();
                }
            }
        }
    }

    protected static void startDriver(DesiredCapabilities desiredCapabilities) {
        try {
            String url = Configuration.appiumUrl.isEmpty() ? "http://127.0.0.1:" + Configuration.usePort.get(Configuration.usePort.size()-1) + "/wd/hub" :
                    Configuration.appiumUrl;
            setDriver(new AppiumDriver(new URL(url), desiredCapabilities) {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void UIAssert(String reason, boolean assertion){
        if (!assertion && Configuration.useAllure) {
            boolean test = Configuration.deviceTests;
            Configuration.deviceTests = true;
            for (int index = 0; index < getDrivers().size(); index++) {
                byte[] screenshot = takeScreenshot(index);
                Allure.getLifecycle().addAttachment("Screenshot Mobile " + getDevicesNames().get(index), "image/png", "png", screenshot);
            }
            Configuration.deviceTests = false;
            if (WebDriverRunner.driver().hasWebDriverStarted()) {
                try {
                    byte[] screenshot = takeScreenshot();
                    Allure.getLifecycle().addAttachment("Screenshot Laptop Browser", "image/png", "png", screenshot);
                } catch (Exception e) {
                    System.err.println("Could not take a screenshot in the laptop browser...");
                }
            }
            Configuration.deviceTests = test;
        }
        assertThat(reason, assertion);
    }
}
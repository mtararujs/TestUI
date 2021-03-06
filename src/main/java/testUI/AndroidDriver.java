package testUI;

import org.openqa.selenium.remote.DesiredCapabilities;

import static testUI.Configuration.*;
import static testUI.Configuration.browser;
import static testUI.TestUIDriver.*;
import static testUI.TestUIDriver.getDriver;
import static testUI.TestUIServer.attachShutDownHookStopEmulator;
import static testUI.TestUIServer.startServerAndDevice;
import static testUI.UIUtils.*;

public class AndroidDriver {

    // ANDROID APP AND BROWSER SUPPORT

    public static void openApp() {
        deviceTests = true;
        iOSTesting = false;
        if (getServices().size() == 0 || !getServices().get(0).isRunning() && desiredCapabilities == null) {
            startServerAndDevice(true);
            DesiredCapabilities cap = setAppAndroidCapabilities();
            startFirstDriver(cap);
            if (!emulatorName.isEmpty()) {
                setDevice(getDriver().getCapabilities().asMap().get("deviceUDID").toString(), getDriver().getCapabilities().asMap().get("deviceUDID").toString());
                attachShutDownHookStopEmulator(getServices(), getDriver().getCapabilities().asMap().get("deviceUDID").toString());
            }
            putAllureParameter("Version", getDriver().getCapabilities().asMap().get("platformVersion").toString());
        } else {
            driver = 1;
            DesiredCapabilities cap = setAppAndroidCapabilities();
            if (appiumUrl.isEmpty()) {
                putAllureParameter("Using Appium port", usePort.get(0));
            } else {
                putAllureParameter("Using Appium url", appiumUrl);
            }
            putAllureParameter("Version", getDriver().getCapabilities().asMap().get("platformVersion").toString());
            startFirstDriver(cap);
        }
        emulatorName = "";
    }

    public static void openNewApp() {
        deviceTests = true;
        iOSTesting = false;
        startServerAndDevice(true);
        DesiredCapabilities cap = setAppAndroidCapabilities();
        startDriver(cap);
        if (!emulatorName.isEmpty()) {
            setDevice(getDriver().getCapabilities().asMap().get("deviceUDID").toString(), getDriver().getCapabilities().asMap().get("deviceUDID").toString());
            attachShutDownHookStopEmulator(getServices(), getDriver().getCapabilities().asMap().get("deviceUDID").toString());
        }
        putAllureParameter("Version", getDriver().getCapabilities().asMap().get("platformVersion").toString());
        emulatorName = "";
    }

    public static void openBrowser(String urlOrRelativeUrl) {
        iOSTesting = false;
        if (deviceTests) {
            urlOrRelativeUrl = baseUrl + urlOrRelativeUrl;
            if ((getServices().size() == 0 || !getServices().get(0).isRunning()) && desiredCapabilities == null) {
                startServerAndDevice(true);
                DesiredCapabilities cap = setAndroidBrowserCapabilities();
                startFirstBrowserDriver(cap, urlOrRelativeUrl);
                if (!emulatorName.isEmpty()) {
                    setDevice(getDriver().getCapabilities().asMap().get("deviceUDID").toString(), getDriver().getCapabilities().asMap().get("deviceUDID").toString());
                    attachShutDownHookStopEmulator(getServices(), getDriver().getCapabilities().asMap().get("deviceUDID").toString());
                }
                putAllureParameter("Version", getDriver().getCapabilities().asMap().get("platformVersion").toString());
            } else {
                driver = 1;
                DesiredCapabilities cap = setAndroidBrowserCapabilities();
                if (appiumUrl.isEmpty()) {
                    putAllureParameter("Using Appium port", usePort.get(0));
                } else {
                    putAllureParameter("Using Appium url", appiumUrl);
                }
                putAllureParameter("Version", getDriver().getCapabilities().asMap().get("platformVersion").toString());
                startFirstBrowserDriver(cap, urlOrRelativeUrl);
            }
        } else {
            startSelenideDriver(urlOrRelativeUrl);
        }
        emulatorName = "";
        putAllureParameter("Browser", browser);
    }

    public static void openNewBrowser(String urlOrRelativeUrl) {
        iOSTesting = false;
        if (deviceTests) {
            urlOrRelativeUrl = baseUrl + urlOrRelativeUrl;
            startServerAndDevice(true);
            DesiredCapabilities cap = setAndroidBrowserCapabilities();
            startBrowserDriver(cap, urlOrRelativeUrl);
            if (!emulatorName.isEmpty()) {
                setDevice(getDriver().getCapabilities().asMap().get("deviceUDID").toString(), getDriver().getCapabilities().asMap().get("deviceUDID").toString());
                attachShutDownHookStopEmulator(getServices(), getDriver().getCapabilities().asMap().get("deviceUDID").toString());
            }
            putAllureParameter("Version", getDriver().getCapabilities().asMap().get("platformVersion").toString());
        } else {
            startSelenideDriver(urlOrRelativeUrl);
        }
        putAllureParameter("Browser", browser);
    }
}

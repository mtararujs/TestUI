package testUI;

import org.openqa.selenium.remote.DesiredCapabilities;

import static testUI.Configuration.*;
import static testUI.TestUIDriver.setIOSCapabilities;
import static testUI.TestUIServer.startServerAndDevice;
import static testUI.UIUtils.*;

public class IOSDriver {

    // NOW IOS APP AND BROWSER

    public static void openIOSApp() {
        deviceTests = true;
        iOSTesting = true;
        iOSDevices++;
        if ((getServices().size() == 0 || !getServices().get(0).isRunning()) && desiredCapabilities == null) {
            startServerAndDevice(true);
            DesiredCapabilities cap = setIOSCapabilities(false);
            startFirstDriver(cap);
        } else {
            DesiredCapabilities cap = setIOSCapabilities(false);
            if (appiumUrl.isEmpty()) {
                putAllureParameter("Using Appium port", usePort.get(0));
            } else {
                putAllureParameter("Using Appium url", appiumUrl);
            }
            startFirstDriver(cap);
        }
    }

    public static void openNewIOSApp() {
        deviceTests = true;
        iOSTesting = true;
        iOSDevices++;
        if (getServices().size() == 0 || !getServices().get(0).isRunning()) {
            startServerAndDevice(true);
            DesiredCapabilities cap = setIOSCapabilities(false);
            startFirstDriver(cap);
        } else {
            DesiredCapabilities cap = setIOSCapabilities(false);
            putAllureParameter("Using Appium port", usePort.get(0));
            startFirstDriver(cap);
        }
    }

    public static void openIOSBrowser(String urlOrRelativeUrl) {
        deviceTests = true;
        iOSTesting = true;
        iOSDevices++;
        urlOrRelativeUrl = baseUrl + urlOrRelativeUrl;
        if (getServices().size() == 0 || !getServices().get(0).isRunning() && desiredCapabilities == null) {
            startServerAndDevice(true);
            DesiredCapabilities cap = setIOSCapabilities(true);
            startFirstBrowserDriver(cap, urlOrRelativeUrl);
        } else {
            DesiredCapabilities cap = setIOSCapabilities(true);
            if (appiumUrl.isEmpty()) {
                putAllureParameter("Using Appium port", usePort.get(0));
            } else {
                putAllureParameter("Using Appium url", appiumUrl);
            }
            startFirstBrowserDriver(cap, urlOrRelativeUrl);
        }
        putAllureParameter("Browser", "Safari");
    }


    public static void openNewIOSBrowser(String urlOrRelativeUrl) {
        deviceTests = true;
        iOSTesting = true;
        iOSDevices++;
        urlOrRelativeUrl = baseUrl + urlOrRelativeUrl;
        if (getServices().size() == 0 || !getServices().get(0).isRunning()) {
            startServerAndDevice(true);
            DesiredCapabilities cap = setIOSCapabilities(true);
            startBrowserDriver(cap, urlOrRelativeUrl);
        } else {
            DesiredCapabilities cap = setIOSCapabilities(true);
            startBrowserDriver(cap, urlOrRelativeUrl);
        }
        putAllureParameter("Browser", "Safari");
    }
}
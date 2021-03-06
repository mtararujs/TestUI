package testUI;

import static testUI.AndroidDriver.*;
import static testUI.IOSDriver.*;
import static testUI.TestUIDriver.getDesiredCapabilities;
import static testUI.UIUtils.putLog;
import static testUI.Configuration.*;

public class UIOpen {
    public static void open() {
        if (iOSTesting) {
            if (iOSAppPath.isEmpty() && getDesiredCapabilities() == null) {
                if (!androidAppPath.isEmpty() || (!appActivity.isEmpty() && !appPackage.isEmpty())) {
                    putLog("iOSTesting variable is set to true, but while all the Android variables are correctly set, the iOS ones aren't:"
                            + "\niOSAppPath = " + iOSAppPath
                            + "\n trying to start the Android app");
                    openApp();
                } else {
                    throw new Error("iOSAppPath is mandatory field to run iOS apps, here are your iOS values:"
                            + "\niOSAppPath = " + iOSAppPath
                            + "\niOSDeviceName = " + iOSDeviceName
                            + "\niOSVersion = " + iOSVersion);
                }

            } else {
                openIOSApp();
            }
        } else {
            if (androidAppPath.isEmpty() && (appActivity.isEmpty() && appPackage.isEmpty()) && getDesiredCapabilities() == null) {
                if (!iOSAppPath.isEmpty()) {
                    putLog("iOSTesting variable is set to false, but while all the iOS variables are correctly set, the android ones aren't:"
                            + "\nandroidAppPath = " + androidAppPath
                            + "\nappActivity = " + appActivity
                            + "\nappPackage = " + appPackage
                            + "\n trying to start the iOS app");
                    openIOSApp();
                } else {
                    throw new Error("androidAppPath or appActivity and appPackage are mandatory fields to run Android apps, but their values are:"
                            + "\nandroidAppPath = " + androidAppPath
                            + "\nappActivity = " + appActivity
                            + "\nappPackage = " + appPackage);
                }
            } else {
                openApp();
            }
        }
    }

    public static void openNew() {
        if (iOSTesting) {
            if (iOSAppPath.isEmpty() && getDesiredCapabilities() == null) {
                if (!androidAppPath.isEmpty() && (!appActivity.isEmpty() && !appPackage.isEmpty())) {
                    putLog("iOSTesting variable is set to true, but while all the Android variables are correctly set, the iOS ones aren't:"
                            + "\niOSAppPath = " + iOSAppPath
                            + "\niOSDeviceName = " + iOSDeviceName
                            + "\niOSVersion = " + iOSVersion
                            + "\n trying to start the Android app");
                    openNewApp();
                } else {
                    throw new Error("iOSAppPath is mandatory fields to run iOS apps, here are your iOS values:"
                            + "\niOSAppPath = " + iOSAppPath
                            + "\niOSDeviceName = " + iOSDeviceName
                            + "\niOSVersion = " + iOSVersion);
                }
            } else {
                openNewIOSApp();
            }
        } else {
            if (androidAppPath.isEmpty() && (appActivity.isEmpty() && appPackage.isEmpty()) && getDesiredCapabilities() == null) {
                if (!iOSAppPath.isEmpty()) {
                    putLog("iOSTesting variable is set to false, but while all the iOS variables are correctly set, the android ones aren't:"
                            + "\nandroidAppPath = " + androidAppPath
                            + "\nappActivity = " + appActivity
                            + "\nappPackage = " + appPackage
                            + "\n trying to start the iOS app");
                    openNewIOSApp();
                } else {
                    throw new Error("androidAppPath or appActivity and appPackage are mandatory fields to run Android apps, but their values are:"
                            + "\nandroidAppPath = " + androidAppPath
                            + "\nappActivity = " + appActivity
                            + "\nappPackage = " + appPackage);
                }
            } else {
                openNewApp();
            }
        }
    }

    public static void open(String urlOrRelativeUrl) {
        if (deviceTests && iOSTesting) {
            openIOSBrowser(urlOrRelativeUrl);
        } else {
            openBrowser(urlOrRelativeUrl);
        }
    }

    public static void openNew(String urlOrRelativeUrl) {
        if (deviceTests && iOSTesting) {
            openNewIOSBrowser(urlOrRelativeUrl);
        } else {
            openNewBrowser(urlOrRelativeUrl);
        }
    }
}

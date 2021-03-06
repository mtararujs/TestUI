package testUI;

import testUI.Utils.AppiumHelps;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.AndroidServerFlag;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static testUI.ADBUtils.*;
import static testUI.TestUIDriver.*;
import static testUI.UIUtils.*;
import static testUI.Configuration.*;
import static testUI.Configuration.driver;
import static testUI.Utils.AppiumHelps.sleep;
import static testUI.iOSCommands.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestUIServer {
    private volatile static boolean serviceRunning = false;

    protected static void startServer(String port, String Bootstrap) {
        AppiumServiceBuilder builder;
        DesiredCapabilities cap;
        //Set Capabilities
        cap = new DesiredCapabilities();
        cap.setCapability("noReset", "false");
        //Build the Appium service
        builder = new AppiumServiceBuilder();
        builder.withIPAddress("127.0.0.1");
        builder.usingPort(Integer.parseInt(port));
        builder.withCapabilities(cap);
        builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
        builder.withArgument(GeneralServerFlag.LOG_LEVEL, "error");
        builder.withArgument(AndroidServerFlag.BOOTSTRAP_PORT_NUMBER, Bootstrap);
        //Start the server with the builder
        TestUIServer.serviceRunning = false;
        setService(AppiumDriverLocalService.buildService(builder));
        getServices().get(getServices().size() - 1).start();
        for (int i = 0; i < 5; i++) {
            String serviceOut = getService(getServices().size() - 1).getStdOut();
            if (serviceOut != null) {
                if (serviceOut.contains("Could not start REST http")) {
                    putLog("Could not start server in port: " + port + "\n Let's try a different one");
                    TestUIServer.serviceRunning = false;
                    break;
                } else {
                    TestUIServer.serviceRunning = true;
                }
            } else {
                TestUIServer.serviceRunning = true;
            }
            sleep(500);
        }
        if (!TestUIServer.serviceRunning) {
            getServices().remove(getServices().size() - 1);
        }
    }

    protected static void attachShutDownHookStopEmulator(List<AppiumDriverLocalService> serv) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> stopEmulators(serv)));
    }

    protected static void attachShutDownHookStopEmulator(List<AppiumDriverLocalService> serv, String device) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> stopEmulators(serv, device)));
    }

    protected static void attachShutDownHook(List<AppiumDriverLocalService> serv) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> closeDriverAndServer(serv)));
    }

    private static void stopEmulators(List<AppiumDriverLocalService> serv) {
        for (int i = 0; i < 40; i++) {
            if (serv.size() != 0 && !serv.get(0).isRunning()) {
                AppiumHelps.sleep(1000);
                for (String device : getDevices()) {
                    stopEmulator(device);
                }
                break;
            }
            AppiumHelps.sleep(1000);
        }
    }

    private static void stopEmulators(List<AppiumDriverLocalService> serv, String device) {
        for (int i = 0; i < 40; i++) {
            if (serv.size() == 0 || !serv.get(0).isRunning()) {
                AppiumHelps.sleep(1000);
                stopEmulator(device);
                break;
            }
            AppiumHelps.sleep(1000);
        }
    }

    private static void closeDriverAndServer(List<AppiumDriverLocalService> serv) {
        putLog("Stopping drivers");
        for (AppiumDriver driver : getDrivers()) {
            try {
                driver.close();
            } catch (Exception e) {
                System.err.println("Couldn't close the driver");
            }
            try {
                driver.quit();
            } catch (Exception e) {
                System.err.println("Couldn't quit the driver");
            }
        }
        putLog("Running Shutdown Server");
        for (AppiumDriverLocalService service: serv) {
            if (service.isRunning()) {
                service.stop();
            }
        }
    }

    protected static void startServerAndDevice(boolean attachShootDown) {
        int connectedDevices = getDeviceNames().size() - 1;
        int startedEmulators = 0;
        for (String devicesNames : getDeviceNames()) {
            if (devicesNames.contains("emulator")) {
                startedEmulators++;
            }
        }
        int emulators = useEmulators ? getEmulatorName().size() : 0;
        int totalDevices = emulators + connectedDevices - startedEmulators;
        int ports = 9586 + usePort.size()*100;
        int bootstrap = 5333 + useBootstrapPort.size()*100;
        String port = String.valueOf(ports);
        String Bootstrap = String.valueOf(bootstrap);
        for (int device = usePort.size(); device < totalDevices + iOSDevices; device++) {
            if (appiumUrl.isEmpty()) {
                startServer(port, Bootstrap);
                attachShutDownHook(getServices());
            }
            if (serviceRunning || (!appiumUrl.isEmpty() && getDevices().size() >= device)) {
                if (!iOSTesting) {
                    if (androidDeviceName.isEmpty() && emulatorName.isEmpty()) {
                        if (connectedDevices <= device) {
                            System.out.println(device + " number of device");
                            assertThat("There are not enough devices connected", useEmulators);
                            assertThat("There are no emulators to start the automation",
                                    getEmulatorName().get(device), not(isEmptyOrNullString()));
                            Configuration.emulatorName = getEmulatorName().get(device);
                            attachShutDownHookStopEmulator(getServices());
                        } else {
                            if (!getDevices().toString().contains(getDeviceNames().get(device))) {
                                setDevice(getDeviceNames().get(device), getDeviceNames().get(device));
                            }
                        }
                    } else {
                        if (emulatorName.isEmpty()) {
                            setDevice(androidDeviceName, androidDeviceName);
                        }
                    }
                } else {
                    if (iOSDeviceName.isEmpty()) {
                        if (UDID.isEmpty()) {
                            Map<String, String> sampleIOSDevice = getSampleDevice();
                            iOSDeviceName = sampleIOSDevice.get("name");
                            iOSVersion = sampleIOSDevice.get("version");
                            UDID = sampleIOSDevice.get("udid");
                        } else {
                            iOSDeviceName = getIOSName(UDID);
                            iOSVersion = getIOSVersion(UDID);
                        }
                    }
                    setiOSDevice(iOSDeviceName);
                }
                driver = iOSTesting ? getDevices().size() + getIOSDevices().size() : getDevices().size();
                driver = emulatorName.isEmpty() ? driver : driver + 1;
                break;
            }
            port = String.valueOf(Integer.parseInt(port) + 100);
            Bootstrap = String.valueOf(Integer.parseInt(Bootstrap) + 100);
        }
        if (appiumUrl.isEmpty()) {
            usePort.add(port);
            useBootstrapPort.add(Bootstrap);
            putAllureParameter("Using Appium port", usePort.get(usePort.size() - 1));
        } else {
            putAllureParameter("Using Appium url", appiumUrl);
        }
    }

    public static void stop(int driver) {
        if (deviceTests) {
            usePort.remove(driver - 1);
            useBootstrapPort.remove(driver - 1);
            getDrivers().get(driver - 1).close();
            sleep(500);
            getDrivers().get(driver - 1).quit();
            removeDriver(driver - 1);
            getServices().get(driver - 1).stop();
            getServices().remove(driver - 1);
            if (getDevices().size() != 0) {
                stopEmulator(getDevices().get(driver - 1));
                getDevices().remove(driver - 1);
            }
            Configuration.driver = getDrivers().size();
        } else {
            getSelenideDriver().close();
            getSelenideDriver().quit();
            deviceTests = true;
        }
    }

    public static void stop() {
        if (deviceTests) {
            usePort.remove(driver - 1);
            useBootstrapPort.remove(driver - 1);
            getDrivers().get(driver - 1).close();
            sleep(500);
            getDrivers().get(driver - 1).quit();
            removeDriver(driver - 1);
            getServices().get(driver - 1).stop();
            getServices().remove(driver - 1);
            if (getDevices().size() != 0) {
                iOSDevices = driver - getDevices().size();
                stopEmulator(getDevices().get(driver - iOSDevices - 1));
                getDevices().remove(driver - iOSDevices - 1);
            }
            driver = getDrivers().size();
        } else {
            getSelenideDriver().close();
            getSelenideDriver().quit();
            deviceTests = true;
        }
    }
}
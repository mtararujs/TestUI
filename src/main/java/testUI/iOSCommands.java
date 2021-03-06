package testUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class iOSCommands {
    private static Map<String,Map<String,String>> getSimulatorNames() {
        String s;
        List<String> output = new ArrayList<>();
        List<String> versions = new ArrayList<>();
        Map<String,String> devices = new HashMap<>();
        Map<String,Map<String,String>> iOS = new HashMap<>();
        try {
            Process p = Runtime.getRuntime().exec("xcrun simctl list");

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            while ((s = stdInput.readLine()) != null) {
                output.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean Devices = false;
        for (String line : output) {
            if (Devices && line.contains("iPhone")) {
                devices.put(line.split(" \\(")[0].split("    ")[1], line.split("\\(")[1].split("\\)")[0]);
            } else if (Devices && line.contains("iOS")) {
                if (versions.size() != 0 && devices.size() != 0) {
                    iOS.put(versions.get(versions.size() -1), devices);
                    devices = new HashMap<>();
                }
                versions.add(line.split("iOS ")[1].split(" ")[0]);
            }
            if (line.contains("== Devices ==")) {
                Devices = true;
            } else if (line.contains("== Device Pairs ==")) {
                iOS.put(versions.get(versions.size() -1), devices);
                Devices = false;
            }
        }
        return iOS;
    }

    private static List<String> getDeviceUDIDs() {
        String s;
        List<String> output = new ArrayList<>();
        try {
            Process p = Runtime.getRuntime().exec("idevice_id -l");
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            while ((s = stdInput.readLine()) != null) {
                output.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    private static Map<String,String> getSampleSimulator() {
        Map<String,Map<String,String>> simulators = getSimulatorNames();
        Map<String,String> sample = new HashMap<>();
        String version = "";
        String name = "";
        String udid = "";
        for (String keys : simulators.keySet()) {
            version = keys;
            for (String phone : simulators.get(keys).keySet()) {
                name = phone;
                udid = simulators.get(keys).get(phone);
                break;
            }
            break;
        }
        sample.put("version", version);
        sample.put("name", name);
        sample.put("udid", udid);
        return sample;
    }

    public static String getIOSVersion(String udid){
        String s;
        List<String> output = new ArrayList<>();
        try {
            Process p = Runtime.getRuntime().exec("ideviceinfo -u " + udid + " -k ProductVersion");
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            while ((s = stdInput.readLine()) != null) {
                output.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.get(0);
    }

    public static String getIOSName(String udid){
        String s;
        List<String> output = new ArrayList<>();
        try {
            Process p = Runtime.getRuntime().exec("ideviceinfo -u " + udid + " -k DeviceName");
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            while ((s = stdInput.readLine()) != null) {
                output.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.get(0);
    }


    public static Map<String,String> getSampleDevice() {
        if (getDeviceUDIDs().isEmpty()) {
            return getSampleSimulator();
        } else {
            Map<String,String> sample = new HashMap<>();
            sample.put("version", getIOSVersion(getDeviceUDIDs().get(0)));
            sample.put("name", getIOSName(getDeviceUDIDs().get(0)));
            sample.put("udid", getDeviceUDIDs().get(0));
            return sample;
        }
    }
}

package com.example.opsc7312_task2;

public class UserSettings {

    String unit_setting;
    String traffic;

    public UserSettings()
    {
    }

    public UserSettings(String unitSetting, String traffic)
    {
        this.unit_setting = unitSetting;
        this.traffic = traffic;
    }

    public String getUnit_setting() {
        return unit_setting;
    }

    public void setUnit_setting(String unit_setting) {
        this.unit_setting = unit_setting;
    }

    public String getTraffic() {
        return traffic;
    }

    public void setTraffic(String traffic) {
        this.traffic = traffic;
    }
}

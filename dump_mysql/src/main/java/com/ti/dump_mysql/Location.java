package com.ti.dump_mysql;

public class Location {
    private String location;
    private Integer value;

    public Location() {
    }

    public Location(String location, Integer value) {
        this.location = location;
        this.value = value;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Location{" +
                "location='" + location + '\'' +
                ", value=" + value +
                '}';
    }
}

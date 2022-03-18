package org.krahe.chris.mapgen.core.util;

public enum GeoType {
    POINT("point"),
    LINE("line"),
    POLYGON("polygon");

    private final String name;

    GeoType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

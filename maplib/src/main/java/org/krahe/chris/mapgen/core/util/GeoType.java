package org.krahe.chris.mapgen.core.util;

import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

public enum GeoType {
    POINT("point", Point.class),
    LINE("line", LineString.class),
    POLYGON("polygon", Polygon.class);

    private final String name;
    private final Class<?> geoClass;

    GeoType(String name, Class<?> geoClass) {
        this.name = name;
        this.geoClass = geoClass;
    }

    public String getName() {
        return name;
    }

    public Class<?> getGeoClass() {
        return geoClass;
    }
}

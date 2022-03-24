package org.krahe.chris.mapgen.core;

import org.locationtech.jts.geom.Geometry;

public class Feature {
    private final String name;
    private final Geometry geometry;

    public Feature(Geometry geometry) {
        this.name = null;
        this.geometry = geometry;
    }

    public Feature(String name, Geometry geometry) {
        this.name = name;
        this.geometry = geometry;
    }

    public String getName() {
        return name;
    }

    public Geometry getGeometry() {
        return geometry;
    }
}

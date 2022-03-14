package org.krahe.chris.mapgen.core;

import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.xml.styling.SLDParser;
import org.locationtech.jts.geom.Geometry;

import java.awt.image.BufferedImage;

public class StyledGenerator implements Generator {

    private final SimpleFeatureTypeBuilder sftBuilder;
    private SLDParser sldParser;

    public StyledGenerator() {
        sftBuilder = new SimpleFeatureTypeBuilder();
        sftBuilder.setName("MapGenStyle");
        sftBuilder.setCRS(DefaultGeographicCRS.WGS84);
        sftBuilder.add("label", String.class);
        sftBuilder.add("geo", Geometry.class);

    }

    @Override
    public BufferedImage generate(Options options) {
        return null;
    }
}

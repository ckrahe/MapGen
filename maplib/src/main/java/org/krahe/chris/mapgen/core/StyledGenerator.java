package org.krahe.chris.mapgen.core;

import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.xml.styling.SLDParser;
import org.locationtech.jts.geom.Point;

import java.awt.image.BufferedImage;

public class StyledGenerator implements Generator {

    private final SimpleFeatureTypeBuilder sftBuilder;
    private SLDParser sldParser;

    public StyledGenerator() {
        sftBuilder = new SimpleFeatureTypeBuilder();
        sftBuilder.setName("MapGenSchema");
        sftBuilder.setCRS(DefaultGeographicCRS.WGS84);
        sftBuilder.add("point", Point.class);

    }

    @Override
    public BufferedImage generate(Options options) {
        return null;
    }
}

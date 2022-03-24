package org.krahe.chris.mapgen.core;

import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.krahe.chris.mapgen.core.util.GeoType;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StyledGenerator implements Generator {

    private final MapTools mapTools;

    public StyledGenerator() {
        mapTools = MapTools.getInstance();
    }

    @Override
    public BufferedImage generate(Options options) {
        BufferedImage image = createTransparentImage(options);
        MapContent mapContent = createMapContent(options);
        GTRenderer renderer = createRenderer(mapContent);
        renderer.paint(image.createGraphics(), options.getCanvas(), options.getBbox());
        return image;
    }

    private BufferedImage createTransparentImage(Options options) {
        BufferedImage image = new BufferedImage(options.getWidth(), options.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, options.getWidth(), options.getHeight());
        return image;
    }

    private MapContent createMapContent(Options options) {
        MapContent mapContent = new MapContent();

        mapContent.addLayer(createFeatureLayer(options, GeoType.POLYGON));
        mapContent.addLayer(createFeatureLayer(options, GeoType.LINE));
        mapContent.addLayer(createFeatureLayer(options, GeoType.POINT));

        return mapContent;
    }

    private FeatureLayer createFeatureLayer(Options options, GeoType geoType) {
        return new FeatureLayer(createFeatures(options, geoType), mapTools.getStyleMap().get(geoType));
    }

    private FeatureCollection<SimpleFeatureType, SimpleFeature> createFeatures(Options options, GeoType geoType) {
        SimpleFeatureType activeSchema = mapTools.getSchemaMap().get(geoType);
        SimpleFeatureBuilder sfBuilder = new SimpleFeatureBuilder(activeSchema);

        List<SimpleFeature> features = new ArrayList<>();
        int counter = 1;
        for (Geometry geometry : options.getGeometryList()) {
            String geoTypeClassName = geoType.getGeoClass().getTypeName();
            try {
                if (Class.forName(geoTypeClassName).isInstance(geometry))
                {
                    sfBuilder.set(geoType.getName(), geometry);
                    sfBuilder.set("name", String.format("%s-%s", geoType.getName(), counter++));
                    if (geoType.equals(GeoType.POINT)) {
                        Point p = (Point) geometry;
                        sfBuilder.set("X", p.getX());
                        sfBuilder.set("Y", p.getY());
                    }
                    features.add(sfBuilder.buildFeature(null));
                }
            } catch (ClassNotFoundException e) {
                System.out.printf("Skipping unexpected GeoType class %s%n", geoTypeClassName);
            }
        }
        System.out.printf("Adding %s %s%n", features.size(), geoType.getName());
        return new ListFeatureCollection(activeSchema, features);
    }

    private GTRenderer createRenderer(MapContent mapContent) {
        StreamingRenderer renderer = new StreamingRenderer();

        // add map content
        renderer.setMapContent(mapContent);

        // given drawing hints
        renderer.setJava2DHints(
                new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        renderer.setRendererHints(
                Collections.singletonMap("optimizedDataLoadingEnabled", Boolean.TRUE));

        return renderer;
    }
}

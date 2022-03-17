package org.krahe.chris.mapgen.core;

import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.*;
import org.geotools.styling.Stroke;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StyledGenerator implements Generator {

    private SimpleFeatureType schema;
    private final StyleFactory styleFactory;
    private final FilterFactory filterFactory;

    public StyledGenerator() {
        styleFactory = CommonFactoryFinder.getStyleFactory();
        filterFactory = CommonFactoryFinder.getFilterFactory();
    }

    private SimpleFeatureType createSchema(Options options) {
        SimpleFeatureTypeBuilder sftBuilder = new SimpleFeatureTypeBuilder();
        sftBuilder.setName("MapGenSchema");
        sftBuilder.setCRS(DefaultGeographicCRS.WGS84);
        String name = options.getWkt().contains("POINT") ? "point" : "geo";
        Class<?> cls = options.getWkt().contains("POINT") ? Point.class : Geometry.class;
        sftBuilder.add(name, cls);
        return sftBuilder.buildFeatureType();
    }

    @Override
    public BufferedImage generate(Options options) {
        schema = createSchema(options);
        BufferedImage image = new BufferedImage(options.getWidth(), options.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        MapContent mapContent = createMapContent(options);
        GTRenderer renderer = createRenderer(mapContent);
        renderer.paint(image.createGraphics(), options.getCanvas(), options.getBbox());

        return image;
    }

    private MapContent createMapContent(Options options) {
        MapContent mapContent = new MapContent();
        Style style = options.getWkt().contains("POINT") ? createPointStyle() : createPolygonStyle();
        mapContent.addLayer(new FeatureLayer(createFeatures(options), style));
        return mapContent;
    }

    private FeatureCollection<SimpleFeatureType, SimpleFeature> createFeatures(Options options) {
        SimpleFeatureBuilder sfBuilder = new SimpleFeatureBuilder(schema);

        List<SimpleFeature> features = new ArrayList<>();
        String name = options.getWkt().contains("POINT") ? "point" : "geo";
        sfBuilder.set(name, options.getGeometry());
        features.add(sfBuilder.buildFeature(null));

        return new ListFeatureCollection(schema, features);
    }

    private Style createPointStyle() {
        Graphic graphic = styleFactory.createDefaultGraphic();

        // create marker
        Mark mark = styleFactory.getCircleMark();
        mark.setStroke(styleFactory.createStroke(filterFactory.literal(Color.WHITE), filterFactory.literal(1)));
        mark.setFill(styleFactory.createFill(filterFactory.literal(Color.RED)));

        // add marker
        graphic.graphicalSymbols().clear();
        graphic.graphicalSymbols().add(mark);
        graphic.setSize(filterFactory.literal(8));

        // Setting the geometryPropertyName arg to null signals that we want to
        // draw the default geometry of features
        PointSymbolizer sym = styleFactory.createPointSymbolizer(graphic, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(rule);
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

    private Style createPolygonStyle() {
        Stroke stroke =
                styleFactory.createStroke(
                        filterFactory.literal(Color.GREEN),
                        filterFactory.literal(1),
                        filterFactory.literal(1));

        // create a transparent 'fill'
        Fill fill =
                styleFactory.createFill(
                        filterFactory.literal(Color.WHITE), filterFactory.literal(0.0));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geometry of features
         */
        PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(rule);
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

    private GTRenderer createRenderer(MapContent mapContent) {
        StreamingRenderer renderer = new StreamingRenderer();

        // add map content
        renderer.setMapContent(mapContent);

        // given drawing hints
        renderer.setJava2DHints(
                new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF));
        renderer.setRendererHints(
                Collections.singletonMap("optimizedDataLoadingEnabled", Boolean.TRUE));

        return renderer;
    }
}

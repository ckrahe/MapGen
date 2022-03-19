package org.krahe.chris.mapgen.core;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Stroke;
import org.geotools.styling.*;
import org.krahe.chris.mapgen.core.util.GeoType;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MapTools {
    private static MapTools instance = null;

    private final StyleFactory styleFactory;
    private final FilterFactory filterFactory;
    private final Map<GeoType, SimpleFeatureType> schemaMap;
    private final Map<GeoType, Style> styleMap;

    private MapTools() {
        styleFactory = CommonFactoryFinder.getStyleFactory();
        filterFactory = CommonFactoryFinder.getFilterFactory();
        schemaMap = createSchemaMap();
        styleMap = createStyleMap();
    }

    private Map<GeoType, SimpleFeatureType> createSchemaMap() {
        final Map<GeoType, SimpleFeatureType> schemaMap = new HashMap<>();

        schemaMap.put(GeoType.POINT, createSchema(GeoType.POINT));
        schemaMap.put(GeoType.LINE, createSchema(GeoType.LINE));
        schemaMap.put(GeoType.POLYGON, createSchema(GeoType.POLYGON));

        return schemaMap;
    }

    private SimpleFeatureType createSchema(GeoType geoType) {
        SimpleFeatureTypeBuilder sftBuilder = new SimpleFeatureTypeBuilder();

        sftBuilder.setName(String.format("MapGenSchema_%s", geoType.getName()));
        sftBuilder.setCRS(DefaultGeographicCRS.WGS84);
        sftBuilder.add(geoType.getName(), geoType.getGeoClass());

        return sftBuilder.buildFeatureType();
    }

    private Map<GeoType, Style> createStyleMap() {
        final Map<GeoType, Style> styleMap = new HashMap<>();

        styleMap.put(GeoType.POINT, createPointStyle());
        styleMap.put(GeoType.LINE, createLineStyle());
        styleMap.put(GeoType.POLYGON, createPolygonStyle());

        return styleMap;
    }

    private Style createPointStyle() {
        Graphic graphic = styleFactory.createDefaultGraphic();

        // create marker
        Mark mark = styleFactory.getCrossMark();
        mark.setStroke(styleFactory.createStroke(filterFactory.literal(Color.RED), filterFactory.literal(0.1)));
        mark.setFill(styleFactory.createFill(filterFactory.literal(Color.RED)));

        // add marker
        graphic.graphicalSymbols().clear();
        graphic.graphicalSymbols().add(mark);
        graphic.setSize(filterFactory.literal(10));
        graphic.setRotation(filterFactory.literal(45));

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

    private Style createLineStyle() {
        return createPolyStyle(Color.CYAN, 2, Color.PINK, 0.0);
    }

    private Style createPolygonStyle() {
        return createPolyStyle(Color.GREEN, 1, Color.GREEN, 0.2);
    }

    private Style createPolyStyle(Color strokeColor, int strokeWidth, Color fillColor, double fillOpacity) {
        Stroke stroke =
                styleFactory.createStroke(
                        filterFactory.literal(strokeColor),
                        filterFactory.literal(strokeWidth),
                        filterFactory.literal(1));

        // create a transparent 'fill'
        Fill fill =
                styleFactory.createFill(
                        filterFactory.literal(fillColor), filterFactory.literal(fillOpacity));

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

    public Map<GeoType, SimpleFeatureType> getSchemaMap() {
        return schemaMap;
    }

    public Map<GeoType, Style> getStyleMap() {
        return styleMap;
    }

    public static MapTools getInstance() {
        if (instance == null)
            instance = new MapTools();
        return instance;
    }
}

package org.krahe.chris.mapgen.core;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.metadata.iso.citation.OnLineResourceImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Stroke;
import org.geotools.styling.*;
import org.krahe.chris.mapgen.core.util.GeoType;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Literal;
import org.opengis.style.GraphicalSymbol;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
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
        Mark mark = createCircleMark();

        // add marker
        graphic.graphicalSymbols().clear();
        graphic.graphicalSymbols().add(mark);
        graphic.setSize(getCircleSize());

        PointSymbolizer sym = styleFactory.createPointSymbolizer(graphic, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(rule);
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

    private Style createSVGPointStyle() throws URISyntaxException {
        // follows https://docs.geotools.org/maintenance/userguide/library/render/style.html
        org.opengis.style.StyleFactory ogStyleFactory = CommonFactoryFinder.getStyleFactory();
        OnLineResourceImpl svg = new OnLineResourceImpl(new URI("file:marker.svg"));
        List<GraphicalSymbol> symbols = new ArrayList<>();
        symbols.add(ogStyleFactory.externalGraphic(svg, "svg", null));
        symbols.add(createCircleMark());

        org.opengis.style.Graphic graphic = ogStyleFactory.graphic(symbols, null, getCircleSize(),
                null, null, null);

        org.opengis.style.PointSymbolizer sym = ogStyleFactory.pointSymbolizer("point",
                filterFactory.property("the_geom"), null, null, graphic);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym); // hmmm, this ain't right
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(rule);
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

    private Mark createCircleMark() {
        Mark mark = styleFactory.getCircleMark();
        mark.setStroke(styleFactory.createStroke(filterFactory.literal(Color.WHITE), filterFactory.literal(1)));
        mark.setFill(styleFactory.createFill(filterFactory.literal(Color.RED)));
        return mark;
    }

    private Literal getCircleSize() {
        filterFactory.literal(8);
    }

    private Style createLineStyle() {
        return createPolyStyle(Color.BLUE, Color.PINK, 0.0);
    }

    private Style createPolygonStyle() {
        return createPolyStyle(Color.GREEN, Color.GREEN, 0.0);
    }

    private Style createPolyStyle(Color strokeColor, Color fillColor, double fillOpacity) {
        Stroke stroke =
                styleFactory.createStroke(
                        filterFactory.literal(strokeColor),
                        filterFactory.literal(1),
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

package org.krahe.chris.mapgen.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.krahe.chris.mapgen.core.util.OptionsException;
import org.krahe.chris.mapgen.core.util.OptionsParser;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;

class OptionsParserTest {

    @Test
    void simpleBboxTest() throws OptionsException {
        Envelope envelope = OptionsParser.testAndNormalizeBbox("10,10,8,12");
        Assertions.assertEquals(12.0, envelope.getMaxY());
    }

    @Test
    void nanBboxTest() {
        Assertions.assertThrows(OptionsException.class, () -> OptionsParser.testAndNormalizeBbox("X,10,8,12"));
    }

    @Test
    void shortBboxTest() {
        Assertions.assertThrows(OptionsException.class, () -> OptionsParser.testAndNormalizeBbox("10,10,8"));
    }

    @Test
    void nullBboxTest() {
        OptionsException e = Assertions.assertThrows(OptionsException.class, () ->
                OptionsParser.testAndNormalizeBbox(null));
        Assertions.assertTrue(e.getMessage().contains("required"));
    }

    @Test
    void rangeBboxTest() {
        OptionsException e = Assertions.assertThrows(OptionsException.class, () ->
                OptionsParser.testAndNormalizeBbox("201,10,8,12"));
        Assertions.assertTrue(e.getMessage().contains("range"));
    }

    @Test
    void flipBboxTest() throws OptionsException {
        Envelope envelope = OptionsParser.testAndNormalizeBbox("10,12,8,10");
        Assertions.assertEquals(12.0, envelope.getMaxY());
    }

    @Test
    void lineTest() {
        OptionsException e = Assertions.assertThrows(OptionsException.class, () ->
                OptionsParser.testAndNormalizeBbox("10,12,8,12"));
        Assertions.assertTrue(e.getMessage().contains("max"));
    }

    @Test
    void simplePointTypeTest() throws OptionsException {
        Geometry geometry = OptionsParser.validateWkt("POINT (9 11)");
        Assertions.assertTrue(geometry instanceof Point);
    }

    @Test
    void simplePointValueTest() throws OptionsException {
        Geometry geometry = OptionsParser.validateWkt("POINT (9 11)");
        Point point = (Point) geometry;
        Assertions.assertEquals(11.0, point.getY());
    }

    @Test
    void malformedWktTest() {
        OptionsException e = Assertions.assertThrows(OptionsException.class, () ->
                OptionsParser.validateWkt("POINT [9 11]"));
        Assertions.assertTrue(e.getCause() instanceof ParseException);
    }

    @Test
    void simpleDimensionTest() throws OptionsException {
        int dim = OptionsParser.validateDimension("11");
        Assertions.assertEquals(11, dim);
    }

    @Test
    void zeroDimensionTest() {
        OptionsException e = Assertions.assertThrows(OptionsException.class, () ->
                OptionsParser.validateDimension("0"));
        Assertions.assertTrue(e.getMessage().contains("range"));
    }

    @Test
    void malformedDimensionTest() {
        OptionsException e = Assertions.assertThrows(OptionsException.class, () ->
                OptionsParser.validateDimension("Z"));
        Assertions.assertTrue(e.getCause() instanceof NumberFormatException);
    }
}
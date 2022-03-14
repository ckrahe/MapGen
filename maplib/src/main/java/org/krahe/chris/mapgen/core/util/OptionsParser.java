package org.krahe.chris.mapgen.core.util;

import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class OptionsParser {
    public static Geometry validateWkt(String wkt) throws OptionsException {
        Geometry geometry;
        try {
            WKTReader reader = new WKTReader();
            geometry = reader.read(wkt);
        } catch (ParseException e) {
            throw new OptionsException("error parsing wkt", e);
        }
        return geometry;
    }

    public static Envelope testAndNormalizeBbox(String bboxStr) throws OptionsException {
        if (bboxStr == null)
            throw new OptionsException("bbox property is required");

        String[] bboxStrings = bboxStr.split(",", 4);
        if (bboxStrings.length != 4)
            throw new OptionsException("bbox must have 4 elements");

        Double[] bboxDoubles = new Double[4];
        try {
            for (int i = 0; i <= 3; i++) {
                bboxDoubles[i] = Double.parseDouble(bboxStrings[i]);
            }
        } catch (NumberFormatException nfe) {
            throw new OptionsException("bbox elements must be doubles");
        }

        Envelope envelope = new Envelope(bboxDoubles[2], bboxDoubles[0], bboxDoubles[1], bboxDoubles[3]);
        validateLongitude(envelope.getMinX());
        validateLongitude(envelope.getMaxX());
        validateLatitude(envelope.getMinY());
        validateLatitude(envelope.getMaxY());
        validateMinMax(envelope.getMinX(), envelope.getMaxX());
        validateMinMax(envelope.getMinY(), envelope.getMaxY());

        return envelope;
    }

    protected static void validateLongitude(double longitude) throws OptionsException {
        if (longitude < -180.0 || longitude > 180.0)
            throw new OptionsException("longitude value out of range");
    }

    protected static void validateLatitude(double latitude) throws OptionsException {
        if (latitude < -90.0 || latitude > 90.0)
            throw new OptionsException("latitude value out of range");
    }

    protected static void validateMinMax(double min, double max) throws OptionsException {
        if (max <= min)
            throw new OptionsException("max value must be greater than min");
    }

    public static int validateDimension(String dimStr) throws OptionsException {
        int dim;
        try {
            dim = Integer.parseInt(dimStr);
        } catch (NumberFormatException e) {
            throw new OptionsException("Dimension must be numeric", e);
        }
        if (dim < 1 || dim > 1000)
            throw new OptionsException(String.format("Dimension, %d, out of range", dim));

        return dim;
    }
}

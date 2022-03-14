package org.krahe.chris.mapgen.core;

import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class Options {
	private boolean valid;
	private Geometry geometry;
	private String symbol;
	private Integer width;
	private Integer height;
	private Envelope bbox;
	private Boolean ui;
	private String filename;
	private String generator;

	// for ease in toString
	private String wkt;
	private String bboxStr;
	
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	public static class OptionsException extends Exception {
		private final Exception cause;
		public OptionsException(String message) {
			super(message);
			cause = null;
		}

		public OptionsException(String message, Exception cause) {
			super(message);
			this.cause = cause;
		}

		public boolean hasCause() {
			return cause != null;
		}

		public Exception getCause() {
			return cause;
		}
	}
	
	public Options() { valid = false; }

	/**
	 * Initialize from system properties
	 * @throws OptionsException if required properties are missing or malformed
	 */
	public void init() throws OptionsException {
		loadWkt();
		loadSymbol();
		loadSize();
		loadBbox();
		loadUI();
		loadFilename();
		loadGenerator();
        validateFinal();
		valid = true;
	}
	
	public String getWkt() {
		return wkt;
	}
	public Geometry getGeometry() { return geometry; }
	public String getSymbol() { return symbol; }
	public Boolean hasSymbol() { return(symbol != null); }
	public Integer getWidth() { return width; }
	public Integer getHeight() { return height; }
	public Envelope getBbox() { return bbox; }
	public Boolean getUI() { return ui; }
	public String getFilename() { return filename; }
	public String getGenerator() { return generator; }

	private void loadWkt() throws OptionsException {
		wkt = System.getProperty("wkt");
		geometry = validateWkt(wkt);
	}

	protected static Geometry validateWkt(String wkt) throws OptionsException {
		Geometry geometry;
		try {
			WKTReader reader = new WKTReader();
			geometry = reader.read(wkt);
		} catch (ParseException e) {
			throw new OptionsException("error parsing wkt", e);
		}
		return geometry;
	}

	private void loadSymbol() {
		symbol = System.getProperty("symbol");
	}
	
	private void loadSize() throws OptionsException {
		String widthStr = System.getProperty("width", "512");
		String heightStr = System.getProperty("height", "512");
		try {
			width = Integer.parseInt(widthStr);
			height = Integer.parseInt(heightStr);
		} catch (NumberFormatException e) {
			throw new OptionsException("width and height must be integers");
		}
	}
	
	private void loadBbox() throws OptionsException {
		bboxStr = System.getProperty("bbox");
		bbox = Options.testAndNormalizeBbox(bboxStr);
	}

	protected static Envelope testAndNormalizeBbox(String bboxStr) throws OptionsException {
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
		Options.validateLongitude(envelope.getMinX());
		Options.validateLongitude(envelope.getMaxX());
		Options.validateLatitude(envelope.getMinY());
		Options.validateLatitude(envelope.getMaxY());
		Options.validateMinMax(envelope.getMinX(), envelope.getMaxX());
		Options.validateMinMax(envelope.getMinY(), envelope.getMaxY());

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

	private void loadUI() {
		String uiString = System.getProperty("ui", "false");
		ui = uiString.equalsIgnoreCase("true");
	}
	
	private void loadFilename() {
		filename = System.getProperty("filename", "map.png");
	}
	
	private void loadGenerator() {
		generator = System.getProperty("generator", "color");
	}
    
    private void validateFinal() {
    }

	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		if (valid) {
			sb.append(String.format("wkt = %s", getWkt()));
			sb.append(LINE_SEPARATOR);
			sb.append(hasSymbol() ? String.format("symbol = %s", symbol) : "no symbol");
			sb.append(LINE_SEPARATOR);
			sb.append(String.format("size = %dx%d", width, height));
			sb.append(LINE_SEPARATOR);
			sb.append(String.format("bbox = %s", bboxStr));
			sb.append(LINE_SEPARATOR);
			sb.append(String.format("ui = %s", (ui ? "true" : "false")));
			sb.append(LINE_SEPARATOR);
			sb.append(String.format("filename = %s", filename));
			sb.append(LINE_SEPARATOR);
			sb.append(String.format("generator = %s", generator));
		} else {
			sb.append("Invalid options");
		}
		
		return sb.toString();
	}
}
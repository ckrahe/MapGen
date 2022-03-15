package org.krahe.chris.mapgen.core;

import org.krahe.chris.mapgen.core.util.OptionsException;
import org.krahe.chris.mapgen.core.util.OptionsParser;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;

import java.awt.*;

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

	public Rectangle getCanvas() {
		return new Rectangle(getWidth(), getHeight());
	}

	private void loadWkt() throws OptionsException {
		wkt = System.getProperty("wkt");
		if (wkt != null)
			geometry = OptionsParser.validateWkt(wkt);
	}

	private void loadSymbol() {
		symbol = System.getProperty("symbol");
	}
	
	private void loadSize() throws OptionsException {
		width = OptionsParser.validateDimension(System.getProperty("width", "512"));
		height = OptionsParser.validateDimension(System.getProperty("height", "512"));
	}
	
	private void loadBbox() throws OptionsException {
		bboxStr = System.getProperty("bbox");
		if (bboxStr != null)
			bbox = OptionsParser.testAndNormalizeBbox(bboxStr);
	}

	private void loadUI() {
		String uiString = System.getProperty("ui", "false");
		ui = uiString.equalsIgnoreCase("true");
	}
	
	private void loadFilename() {
		filename = System.getProperty("filename", "map.png");
	}
	
	private void loadGenerator() {
		generator = System.getProperty("generator", "static");
	}

	private void validateFinal() throws OptionsException {
		if (generator.equals("styled")) {
			if (wkt == null)
				throw new OptionsException("wkt is required for the styled generator");
			if (bbox == null)
				throw new OptionsException("bbox is required for the styled generator");
		}
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
package org.krahe.chris.mapgen;

import org.krahe.chris.mapgen.core.Generator;
import org.krahe.chris.mapgen.core.Options;
import org.krahe.chris.mapgen.factory.GeneratorFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MapTool {
	
	public static void main(String[] args) {
		Options options = new Options();
		try {
			options.init();
			handleSuccess(options);
		} catch(Exception e) {
			handleFailure(e);
		}
	}
	
	private static void handleSuccess(Options options) throws Exception {
		Generator generator = GeneratorFactory.build(options);
		BufferedImage image = generator.generate(options);
		if (options.getUI())
			handleResultUI(image, options);
		else
			handleResultCommandLine(image, options);
	}
	
	private static void handleFailure(Exception e) {
		System.out.printf("Fail: %s%n", e.getMessage());
		e.printStackTrace();
	}

	private static void handleResultCommandLine(BufferedImage image, Options options) throws Exception {
		File outputFile = new File(options.getFilename());
		if(!outputFile.createNewFile())
			throw new Exception(String.format("'%s' already exists", options.getFilename()));

		if (outputFile.exists()) {
			try {
				ImageIO.write(image, "png", outputFile);
				System.out.printf("Image created: '%s'%n", options.getFilename());
			} catch (IOException e) {
				throw new Exception(String.format("Could not write to '%s'", options.getFilename()));
			}
		} else
			throw new Exception(String.format("Could not create '%s'", options.getFilename()));
	}
	
	private static void handleResultUI(Image image, Options options) {
		// prep
		JFrame frame = new JFrame("mapgen");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(options.getWidth(), options.getHeight()));
		frame.getContentPane().add(new JLabel(new ImageIcon(image)));
		
		// display
		frame.pack();
		frame.setVisible(true);
	}
}
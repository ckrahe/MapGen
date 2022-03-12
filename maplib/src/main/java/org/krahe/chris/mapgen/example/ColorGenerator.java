package org.krahe.chris.mapgen.example;

import org.krahe.chris.mapgen.core.Generator;
import org.krahe.chris.mapgen.core.Options;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ColorGenerator implements Generator {
	private final Color color;
	
	public ColorGenerator() {
		this.color = Color.CYAN;
	}
	
	public ColorGenerator(Color color) {
		this.color = color; 
	}
	
	@Override
	public BufferedImage generate(Options options) {
		BufferedImage image = new BufferedImage(options.getWidth(), options.getHeight(),
			BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				image.setRGB(i, j, color.getRGB());
			}			
		}
		return image;
	}
}
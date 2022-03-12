package org.krahe.chris.mapgen.example;

import org.krahe.chris.mapgen.core.Generator;
import org.krahe.chris.mapgen.core.Options;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class StaticGenerator implements Generator {

	@Override
	public BufferedImage generate(Options options) {
		BufferedImage image;
		try {
			InputStream imageStream = StaticGenerator.class.getResourceAsStream("/blue_marble.jpg");
			if (imageStream != null)
				image = resizeIfNeeded(ImageIO.read(imageStream), options);
			else
				image = generateErrorImage(options);
		} catch (IOException e) {
			image = generateErrorImage(options);
		}
		return image;
	}
	
	private BufferedImage resizeIfNeeded(BufferedImage originalImage, Options options) {
        BufferedImage resultingImage;
		if (	(originalImage.getWidth() != options.getWidth())
			||	(originalImage.getHeight() != options.getHeight())) {
			resultingImage = new BufferedImage(options.getWidth(), options.getHeight(),
				BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics2D = resultingImage.createGraphics();
			graphics2D.drawImage(originalImage, 0, 0, options.getWidth(), options.getHeight(), 
				null);
			graphics2D.dispose();
		} else {
			resultingImage = originalImage;
		}
		return resultingImage;
	}
	
	private BufferedImage generateErrorImage(Options options) {
		BufferedImage image = new BufferedImage(options.getWidth(), options.getHeight(),
			BufferedImage.TYPE_INT_RGB);
		Color color = new Color(255, 0, 0, 127);
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				image.setRGB(i, j, color.getRGB());
			}			
		}
		return image;
	}
}
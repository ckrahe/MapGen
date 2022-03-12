package org.krahe.chris.mapgen.core;

import java.awt.image.BufferedImage;

public interface Generator {
	BufferedImage generate(Options options);
}
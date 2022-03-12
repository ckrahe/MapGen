package org.krahe.chris.mapgen.v1;

import org.krahe.chris.mapgen.core.Generator;
import org.krahe.chris.mapgen.core.Options;
import org.krahe.chris.mapgen.example.ColorGenerator;

import java.awt.*;

public class GeneratorFactory {
	public static Generator build(Options options) {
		return(new ColorGenerator(Color.GRAY)); // placeholder
	}
}
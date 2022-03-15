package org.krahe.chris.mapgen.factory;

import org.krahe.chris.mapgen.core.Generator;
import org.krahe.chris.mapgen.core.Options;
import org.krahe.chris.mapgen.core.StyledGenerator;
import org.krahe.chris.mapgen.example.ColorGenerator;
import org.krahe.chris.mapgen.example.StaticGenerator;

import java.awt.*;

public class GeneratorFactory {
	public static Generator build(Options options) {
		Generator generator;
		switch (options.getGenerator()) {
			case "styled":
				generator = new StyledGenerator();
				break;

			case "color":
				generator = new ColorGenerator();
				break;
				
			case "static":
				generator = new StaticGenerator();
				break;
				
			case "v1":
				generator = org.krahe.chris.mapgen.v1.GeneratorFactory.build(options);
				break;
				
			default:
				generator = new ColorGenerator(Color.MAGENTA);
		}
		return generator;
	}
}
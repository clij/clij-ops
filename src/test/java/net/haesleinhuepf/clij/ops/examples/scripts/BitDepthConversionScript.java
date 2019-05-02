package net.haesleinhuepf.clij.ops.examples.scripts;

import net.imagej.ImageJ;

import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;

public class BitDepthConversionScript {
	public static void main(String... args) throws FileNotFoundException, ScriptException {
		// start ImageJ
		ImageJ ij = new ImageJ();
		ij.ui().showUI();

		ij.script().run(new File("/home/random/Development/imagej/project/clij/clij-ops/src/test/jython/bitdepthConversion.py"),
				false, new Object[]{"ops", ij.op(), "ui", ij.ui(), "io", ij.io()});

	}
}

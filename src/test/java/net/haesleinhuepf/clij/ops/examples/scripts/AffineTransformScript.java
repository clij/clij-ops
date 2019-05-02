package net.haesleinhuepf.clij.ops.examples.scripts;

import ij.ImagePlus;
import net.imagej.ImageJ;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.ShortType;

import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;

public class AffineTransformScript {
	public static void main(String... args) throws FileNotFoundException, ScriptException {
		// start ImageJ
		ImageJ ij = new ImageJ();
		ij.ui().showUI();

		ij.script().run(new File("/home/random/Development/imagej/project/clij/clij-ops/src/test/jython/affineTransform.py"),
				false, new Object[]{"ops", ij.op(), "ui", ij.ui(), "io", ij.io()});

	}
}

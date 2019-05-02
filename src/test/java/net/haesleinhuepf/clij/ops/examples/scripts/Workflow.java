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

public class Workflow {
	public static void main(String... args) throws FileNotFoundException, ScriptException {
		// start ImageJ
		ImageJ ij = new ImageJ();
		ij.ui().showUI();

		// get some image to process
		Img<ShortType> testImage = ArrayImgs.shorts(new long[]{100, 100});
		ImagePlus imp = ImageJFunctions.wrap(testImage, "TestImage");

		// run the script, hand over image and imagej instances
		ij.script().run(new File("/home/random/Development/imagej/project/clij/clij-ops/src/test/jython/workflow.py"),
				false, new Object[]{"data", imp, "ops", ij.op(), "ui", ij.ui()});

	}
}

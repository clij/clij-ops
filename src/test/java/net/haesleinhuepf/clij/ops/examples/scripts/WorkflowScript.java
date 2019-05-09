
package net.haesleinhuepf.clij.ops.examples.scripts;

import java.io.File;
import java.io.FileNotFoundException;

import javax.script.ScriptException;

import org.junit.Test;

import ij.ImagePlus;
import net.imagej.ImageJ;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.ShortType;

public class WorkflowScript {

	public static void main(String... args) throws FileNotFoundException,
		ScriptException
	{
		new WorkflowScript().run();
	}

	@Test
	public void run() throws FileNotFoundException, ScriptException {
		// start ImageJ
		ImageJ ij = new ImageJ();
		ij.ui().showUI();

		// get some image to process
		Img<ShortType> testImage = ArrayImgs.shorts(new long[] { 100, 100 });
		ImagePlus imp = ImageJFunctions.wrap(testImage, "TestImage");

		ij.script().run(new File(getClass().getResource("/jython/workflow.py")
			.getPath()), false, new Object[] { "data", imp, "ops", ij.op(), "ui", ij
				.ui(), "io", ij.io() });
	}
}

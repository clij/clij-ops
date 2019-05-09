package net.haesleinhuepf.clij.ops.examples.scripts;

import net.imagej.ImageJ;

import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;

public class AllocateBigImagesScript {
	public static void main(String... args) throws FileNotFoundException, ScriptException {
		ImageJ ij = new ImageJ();
		ij.ui().showUI();

		ij.script().run(new File("/home/random/Development/imagej/project/clij/clij-ops/src/test/jython/allocateBigImages.py"),
				false, new Object[]{"ops", ij.op(), "ui", ij.ui(), "io", ij.io()});

	}
}
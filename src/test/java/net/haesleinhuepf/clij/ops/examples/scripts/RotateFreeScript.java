
package net.haesleinhuepf.clij.ops.examples.scripts;

import java.io.File;
import java.io.FileNotFoundException;

import javax.script.ScriptException;

import org.junit.Test;

import net.imagej.ImageJ;

public class RotateFreeScript {

	public static void main(String... args) throws FileNotFoundException,
			ScriptException
	{
		new RotateFreeScript().run();
	}

	@Test
	public void run() throws FileNotFoundException, ScriptException {
		ImageJ ij = new ImageJ();
		ij.ui().showUI();

		ij.script().run(new File(getClass().getResource("/jython/rotateFree.py").getPath()),
				false, new Object[] { "ops", ij.op(), "ui", ij.ui(), "io", ij.io() });
	}
}
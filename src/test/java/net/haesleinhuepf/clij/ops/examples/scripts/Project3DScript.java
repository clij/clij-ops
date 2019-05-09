
package net.haesleinhuepf.clij.ops.examples.scripts;

import java.io.File;
import java.io.FileNotFoundException;

import javax.script.ScriptException;

import org.junit.Test;

import net.imagej.ImageJ;

public class Project3DScript {

	public static void main(String... args) throws FileNotFoundException,
			ScriptException
	{
		new Project3DScript().run();
	}

	@Test
	public void run() throws FileNotFoundException, ScriptException {
		ImageJ ij = new ImageJ();
		ij.ui().showUI();

		ij.script().run(new File(getClass().getResource("/jython/project3D.py").getPath()),
				false, new Object[] { "ops", ij.op(), "ui", ij.ui(), "io", ij.io() });
	}
}

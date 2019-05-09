
package net.haesleinhuepf.clij.ops.examples;

import java.io.IOException;

import org.junit.Test;

import net.haesleinhuepf.clij.ops.CLIJ_close.CLIJ_close;
import net.haesleinhuepf.clij.ops.CLIJ_minimumBox.CLIJ_minimumBox;
import net.haesleinhuepf.clij.ops.CLIJ_pull.CLIJ_pull;
import net.haesleinhuepf.clij.ops.CLIJ_push.CLIJ_push;
import net.imagej.ImageJ;

public class Minimum {

	@Test
	public void run() throws IOException {
		ImageJ ij = new ImageJ();
		Object input = ij.io().open(getClass().getResource("/samples/t1-head.tif")
			.getPath());
		ij.ui().show("input", input);
		// push image to GPU
		Object inputGPU = ij.op().run(CLIJ_push.class, input);
		// minimum
		Object minimum = ij.op().run(CLIJ_minimumBox.class, inputGPU, 3, 3, 3);
		// show result
		Object result = ij.op().run(CLIJ_pull.class, minimum);
		ij.ui().show("minimum", result);

		// cleanup
		ij.op().run(CLIJ_close.class, inputGPU);
		ij.op().run(CLIJ_close.class, minimum);
		ij.op().run(CLIJ_close.class);

	}

	public static void main(String... args) throws IOException {
		Minimum task = new Minimum();
		task.run();
	}
}

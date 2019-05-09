
package net.haesleinhuepf.clij.ops.examples;

import java.io.IOException;

import org.junit.Test;

import net.haesleinhuepf.clij.ops.CLIJ_close.CLIJ_close;
import net.haesleinhuepf.clij.ops.CLIJ_maxProjection.CLIJ_maximumZProjection;
import net.haesleinhuepf.clij.ops.CLIJ_pull.CLIJ_pull;
import net.haesleinhuepf.clij.ops.CLIJ_push.CLIJ_push;
import net.imagej.ImageJ;

public class MaximumProjection {

	@Test
	public void run() throws IOException {
		ImageJ ij = new ImageJ();
		Object input = ij.io().open(getClass().getResource("/samples/t1-head.tif").getPath());
		ij.ui().show("input", input);
		// push image to GPU
		Object inputGPU = ij.op().run(CLIJ_push.class, input);
		// max projection
		Object maximumProjected = ij.op().run(CLIJ_maximumZProjection.class,
			inputGPU);
		// show result
		Object result = ij.op().run(CLIJ_pull.class, maximumProjected);
		ij.ui().show("maximum projected", result);

		// cleanup
		ij.op().run(CLIJ_close.class, inputGPU);
		ij.op().run(CLIJ_close.class, maximumProjected);
		ij.op().run(CLIJ_close.class);

	}

	public static void main(String... args) throws IOException {
		MaximumProjection task = new MaximumProjection();
		task.run();
	}
}

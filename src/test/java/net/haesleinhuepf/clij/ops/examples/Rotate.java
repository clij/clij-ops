
package net.haesleinhuepf.clij.ops.examples;

import java.io.IOException;

import org.junit.Test;

import net.haesleinhuepf.clij.ops.CLIJ_close.CLIJ_close;
import net.haesleinhuepf.clij.ops.CLIJ_pull.CLIJ_pull;
import net.haesleinhuepf.clij.ops.CLIJ_push.CLIJ_push;
import net.haesleinhuepf.clij.ops.CLIJ_rotateLeft.CLIJ_rotateLeft;
import net.imagej.ImageJ;

public class Rotate {

	@Test
	public void run() throws IOException {
		ImageJ ij = new ImageJ();
		Object input = ij.io().open(getClass().getResource("/samples/t1-head.tif")
			.getPath());
		ij.ui().show("input", input);
		// push image to GPU
		Object inputGPU = ij.op().run(CLIJ_push.class, input);
		// flip
		Object rotated = ij.op().run(CLIJ_rotateLeft.class, inputGPU);
		// show result
		Object result = ij.op().run(CLIJ_pull.class, rotated);
		ij.ui().show("rotated", result);

		// cleanup
		ij.op().run(CLIJ_close.class, inputGPU);
		ij.op().run(CLIJ_close.class, rotated);
		ij.op().run(CLIJ_close.class);

	}

	public static void main(String... args) throws IOException {
		Rotate task = new Rotate();
		task.run();
	}
}

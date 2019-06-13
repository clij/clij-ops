
package net.haesleinhuepf.clij.ops.examples;

import java.io.IOException;

import org.junit.Test;

import net.haesleinhuepf.clij.ops.CLIJ_close.CLIJ_close;
import net.haesleinhuepf.clij.ops.CLIJ_pull.CLIJ_pull;
import net.haesleinhuepf.clij.ops.CLIJ_push.CLIJ_push;
import net.imagej.ImageJ;

public class Reslice {

	@Test
	public void run() throws IOException {
		ImageJ ij = new ImageJ();
		Object input = ij.io().open(getClass().getResource("/samples/t1-head.tif")
			.getPath());
		ij.ui().show("input", input);
		// push image to GPU
		Object inputGPU = ij.op().run(CLIJ_push.class, input);
		// flip
		Object resliced = ij.op().run(
			net.haesleinhuepf.clij.ops.filter.reslice.CLIJ_resliceLeft.class,
			inputGPU);
		// show result
		Object result = ij.op().run(CLIJ_pull.class, resliced);
		ij.ui().show("resliced", result);

		// cleanup
		ij.op().run(CLIJ_close.class, inputGPU);
		ij.op().run(CLIJ_close.class, resliced);


	}

	public static void main(String... args) throws IOException {
		Reslice task = new Reslice();
		task.run();
	}
}

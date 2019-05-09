
package net.haesleinhuepf.clij.ops.examples;

import java.io.IOException;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import org.junit.Test;

import net.haesleinhuepf.clij.ops.CLIJ_blur.CLIJ_blur;
import net.haesleinhuepf.clij.ops.CLIJ_close.CLIJ_close;
import net.haesleinhuepf.clij.ops.CLIJ_pull.CLIJ_pull;
import net.haesleinhuepf.clij.ops.CLIJ_push.CLIJ_push;
import net.imagej.ImageJ;

public class Blur {

	@Test
	public void run() throws IOException {
		ImageJ ij = new ImageJ();
		Object input = ij.io().open(getClass().getResource("/samples/t1-head.tif").getPath());
		ij.ui().show("input", input);
		// push image to GPU
		Object inputGPU = ij.op().run(CLIJ_push.class, input);

		Object blurred = ij.op().run(CLIJ_blur.class, inputGPU, 5, 5, 1);
		// show result
		Object result = ij.op().run(CLIJ_pull.class, blurred);
		ij.ui().show("blurred", result);

		// cleanup
		ij.op().run(CLIJ_close.class, inputGPU);
		ij.op().run(CLIJ_close.class, blurred);
		ij.op().run(CLIJ_close.class);

	}

	public static void main(String... args) throws IOException {
		Blur task = new Blur();
		task.run();
	}
}

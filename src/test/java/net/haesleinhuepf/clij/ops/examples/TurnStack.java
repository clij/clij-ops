
package net.haesleinhuepf.clij.ops.examples;

import java.io.IOException;

import org.junit.Test;

import net.haesleinhuepf.clij.ops.CLIJ_close.CLIJ_close;
import net.haesleinhuepf.clij.ops.CLIJ_pull.CLIJ_pull;
import net.haesleinhuepf.clij.ops.CLIJ_push.CLIJ_push;
import net.haesleinhuepf.clij.ops.CLIJ_rotateRight.CLIJ_rotateRight;
import net.imagej.ImageJ;

public class TurnStack {

	@Test
	public void run() throws IOException {
		ImageJ ij = new ImageJ();
		Object input = ij.io().open(getClass().getResource("/samples/t1-head.tif").getPath());
		ij.ui().show("input", input);
		// push image to GPU
		Object inputGPU = ij.op().run(CLIJ_push.class, input);
		// reslice left
		Object resliceLeft = ij.op().run(
			net.haesleinhuepf.clij.ops.filter.reslice.CLIJ_resliceLeft.class,
			inputGPU);
		// rotate right
		Object rotateRight = ij.op().run(CLIJ_rotateRight.class, resliceLeft);
		// show result
		Object result = ij.op().run(CLIJ_pull.class, rotateRight);
		ij.ui().show("turned stack", result);

		// cleanup
		ij.op().run(CLIJ_close.class, inputGPU);
		ij.op().run(CLIJ_close.class, resliceLeft);
		ij.op().run(CLIJ_close.class, rotateRight);
		ij.op().run(CLIJ_close.class);

	}

	public static void main(String... args) throws IOException {
		TurnStack task = new TurnStack();
		task.run();
	}
}

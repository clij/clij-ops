
package net.haesleinhuepf.clij.ops.examples;

import java.io.IOException;

import org.junit.Test;

import net.haesleinhuepf.clij.ops.CLIJ_close.CLIJ_close;
import net.haesleinhuepf.clij.ops.CLIJ_pull.CLIJ_pull;
import net.haesleinhuepf.clij.ops.CLIJ_push.CLIJ_push;
import net.imagej.ImageJ;
import net.imglib2.img.Img;

public class Project3D {

	@Test
	public void run() throws IOException {
		int angle_step = 10;
		ImageJ ij = new ImageJ();
		Img input = (Img) ij.io().open(getClass().getResource("/samples/t1-head.tif").getPath());
		ij.ui().show("input", input);
		Img target = ij.op().create().img(new long[] { input.dimension(0), input
			.dimension(1), 360 / angle_step });
		Img target32 = ij.op().convert().float32(target);
		// push image to GPU
		Object inputGPU = ij.op().run(CLIJ_push.class, input);
		Object targetGPU = ij.op().run(CLIJ_push.class, target32);

		// we need to translate the stack in Z to get some space for the shoulders
		// when we rotate the head around the y-axis
		Object translated = ij.op().run("CLIJ_translate3D", inputGPU, 0, 0, input
			.dimension(2) / 2);

		int count = 0;
		for (int angle = 0; angle < 360; angle += angle_step) {
			Object rotated = ij.op().run("CLIJ_rotate3D", translated, 0, angle, 0.0,
				true);
			Object maxProjected = ij.op().run("CLIJ_maximumZProjection", rotated);

			// put the maximum projection in the right place in the result stack
			ij.op().run("CLIJ_copySlice", targetGPU, maxProjected, count);

			ij.op().run(CLIJ_close.class, rotated);
			ij.op().run(CLIJ_close.class, maxProjected);
			count++;
		}

		Object result = ij.op().run(CLIJ_pull.class, targetGPU);
		ij.ui().show("3D Projection", result);

		// cleanup
		ij.op().run(CLIJ_close.class, inputGPU);
		ij.op().run(CLIJ_close.class, targetGPU);
		ij.op().run(CLIJ_close.class, translated);
		ij.op().run(CLIJ_close.class);

	}

	public static void main(String... args) throws IOException {
		Project3D task = new Project3D();
		task.run();
	}
}

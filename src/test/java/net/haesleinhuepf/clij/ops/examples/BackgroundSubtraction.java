
package net.haesleinhuepf.clij.ops.examples;

import java.io.IOException;

import org.junit.Test;

import net.haesleinhuepf.clij.ops.CLIJ_addImagesWeighted.CLIJ_addImagesWeighted;
import net.haesleinhuepf.clij.ops.CLIJ_blur.CLIJ_blur;
import net.haesleinhuepf.clij.ops.CLIJ_close.CLIJ_close;
import net.haesleinhuepf.clij.ops.CLIJ_maxProjection.CLIJ_maximumZProjection;
import net.haesleinhuepf.clij.ops.CLIJ_pull.CLIJ_pull;
import net.haesleinhuepf.clij.ops.CLIJ_push.CLIJ_push;
import net.imagej.ImageJ;
import net.imglib2.img.Img;

public class BackgroundSubtraction {

	@Test
	public void run() throws IOException {
		ImageJ ij = new ImageJ();
		Img input = (Img) ij.io().open("/home/random/t1-head-1.tif");
		ij.ui().show("input", input);
		// push image to GPU
		Object inputGPU = ij.op().run(CLIJ_push.class, input);

		Object background = ij.op().run(CLIJ_blur.class, inputGPU, 10, 10, 1);
		Object background_subtracted = ij.op().run(CLIJ_addImagesWeighted.class,
			inputGPU, background, 1, -1);
		Object maximum_projected = ij.op().run(CLIJ_maximumZProjection.class,
			background_subtracted);

		// show result
		Object result = ij.op().run(CLIJ_pull.class, maximum_projected);
		ij.ui().show("background subtraction", result);

		// cleanup
		ij.op().run(CLIJ_close.class, inputGPU);
		ij.op().run(CLIJ_close.class, background);
		ij.op().run(CLIJ_close.class, background_subtracted);
		ij.op().run(CLIJ_close.class, maximum_projected);


	}

	public static void main(String... args) throws IOException {
		BackgroundSubtraction task = new BackgroundSubtraction();
		task.run();
	}
}


package net.haesleinhuepf.clij.ops.examples;

import java.io.IOException;

import org.junit.Test;

import net.haesleinhuepf.clij.ops.CLIJ_close.CLIJ_close;
import net.haesleinhuepf.clij.ops.CLIJ_maximumBox.CLIJ_maximumBox;
import net.haesleinhuepf.clij.ops.CLIJ_medianSliceBySliceSphere.CLIJ_medianSliceBySliceSphere;
import net.haesleinhuepf.clij.ops.CLIJ_minimumBox.CLIJ_minimumBox;
import net.haesleinhuepf.clij.ops.CLIJ_pull.CLIJ_pull;
import net.haesleinhuepf.clij.ops.CLIJ_push.CLIJ_push;
import net.haesleinhuepf.clij.ops.CLIJ_subtractImages.CLIJ_subtractImages;
import net.imagej.ImageJ;

public class TopHat {

	@Test
	public void run() throws IOException {
		int radiusXY = 10;
		int radiusZ = 0;
		ImageJ ij = new ImageJ();
		Object input = ij.io().open(getClass().getResource("/samples/t1-head.tif").getPath());
		ij.ui().show("input", input);
		// push image to GPU
		Object inputGPU = ij.op().run(CLIJ_push.class, input);
		// median slice by slice
		Object median = ij.op().run(CLIJ_medianSliceBySliceSphere.class, inputGPU,
			1, 1);
		Object temp1 = ij.op().run(CLIJ_minimumBox.class, median, radiusXY,
			radiusXY, radiusZ);
		Object temp2 = ij.op().run(CLIJ_maximumBox.class, temp1, radiusXY, radiusXY,
			radiusZ);
		Object output = ij.op().run(CLIJ_subtractImages.class, median, temp2);

		// show result
		Object result = ij.op().run(CLIJ_pull.class, output);
		ij.ui().show("top hat", result);

		// cleanup
		ij.op().run(CLIJ_close.class, inputGPU);
		ij.op().run(CLIJ_close.class, median);
		ij.op().run(CLIJ_close.class, temp1);
		ij.op().run(CLIJ_close.class, temp2);
		ij.op().run(CLIJ_close.class, output);
		ij.op().run(CLIJ_close.class);

	}

	public static void main(String... args) throws IOException {
		TopHat task = new TopHat();
		task.run();
	}
}

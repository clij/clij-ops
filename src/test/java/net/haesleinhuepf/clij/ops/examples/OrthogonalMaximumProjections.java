
package net.haesleinhuepf.clij.ops.examples;

import java.io.IOException;

import org.junit.Test;

import net.haesleinhuepf.clij.ops.CLIJ_close.CLIJ_close;
import net.haesleinhuepf.clij.ops.CLIJ_maximumXYZProjection.CLIJ_maximumXYZProjection;
import net.haesleinhuepf.clij.ops.CLIJ_pull.CLIJ_pull;
import net.haesleinhuepf.clij.ops.CLIJ_push.CLIJ_push;
import net.imagej.ImageJ;

public class OrthogonalMaximumProjections {

	@Test
	public void run() throws IOException {
		int angle_step = 10;
		ImageJ ij = new ImageJ();
		Object input = ij.io().open(getClass().getResource("/samples/t1-head.tif").getPath());
		ij.ui().show("input", input);
		// push image to GPU
		Object inputGPU = ij.op().run(CLIJ_push.class, input);

		float downScalingFactorInXY = 0.666f; // because the image has slice
																					// distance 1.5
		float downScalingFactorInZ = 1;

		Object downscaled = ij.op().run(
			net.haesleinhuepf.clij.ops.transform.downsample.CLIJ_downsample.class,
			inputGPU, downScalingFactorInXY, downScalingFactorInXY,
			downScalingFactorInZ);

		Object maximumProjectionX = ij.op().run(CLIJ_maximumXYZProjection.class,
			downscaled, 2, 1, 0);
		Object maximumProjectionY = ij.op().run(CLIJ_maximumXYZProjection.class,
			downscaled, 2, 0, 1);
		Object maximumProjectionZ = ij.op().run(CLIJ_maximumXYZProjection.class,
			downscaled, 0, 1, 2);

		// show results
		Object resultX = ij.op().run(CLIJ_pull.class, maximumProjectionX);
		Object resultY = ij.op().run(CLIJ_pull.class, maximumProjectionY);
		Object resultZ = ij.op().run(CLIJ_pull.class, maximumProjectionZ);
		ij.ui().show("Maximum projection in X", resultX);
		ij.ui().show("Maximum projection in Y", resultY);
		ij.ui().show("Maximum projection in Z", resultZ);

		// cleanup
		ij.op().run(CLIJ_close.class, inputGPU);
		ij.op().run(CLIJ_close.class, downscaled);
		ij.op().run(CLIJ_close.class, maximumProjectionX);
		ij.op().run(CLIJ_close.class, maximumProjectionY);
		ij.op().run(CLIJ_close.class, maximumProjectionZ);
		ij.op().run(CLIJ_close.class);

	}

	public static void main(String... args) throws IOException {
		OrthogonalMaximumProjections task = new OrthogonalMaximumProjections();
		task.run();
	}
}

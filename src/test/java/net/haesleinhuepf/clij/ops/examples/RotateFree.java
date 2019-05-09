
package net.haesleinhuepf.clij.ops.examples;

import java.io.IOException;

import org.junit.Test;

import net.haesleinhuepf.clij.ops.CLIJ_close.CLIJ_close;
import net.haesleinhuepf.clij.ops.CLIJ_copySlice.CLIJ_copySlice;
import net.haesleinhuepf.clij.ops.CLIJ_pull.CLIJ_pull;
import net.haesleinhuepf.clij.ops.CLIJ_push.CLIJ_push;
import net.haesleinhuepf.clij.ops.CLIJ_rotate.CLIJ_rotate2D;
import net.imagej.ImageJ;
import net.imglib2.FinalDimensions;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;

public class RotateFree {

	@Test
	public void run() throws IOException {

		int angle_step = 1;

		ImageJ ij = new ImageJ();
		Img blobs = (Img) ij.io().open("https://bds.mpi-cbg.de/samples/blobs.png");
		ij.ui().show("input", blobs);
		Img blobs32 = ij.op().convert().float32(blobs);
		// reserve the right amount of memory for the result image
		Object target = ij.op().create().img(new FinalDimensions(blobs.dimension(0),
			blobs.dimension(1), 360 / angle_step), new FloatType());
		// push images to GPU
		Object blobsGPU = ij.op().run(CLIJ_push.class, blobs32);
		Object targetGPU = ij.op().run(CLIJ_push.class, target);

		int count = 0;
		for (int angle = 0; angle < 360; angle += angle_step) {
			Object rotated = ij.op().run(CLIJ_rotate2D.class, blobsGPU, angle, true);
			// put the rotated image in the right place in the result stack
			ij.op().run(CLIJ_copySlice.class, targetGPU, rotated, count);
			ij.op().run(CLIJ_close.class, rotated);
			count++;
		}
		// show result
		Object result = ij.op().run(CLIJ_pull.class, targetGPU);
		ij.ui().show("freely rotated", result);

		// cleanup
		ij.op().run(CLIJ_close.class, blobsGPU);
		ij.op().run(CLIJ_close.class, targetGPU);
		ij.op().run(CLIJ_close.class);

	}

	public static void main(String... args) throws IOException {
		RotateFree task = new RotateFree();
		task.run();
	}
}

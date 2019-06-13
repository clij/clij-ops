
package net.haesleinhuepf.clij.ops.examples;

import java.io.IOException;

import org.junit.Test;

import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.ops.CLIJ_close.CLIJ_close;
import net.haesleinhuepf.clij.ops.CLIJ_copySlice.CLIJ_copySlice;
import net.haesleinhuepf.clij.ops.CLIJ_create.CLIJ_create;
import net.haesleinhuepf.clij.ops.CLIJ_pull.CLIJ_pull;
import net.haesleinhuepf.clij.ops.CLIJ_push.CLIJ_push;
import net.haesleinhuepf.clij.ops.CLIJ_scale.CLIJ_scale;
import net.imagej.ImageJ;
import net.imglib2.img.Img;

public class ScaleFree {

	@Test
	public void run() throws IOException {
		double zoom_step = 0.03;

		ImageJ ij = new ImageJ();
		Img blobs = (Img) ij.io().open("https://bds.mpi-cbg.de/samples/blobs.png");
		ij.ui().show("input", blobs);
		// push images to GPU
		Object blobsGPU = ij.op().run(CLIJ_push.class, blobs);

		int count = 0;
		Object target = ij.op().run(CLIJ_create.class, new long[] { blobs.dimension(
			0), blobs.dimension(1), (long) (1.0 / zoom_step) }, NativeTypeEnum.Float);
		for (float zoom = 1; zoom > 0; zoom -= zoom_step) {
			Object zoomed = ij.op().run(CLIJ_scale.class, blobsGPU, zoom, true);
			// put the zoomed image in the right place in the result stack
			ij.op().run(CLIJ_copySlice.class, target, zoomed, count);
			ij.op().run(CLIJ_close.class, zoomed);
			count++;
		}
		// show result
		Object result = ij.op().run(CLIJ_pull.class, target);
		ij.ui().show("free scale", result);

		// cleanup
		ij.op().run(CLIJ_close.class, blobsGPU);
		ij.op().run(CLIJ_close.class, target);


	}

	public static void main(String... args) throws IOException {
		ScaleFree task = new ScaleFree();
		task.run();
	}
}

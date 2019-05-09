
package net.haesleinhuepf.clij.ops.examples;

import java.io.IOException;

import net.haesleinhuepf.clij.ops.CLIJ_push.CLIJ_push;
import org.junit.Test;

import net.haesleinhuepf.clij.ops.CLIJ_close.CLIJ_close;
import net.haesleinhuepf.clij.ops.CLIJ_pull.CLIJ_pull;
import net.haesleinhuepf.clij.ops.CLIJ_threshold.CLIJ_threshold;
import net.imagej.ImageJ;

public class Threshold {

	@Test
	public void run() throws IOException {
		ImageJ ij = new ImageJ();
		Object blobs = ij.io().open("https://bds.mpi-cbg.de/samples/blobs.png");
		ij.ui().show("input", blobs);
		// push images to GPU
		Object blobsGPU = ij.op().run(CLIJ_push.class, blobs);
		// run threshold
		int threshold = 128;
		Object maskGPU = ij.op().run(CLIJ_threshold.class, blobsGPU, threshold);
		// show result
		Object result = ij.op().run(CLIJ_pull.class, maskGPU);
		ij.ui().show("threshold", result);

		// cleanup
		ij.op().run(CLIJ_close.class, blobsGPU);
		ij.op().run(CLIJ_close.class, maskGPU);
		ij.op().run(CLIJ_close.class);

	}

	public static void main(String... args) throws IOException {
		Threshold task = new Threshold();
		task.run();
	}
}

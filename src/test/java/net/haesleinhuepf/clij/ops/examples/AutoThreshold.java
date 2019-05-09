
package net.haesleinhuepf.clij.ops.examples;

import java.io.IOException;

import org.junit.Test;

import net.haesleinhuepf.clij.ops.CLIJ_automaticThreshold.CLIJ_automaticThreshold;
import net.haesleinhuepf.clij.ops.CLIJ_close.CLIJ_close;
import net.haesleinhuepf.clij.ops.CLIJ_pull.CLIJ_pull;
import net.haesleinhuepf.clij.ops.CLIJ_push.CLIJ_push;
import net.imagej.ImageJ;
import net.imglib2.img.Img;

public class AutoThreshold {

	@Test
	public void run() throws IOException {
		ImageJ ij = new ImageJ();
		Img blobs = (Img) ij.io().open("https://bds.mpi-cbg.de/samples/blobs.png");
		ij.ui().show("input", blobs);
		// push image to GPU
		Object blobsGPU = ij.op().run(CLIJ_push.class, blobs);
		// run threshold
		Object maskGPU = ij.op().run(CLIJ_automaticThreshold.class, blobsGPU,
			"Otsu");
		// show result
		Object result = ij.op().run(CLIJ_pull.class, maskGPU);
		ij.ui().show("auto threshold", result);
		// cleanup
		ij.op().run(CLIJ_close.class, blobsGPU);
		ij.op().run(CLIJ_close.class, maskGPU);
		ij.op().run(CLIJ_close.class);

	}

	public static void main(String... args) throws IOException {
		AutoThreshold task = new AutoThreshold();
		task.run();
	}
}

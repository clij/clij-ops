
package net.haesleinhuepf.clij.ops.examples;

import java.io.IOException;

import org.junit.Test;

import net.haesleinhuepf.clij.ops.CLIJ_binaryXOr.CLIJ_binaryXOr;
import net.haesleinhuepf.clij.ops.CLIJ_close.CLIJ_close;
import net.haesleinhuepf.clij.ops.CLIJ_erodeBox.CLIJ_erodeBox;
import net.haesleinhuepf.clij.ops.CLIJ_meanBox.CLIJ_meanBox;
import net.haesleinhuepf.clij.ops.CLIJ_pull.CLIJ_pull;
import net.haesleinhuepf.clij.ops.CLIJ_push.CLIJ_push;
import net.haesleinhuepf.clij.ops.CLIJ_threshold.CLIJ_threshold;
import net.imagej.ImageJ;

public class CreateObjectOutlines {

	@Test
	public void run() throws IOException {
		ImageJ ij = new ImageJ();
		Object blobs = ij.io().open("https://bds.mpi-cbg.de/samples/blobs.png");
		ij.ui().show("input", blobs);
		// push images to GPU
		Object blobsGPU = ij.op().run(CLIJ_push.class, blobs);
		Object mean2DBox = ij.op().run(CLIJ_meanBox.class, blobsGPU, 2.0, 2.0);
		Object threshold = ij.op().run(CLIJ_threshold.class, mean2DBox, 127.0);
		Object erodeBox = ij.op().run(CLIJ_erodeBox.class, threshold);
		Object binaryXOr = ij.op().run(CLIJ_binaryXOr.class, threshold, erodeBox);

		// show result
		Object result = ij.op().run(CLIJ_pull.class, binaryXOr);
		ij.ui().show("object outlines", result);

		// cleanup
		ij.op().run(CLIJ_close.class, blobsGPU);
		ij.op().run(CLIJ_close.class, mean2DBox);
		ij.op().run(CLIJ_close.class, threshold);
		ij.op().run(CLIJ_close.class, erodeBox);
		ij.op().run(CLIJ_close.class, binaryXOr);
		ij.op().run(CLIJ_close.class);

	}

	public static void main(String... args) throws IOException {
		CreateObjectOutlines task = new CreateObjectOutlines();
		task.run();
	}
}


package net.haesleinhuepf.clij.ops.examples;

import java.io.IOException;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import org.junit.Test;

import net.haesleinhuepf.clij.ops.CLIJ_close.CLIJ_close;
import net.haesleinhuepf.clij.ops.CLIJ_dilateBox.CLIJ_dilateBox;
import net.haesleinhuepf.clij.ops.CLIJ_erodeBox.CLIJ_erodeBox;
import net.haesleinhuepf.clij.ops.CLIJ_pull.CLIJ_pull;
import net.haesleinhuepf.clij.ops.CLIJ_push.CLIJ_push;
import net.haesleinhuepf.clij.ops.CLIJ_threshold.CLIJ_threshold;
import net.imagej.ImageJ;
import net.imglib2.img.Img;

public class BinaryProcessing {

	@Test
	public void run() throws IOException {
		ImageJ ij = new ImageJ();
		Img input = (Img) ij.io().open("https://bds.mpi-cbg.de/samples/blobs.png");
		ij.ui().show("input", input);
		// push image to GPU
		Object inputGPU = ij.op().run(CLIJ_push.class, input);

		int threshold = 128;
		Object mask = ij.op().run(CLIJ_threshold.class, inputGPU, threshold);
		Object temp = ij.op().run(CLIJ_erodeBox.class, mask);
		mask = ij.op().run(CLIJ_erodeBox.class, temp);
		temp = ij.op().run(CLIJ_dilateBox.class, mask);
		mask = ij.op().run(CLIJ_dilateBox.class, temp);
		// show result
		Object result = ij.op().run(CLIJ_pull.class, mask);
		ij.ui().show("binary processing", result);

		// cleanup
		ij.op().run(CLIJ_close.class, inputGPU);
		ij.op().run(CLIJ_close.class, mask);
		ij.op().run(CLIJ_close.class, temp);
		ij.op().run(CLIJ_close.class);

	}

	public static void main(String... args) throws IOException {
		BinaryProcessing task = new BinaryProcessing();
		task.run();
	}
}

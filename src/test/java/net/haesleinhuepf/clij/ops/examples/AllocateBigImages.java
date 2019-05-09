
package net.haesleinhuepf.clij.ops.examples;

import java.io.IOException;

import org.junit.Test;

import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.ops.CLIJ_close.CLIJ_close;
import net.haesleinhuepf.clij.ops.CLIJ_create.CLIJ_create;
import net.haesleinhuepf.clij.ops.CLIJ_pull.CLIJ_pull;
import net.haesleinhuepf.clij.ops.CLIJ_push.CLIJ_push;
import net.imagej.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;

public class AllocateBigImages {

	@Test
	public void run() throws IOException {
		ImageJ ij = new ImageJ();
		Img input = (Img) ij.io().open("https://bds.mpi-cbg.de/samples/blobs.png");
		ij.ui().show("input", input);

		// push images to GPU
		Object inputGPU = ij.op().run(CLIJ_push.class, input);

		// create an 800 MB image in GPU memory
		Object bigStack = ij.op().run(CLIJ_create.class, new long[] { 2048, 2048,
			100 }, NativeTypeEnum.UnsignedShort);

		for (int i = 0; i < 1; i++) {
			// fill the image with content
			ij.op().run("CLIJ_copySlice", bigStack, inputGPU, i);
		}

		Object crop = ij.op().run("CLIJ_crop", bigStack, 0, 0, 0, 150, 150, 10);

		// Get results back from GPU
		RandomAccessibleInterval result = (RandomAccessibleInterval) ij.op().run(
			CLIJ_pull.class, crop);
		ij.ui().show("crop of big image", result);
		// cleanup
		ij.op().run(CLIJ_close.class, inputGPU);
		ij.op().run(CLIJ_close.class, bigStack);
		ij.op().run(CLIJ_close.class);

	}

	public static void main(String... args) throws IOException {
		AllocateBigImages task = new AllocateBigImages();
		task.run();
	}

}

package net.haesleinhuepf.clij.ops.examples;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.ops.reviewed.dilateBoxCLIJ.DilateBoxCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.erodeBoxCLIJ.ErodeBoxCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.thresholdCLIJ.ThresholdCLIJ;
import net.imagej.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;

import java.io.IOException;

public class BinaryProcessing {

	public void run() throws IOException {
		ImageJ ij = new ImageJ();
		Img input = (Img) ij.io().open("https://imagej.nih.gov/ij/images/blobs.gif");
		// init GPU
		CLIJ clij = CLIJ.getInstance();
		// push image to GPU
		ClearCLBuffer inputGPU = clij.push(input);

		int threshold = 128;
		Object mask = ij.op().run(ThresholdCLIJ.class, inputGPU, threshold);
		Object temp = ij.op().run(ErodeBoxCLIJ.class, mask);
		mask = ij.op().run(ErodeBoxCLIJ.class, temp);
		temp = ij.op().run(DilateBoxCLIJ.class, mask);
		mask = ij.op().run(DilateBoxCLIJ.class, temp);
		// show result
		RandomAccessibleInterval result = clij.convert(mask, RandomAccessibleInterval.class);
		ij.ui().show(result);
		clij.close();
	}

	public static void main(String ... args) throws IOException {
		BinaryProcessing task = new BinaryProcessing();
		task.run();
	}
}

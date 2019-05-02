package net.haesleinhuepf.clij.ops.examples;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.ops.reviewed.automaticThresholdCLIJ.AutomaticThresholdCLIJ;
import net.imagej.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;

import java.io.IOException;

public class AutoThreshold {

	public void run() throws IOException {
		ImageJ ij = new ImageJ();
		Img blobs = (Img) ij.io().open("https://imagej.nih.gov/ij/images/blobs.gif");
		// init GPU
		CLIJ clij = CLIJ.getInstance();
		// push image to GPU
		ClearCLBuffer blobsGPU = clij.push(blobs);
		// run threshold
		ClearCLBuffer maskGPU = (ClearCLBuffer) ij.op().run(AutomaticThresholdCLIJ.class, blobsGPU, "Otsu");
		// show result
		RandomAccessibleInterval result = clij.convert(maskGPU, RandomAccessibleInterval.class);
		ij.ui().show(result);
	}

	public static void main(String ... args) throws IOException {
		AutoThreshold task = new AutoThreshold();
		task.run();
	}
}

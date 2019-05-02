package net.haesleinhuepf.clij.ops.examples;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.ops.reviewed.addImagesWeightedCLIJ.AddImagesWeightedCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.blurCLIJ.BlurCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.maxProjection.MaximumZProjectionCLIJ;
import net.imagej.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;

import java.io.IOException;

public class BackgroundSubtraction {

	public void run() throws IOException {
		ImageJ ij = new ImageJ();
		Img input = (Img) ij.io().open("https://imagej.nih.gov/ij/images/t1-head.gif");
		RandomAccessibleInterval singleChannelInput = ij.op().transform().hyperSliceView(input, 2, 0);
		// init GPU
		CLIJ clij = CLIJ.getInstance();
		// push image to GPU
		ClearCLBuffer inputGPU = clij.push(singleChannelInput);

		ClearCLBuffer background = (ClearCLBuffer) ij.op().run(BlurCLIJ.class, inputGPU, 10, 10, 1);
		ClearCLBuffer background_subtracted = (ClearCLBuffer) ij.op().run(AddImagesWeightedCLIJ.class, inputGPU, background, 1, -1);
		ClearCLBuffer maximum_projected = (ClearCLBuffer) ij.op().run(MaximumZProjectionCLIJ.class, background_subtracted);

		// show result
		RandomAccessibleInterval result = clij.convert(maximum_projected, RandomAccessibleInterval.class);
		ij.ui().show(result);
		clij.close();
	}

	public static void main(String ... args) throws IOException {
		BackgroundSubtraction task = new BackgroundSubtraction();
		task.run();
	}
}

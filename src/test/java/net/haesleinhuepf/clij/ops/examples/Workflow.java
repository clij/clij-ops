package net.haesleinhuepf.clij.ops.examples;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.ops.reviewed.addImagesWeightedCLIJ.AddImagesWeightedCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.blurCLIJ.BlurCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.filter.reslice.ResliceLeftCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.filter.reslice.ResliceTopCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.maxProjection.MaximumZProjectionCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.maximumImageAndScalarCLIJ.MaximumImageAndScalarCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.radialProjection.RadialProjectionCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.transform.downsample.DownsampleCLIJ;
import net.imagej.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;

import java.io.IOException;

public class Workflow {

	static double smallBlurSigmaInPixels = 2;
	static double blurSigmaInPixels = 6;
	static double sampleX = 0.52;
	static double sampleY = 0.52;
	static double sampleZ = 2.0;

	public void run() throws IOException {
		ImageJ ij = new ImageJ();
		Img blobs = (Img) ij.io().open("https://imagej.nih.gov/ij/images/blobs.gif");
		Img imginput = ij.op().convert().float32(blobs);
		// init GPU
		//		CLIJ clij = CLIJ.getInstance("Intel(R) HD Graphics Kabylake Desktop GT1.5");
		CLIJ clij = CLIJ.getInstance();

		ClearCLBuffer original = clij.convert(imginput, ClearCLBuffer.class);

		ClearCLBuffer firstFiltered = (ClearCLBuffer) ij.op().run("blurCLIJ", original, smallBlurSigmaInPixels, smallBlurSigmaInPixels, 0);
		ClearCLBuffer secondFiltered = (ClearCLBuffer) ij.op().run(BlurCLIJ.class, original, blurSigmaInPixels, blurSigmaInPixels, 0);
		ClearCLBuffer imageDoG = (ClearCLBuffer) ij.op().run(AddImagesWeightedCLIJ.class, firstFiltered, secondFiltered, 1.0, -1.0);
		ClearCLBuffer positiveStack = (ClearCLBuffer) ij.op().run(MaximumImageAndScalarCLIJ.class, imageDoG, 1.0);
		ClearCLBuffer scaled = (ClearCLBuffer) ij.op().run(DownsampleCLIJ.class, positiveStack, sampleX, sampleY, sampleZ);
		ClearCLBuffer reslicedFromTop = (ClearCLBuffer) ij.op().run(ResliceTopCLIJ.class, scaled);
		ClearCLBuffer radialResliced = (ClearCLBuffer) ij.op().run(RadialProjectionCLIJ.class, reslicedFromTop, 360, 1.0);
		ClearCLBuffer reslicedFromLeft = (ClearCLBuffer) ij.op().run(ResliceLeftCLIJ.class, radialResliced);
		ClearCLBuffer maxProjected = (ClearCLBuffer) ij.op().run(MaximumZProjectionCLIJ.class, reslicedFromLeft);
		ij.ui().show(clij.convert(maxProjected, RandomAccessibleInterval.class));
	}

	public static void main(String ... args) throws IOException {
		Workflow task = new Workflow();
		task.run();
	}
}

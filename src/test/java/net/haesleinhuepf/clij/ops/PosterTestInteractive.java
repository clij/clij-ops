package net.haesleinhuepf.clij.ops;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.ops.generated.addImagesWeightedCLIJ.AddImagesWeightedCLIJ;
import net.haesleinhuepf.clij.ops.generated.blurCLIJ.BlurCLIJ;
import net.haesleinhuepf.clij.ops.generated.maximumImageAndScalarCLIJ.MaximumImageAndScalarCLIJ;
import net.haesleinhuepf.clij.ops.transform.downsample.DownsampleCLIJ;
import net.imagej.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;

import java.io.IOException;

public class PosterTestInteractive {

	static double smallBlurSigmaInPixels = 2;
	static double blurSigmaInPixels = 6;
	static double sampleX = 0.52;
	static double sampleY = 0.52;
	static double sampleZ = 2.0;

	public static void main(String ... args) throws IOException {
		ImageJ ij = new ImageJ();
		ij.ui().showUI();
		Img input = (Img) ij.io().open("/home/random/Development/imagej/project/clij/data/Drosophila_1.tif");

//		long timeOpStart = System.currentTimeMillis();
//
//		ij.ui().show("CPU", runOpVersion(ij, input));
//
//		long timeOpEnd = System.currentTimeMillis();
//
//		System.out.println("ImageJ2 Op version took " + (timeOpEnd - timeOpStart) + " ms");
//
//		input = (Img) ij.io().open("/home/random/lena.tif");

		long timeOpCLIJStart = System.currentTimeMillis();

		ij.ui().show("GPU", runOpCLIJVersion(ij, input));

		long timeOpCLIJEnd = System.currentTimeMillis();

		System.out.println("ImageJ2 CLIJ Op version took " + (timeOpCLIJEnd - timeOpCLIJStart) + " ms");
	}

	static Img runOpCLIJVersion(ImageJ ij, Img _imginput) {
		CLIJ clij = CLIJ.getInstance("Intel(R) HD Graphics Kabylake Desktop GT1.5");
		Img imginput = ij.op().convert().float32(_imginput);

		ClearCLBuffer original = clij.convert(imginput, ClearCLBuffer.class);
		ClearCLBuffer firstFiltered = clij.create(original);

		//blur

		ij.op().run(BlurCLIJ.class, firstFiltered, original, smallBlurSigmaInPixels, smallBlurSigmaInPixels, 0);

		ClearCLBuffer secondFiltered = clij.create(original);

		ij.op().run(BlurCLIJ.class, secondFiltered, original, blurSigmaInPixels, blurSigmaInPixels, 0);
		ClearCLBuffer imageDoG = clij.create(original);
		ij.op().run(AddImagesWeightedCLIJ.class, imageDoG, firstFiltered, secondFiltered, 1.0, -1.0);
		ClearCLBuffer positiveStack = clij.create(original);
		ij.op().run(MaximumImageAndScalarCLIJ.class, positiveStack, imageDoG, 1.0);
		//reslice
		ClearCLBuffer scaled = (ClearCLBuffer) ij.op().run(DownsampleCLIJ.class, positiveStack, sampleX, sampleY, sampleZ);
//		ClearCLBuffer reslicedFromTop = clij.create(original);
//		ij.op().run(ResliceTopCLIJ.class, reslicedFromTop, scaled);
//		ClearCLBuffer radialResliced = clij.create(original);
//		ij.op().run(RadialProjectionCLIJ.class, radialResliced, reslicedFromTop, /*360.0, */1.0);
//		ClearCLBuffer reslicedFromLeft = clij.create(original);
//		ij.op().run(ResliceLeftCLIJ.class, reslicedFromLeft, radialResliced);
//		ClearCLBuffer maxProjected = clij.create(original);
//		ij.op().run(MaximumZProjectionCLIJ.class, maxProjected, reslicedFromLeft);

		ClearCLBuffer res = scaled;

		return (Img) clij.convert(res, RandomAccessibleInterval.class);

	}
}

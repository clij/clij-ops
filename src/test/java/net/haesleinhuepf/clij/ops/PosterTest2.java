package net.haesleinhuepf.clij.ops;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.ops.reviewed.addImagesWeightedCLIJ.AddImagesWeightedCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.blurCLIJ.BlurCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.maximumImageAndScalarCLIJ.MaximumImageAndScalarCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.maxProjection.MaximumZProjectionCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.radialProjection.RadialProjectionCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.filter.reslice.ResliceLeftCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.filter.reslice.ResliceTopCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.transform.downsample.DownsampleCLIJ;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.HyperSphereShape;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;
import org.junit.Test;

public class PosterTest2 extends CLIJOpsTest {

	Img input = createAscendingImage();

	static double smallBlurSigmaInPixels = 2;
	static double blurSigmaInPixels = 6;
	static double sampleX = 0.52;
	static double sampleY = 0.52;
	static double sampleZ = 2.0;

	@Test
	public void benchmarkPoster2() {
		int runs = 3;
		for (int i = 0; i < runs; i++) {
			long timeOpStart = System.currentTimeMillis();
			runOpVersion(input);
			long timeOpEnd = System.currentTimeMillis();
			System.out.println("ImageJ2 Op version took " + (timeOpEnd - timeOpStart) + " ms");
		}
		for (int i = 0; i < runs; i++) {
			long timeOpCLIJStart = System.currentTimeMillis();
			runOpCLIJVersion(input);
			long timeOpCLIJEnd = System.currentTimeMillis();
			System.out.println("ImageJ2 CLIJ Op version took " + (timeOpCLIJEnd - timeOpCLIJStart) + " ms");
		}
	}

	@Test
	public void comparePoster2Results() {
//		Img outputOp = runOpVersion(input);
		Img outputCLIJ = runOpCLIJVersion(input);
//		compare(outputOp, outputCLIJ);
	}

	private Img runOpCLIJVersion(Img _imginput) {
		CLIJ clij = CLIJ.getInstance();
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
		ClearCLBuffer scaled = clij.create(original);
		ij.op().run(DownsampleCLIJ.class, scaled, positiveStack, sampleX, sampleY, sampleZ);
		ClearCLBuffer reslicedFromTop = clij.create(original);
		ij.op().run(ResliceTopCLIJ.class, reslicedFromTop, scaled);
		ClearCLBuffer radialResliced = clij.create(original);
		ij.op().run(RadialProjectionCLIJ.class, radialResliced, reslicedFromTop, /*360.0, */1.0);
		ClearCLBuffer reslicedFromLeft = clij.create(original);
		ij.op().run(ResliceLeftCLIJ.class, reslicedFromLeft, radialResliced);
		ClearCLBuffer maxProjected = clij.create(original);
		ij.op().run(MaximumZProjectionCLIJ.class, maxProjected, reslicedFromLeft);

		return (Img) clij.convert(maxProjected, RandomAccessibleInterval.class);

	}

	private Img runOpVersion(Img imginput) {
		Img input = ij.op().convert().float32(imginput);
		Img output = input.copy();

		FloatType value = new FloatType(5);
		ij.op().math().add((IterableInterval)output, input, value);
		input = output; output = input.copy();
		double sigma = 5;
		ij.op().filter().gauss(output, input, sigma);
		input = output; output = input.copy();
		long radius = 5;
		ij.op().filter().mean(output, input, new HyperSphereShape(radius));
		return output;
	}


//		Ext.CLIJ_push(original);
//
//	// first Gaussian blur
//	firstFiltered = "firstFiltered";
//	Ext.CLIJ_blur3DFast(original, firstFiltered, smallBlurSigmaInPixels,
//	smallBlurSigmaInPixels, 0);
//
//
//
//	// second Gaussian blur
//	secondFiltered = "secondFiltered";
//	Ext.CLIJ_blur3DFast(original, secondFiltered, blurSigmaInPixels,
//	blurSigmaInPixels, 0);
//	// calculate DoG image
//	imageDoG = "imageDoG";
//	Ext.CLIJ_addImagesWeighted(firstFiltered, secondFiltered, imageDoG, 1.0, -1.0);
//
//
//	positiveStack = "positiveStack";
//	Ext.CLIJ_maximumImageAndScalar(imageDoG, positiveStack, 1.0);
//
//	endTimeBlurring = getTime();
//	startTimeReslicing = getTime();
//
//	scaled = "scaled";
//	Ext.CLIJ_downsample3D(positiveStack, scaled, sampleX, sampleY, sampleZ);
//	//Ext.CLIJ_pull(scaled);
//
//
//	reslicedFromTop = "reslicedFromTop";
//	Ext.CLIJ_resliceTop(scaled, reslicedFromTop);
//	//Ext.CLIJ_pull(reslicedFromTop);
//
//	radialResliced = "radialResliced";
//	Ext.CLIJ_resliceRadial(reslicedFromTop, radialResliced, 360.0, 1.0);
//	//Ext.CLIJ_pull(radialResliced);
//	//break;
//	reslicedFromLeft = "reslicedFromLeft";
//	Ext.CLIJ_resliceLeft(radialResliced, reslicedFromLeft);
//
//	maxProjected = "maxProjected";
//	Ext.CLIJ_maximumZProjection(reslicedFromLeft, maxProjected);
//	//Ext.CLIJ_pull(maxProjected);
//	//rename("maxp");
//
//	Ext.CLIJ_pull(maxProjected);


}

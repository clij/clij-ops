package net.haesleinhuepf.clij.ops;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.ops.reviewed.addImagesWeightedCLIJ.AddImagesWeightedCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.blurCLIJ.BlurCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.maximumImageAndScalarCLIJ.MaximumImageAndScalarCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.filter.reslice.ResliceLeftCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.filter.reslice.ResliceTopCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.maxProjection.MaximumZProjectionCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.radialProjection.RadialProjectionCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.transform.downsample.DownsampleCLIJ;
import net.imagej.ImageJ;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.loops.LoopBuilder;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import java.io.IOException;
import java.util.function.BiConsumer;

public class PosterTestInteractive {

	static double smallBlurSigmaInPixels = 2;
	static double blurSigmaInPixels = 6;
	static double sampleX = 0.52;
	static double sampleY = 0.52;
	static double sampleZ = 2.0;

	public static void main(String ... args) throws IOException {
		ImageJ ij = new ImageJ();
		ij.ui().showUI();
		Img input = (Img) ij.io().open("/home/random/Development/imagej/project/clij/data/Drosophila_1-1.tif");

//		long timeOpStart = System.currentTimeMillis();
//
//		ij.ui().show("CPU", runOpVersion(ij, input));
//
//		long timeOpEnd = System.currentTimeMillis();
//
//		System.out.println("ImageJ2 Op version took " + (timeOpEnd - timeOpStart) + " ms");

		long timeOpCLIJStart = System.currentTimeMillis();

		ij.ui().show("CLIJ Version", runOpCLIJVersion(ij, input));

		long timeOpCLIJEnd = System.currentTimeMillis();

		System.out.println("ImageJ2 CLIJ Op version took " + (timeOpCLIJEnd - timeOpCLIJStart) + " ms");
	}

	private static <T extends RealType<T> & NativeType<T>> Img runOpVersion(ImageJ ij, Img _input) {
		Img input = ij.op().convert().float32(_input);
		RandomAccessibleInterval firstFiltered = ij.op().filter().gauss(input, smallBlurSigmaInPixels, smallBlurSigmaInPixels, 0);
		RandomAccessibleInterval secondFiltered = ij.op().filter().gauss(input, blurSigmaInPixels, blurSigmaInPixels, 0);
		RandomAccessibleInterval imageDoG = (RandomAccessibleInterval) ij.op().math().subtract((IterableInterval)secondFiltered, firstFiltered);
		RandomAccessibleInterval positiveStack = input.factory().create(imageDoG);
		LoopBuilder<BiConsumer<T, T>> lp = LoopBuilder.setImages(imageDoG, positiveStack);
		long indices = 1;
		for (int i = 0; i < imageDoG.numDimensions(); i++) {
			indices *= imageDoG.dimension(i);
		}
		T one = ((T)imageDoG.randomAccess().get()).copy();
		one.setOne();
		lp.forEachPixel((in, out) -> {
			out.set(in.compareTo(one) > 0 ? in : one);
		});
//		RandomAccessibleInterval scaled = ij.op().transform().scaleView(positiveStack, new double[]{sampleX, sampleY, sampleZ}, new NearestNeighborInterpolatorFactory());
//		System.out.println("Scaling done");
		ij.ui().show(positiveStack);
		return null;
	}

	static Img runOpCLIJVersion(ImageJ ij, Img _imginput) {
//		CLIJ clij = CLIJ.getInstance("Intel(R) HD Graphics Kabylake Desktop GT1.5");
		CLIJ clij = CLIJ.getInstance();
		Img imginput = ij.op().convert().float32(_imginput);

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

		return (Img) clij.convert(maxProjected, RandomAccessibleInterval.class);

	}
}

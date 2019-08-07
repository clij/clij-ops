
package net.haesleinhuepf.clij.ops.misc;

import java.io.IOException;
import java.util.function.BiConsumer;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.ops.CLIJ_addImagesWeighted.CLIJ_addImagesWeighted;
import net.haesleinhuepf.clij.ops.CLIJ_blur.CLIJ_blur;
import net.haesleinhuepf.clij.ops.CLIJ_close.CLIJ_close;
import net.haesleinhuepf.clij.ops.CLIJ_maxProjection.CLIJ_maximumZProjection;
import net.haesleinhuepf.clij.ops.CLIJ_maximumImageAndScalar.CLIJ_maximumImageAndScalar;
import net.haesleinhuepf.clij.ops.CLIJ_radialProjection.CLIJ_radialProjection;
import net.haesleinhuepf.clij.ops.CLIJ_reslice.CLIJ_resliceLeft;
import net.haesleinhuepf.clij.ops.CLIJ_reslice.CLIJ_resliceTop;
import net.imagej.ImageJ;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.loops.LoopBuilder;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

public class PosterTestInteractive {

	static double smallBlurSigmaInPixels = 2;
	static double blurSigmaInPixels = 6;
	static double sampleX = 0.52;
	static double sampleY = 0.52;
	static double sampleZ = 2.0;

	public static void main(String... args) throws IOException {
		ImageJ ij = new ImageJ();
		ij.ui().showUI();
		Img input = (Img) ij.io().open(
			"/home/random/Development/imagej/project/clij/data/Drosophila_1-1.tif");

		// long timeOpStart = System.currentTimeMillis();
		//
		// ij.ui().show("CPU", runOpVersion(ij, input));
		//
		// long timeOpEnd = System.currentTimeMillis();
		//
		// System.out.println("ImageJ2 Op version took " + (timeOpEnd - timeOpStart)
		// + " ms");

		long timeOpCLIJStart = System.currentTimeMillis();

		ij.ui().show("CLIJ Version", runOpCLIJVersion(ij, input));

		long timeOpCLIJEnd = System.currentTimeMillis();

		System.out.println("ImageJ2 CLIJ Op version took " + (timeOpCLIJEnd -
			timeOpCLIJStart) + " ms");
	}

	private static <T extends RealType<T> & NativeType<T>> Img runOpVersion(
		ImageJ ij, Img _input)
	{
		Img input = ij.op().convert().float32(_input);
		RandomAccessibleInterval firstFiltered = ij.op().filter().gauss(input,
			smallBlurSigmaInPixels, smallBlurSigmaInPixels, 0);
		RandomAccessibleInterval secondFiltered = ij.op().filter().gauss(input,
			blurSigmaInPixels, blurSigmaInPixels, 0);
		RandomAccessibleInterval imageDoG = (RandomAccessibleInterval) ij.op()
			.math().subtract((IterableInterval) secondFiltered, firstFiltered);
		RandomAccessibleInterval positiveStack = input.factory().create(imageDoG);
		LoopBuilder<BiConsumer<T, T>> lp = LoopBuilder.setImages(imageDoG,
			positiveStack);
		long indices = 1;
		for (int i = 0; i < imageDoG.numDimensions(); i++) {
			indices *= imageDoG.dimension(i);
		}
		T one = ((T) imageDoG.randomAccess().get()).copy();
		one.setOne();
		lp.forEachPixel((in, out) -> {
			out.set(in.compareTo(one) > 0 ? in : one);
		});
		// RandomAccessibleInterval scaled =
		// ij.op().transform().scaleView(positiveStack, new double[]{sampleX,
		// sampleY, sampleZ}, new NearestNeighborInterpolatorFactory());
		// System.out.println("Scaling done");
		ij.ui().show(positiveStack);
		return null;
	}

	static Img runOpCLIJVersion(ImageJ ij, Img _imginput) {
		// CLIJ clij = CLIJ.getInstance("Intel(R) HD Graphics Kabylake Desktop
		// GT1.5");
		CLIJ clij = CLIJ.getInstance();
		Img imginput = ij.op().convert().float32(_imginput);

		Object original = clij.convert(imginput, ClearCLBuffer.class);

		Object firstFiltered = ij.op().run("CLIJ_blur", original,
			smallBlurSigmaInPixels, smallBlurSigmaInPixels, 0);
		Object secondFiltered = ij.op().run(CLIJ_blur.class, original,
			blurSigmaInPixels, blurSigmaInPixels, 0);
		Object imageDoG = ij.op().run(CLIJ_addImagesWeighted.class, firstFiltered,
			secondFiltered, 1.0, -1.0);
		Object positiveStack = ij.op().run(CLIJ_maximumImageAndScalar.class,
			imageDoG, 1.0);
		Object scaled = ij.op().run(
			net.haesleinhuepf.clij.ops.transform.downsample.CLIJ_downsample.class,
			positiveStack, sampleX, sampleY, sampleZ);
		Object reslicedFromTop = ij.op().run(CLIJ_resliceTop.class, scaled);
		Object radialResliced = ij.op().run(CLIJ_radialProjection.class,
			reslicedFromTop, 360, 1.0);
		Object reslicedFromLeft = ij.op().run(CLIJ_resliceLeft.class,
			radialResliced);
		Object maxProjected = ij.op().run(CLIJ_maximumZProjection.class,
			reslicedFromLeft);

		// cleanup
		ij.op().run(CLIJ_close.class, original);
		ij.op().run(CLIJ_close.class, secondFiltered);
		ij.op().run(CLIJ_close.class, imageDoG);
		ij.op().run(CLIJ_close.class, positiveStack);
		ij.op().run(CLIJ_close.class, scaled);
		ij.op().run(CLIJ_close.class, reslicedFromTop);
		ij.op().run(CLIJ_close.class, reslicedFromLeft);
		ij.op().run(CLIJ_close.class, radialResliced);
		ij.op().run(CLIJ_close.class, maxProjected);

		return (Img) clij.convert(maxProjected, RandomAccessibleInterval.class);

	}
}

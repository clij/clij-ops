package net.haesleinhuepf.clij.ops;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.ops.generated.addImageAndScalarCLIJ.AddImageAndScalarCLIJ;
import net.haesleinhuepf.clij.ops.generated.blurFastCLIJ.BlurFastCLIJ;
import net.haesleinhuepf.clij.ops.generated.meanBoxCLIJ.MeanBoxCLIJ;
import net.imagej.ImageJ;
import net.imglib2.FinalDimensions;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.HyperSphereShape;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;
import org.junit.Test;

public class PosterTest {

	@Test
	public void testPosterOpVersion() {
		ImageJ ij = new ImageJ();

		int runs = 10;

		for (int i = 0; i < runs; i++) {
			long timeOpStart = System.currentTimeMillis();

			runOpVersion(ij);

			long timeOpEnd = System.currentTimeMillis();

			System.out.println("ImageJ2 Op version took " + (timeOpEnd - timeOpStart) + " ms");
		}

		for (int i = 0; i < runs; i++) {
			long timeOpCLIJStart = System.currentTimeMillis();

			runOpCLIJVersion(ij);

			long timeOpCLIJEnd = System.currentTimeMillis();

			System.out.println("ImageJ2 CLIJ Op version took " + (timeOpCLIJEnd - timeOpCLIJStart) + " ms");
		}
	}

	private void runOpCLIJVersion(ImageJ ij) {
		CLIJ clij = CLIJ.getInstance();
		Img imginput = ij.op().create().img(new FinalDimensions(20,20,20), new FloatType());
		ClearCLBuffer input = clij.convert(imginput, ClearCLBuffer.class);
		ClearCLBuffer output = clij.create(input);

		float value = 5;
		ij.op().run(AddImageAndScalarCLIJ.class, output, input, value);
		input = output; output = clij.create(input);
		double sigma = 5;
		ij.op().run(BlurFastCLIJ.class, output, input, sigma, sigma, sigma);
		input = output; output = clij.create(input);
		int radius = 5;
		ij.op().run(MeanBoxCLIJ.class, output, input, radius, radius, radius);

		Img imgoutput = (Img) clij.convert(output, RandomAccessibleInterval.class);
	}

	private void runOpVersion(ImageJ ij) {
		Img input = ij.op().create().img(new FinalDimensions(20,20,20), new FloatType());
		Img output = input.copy();

		FloatType value = new FloatType(5);
		ij.op().math().add((IterableInterval)output, input, value);
		input = output; output = input.copy();
		double sigma = 5;
		ij.op().filter().gauss(output, input, sigma);
		input = output; output = input.copy();
		long radius = 5;
		ij.op().filter().mean(output, input, new HyperSphereShape(radius));
	}

}

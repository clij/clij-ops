
package net.haesleinhuepf.clij.ops.misc;

import org.junit.Test;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.ops.CLIJOpsTest;
import net.haesleinhuepf.clij.ops.CLIJ_addImageAndScalar.CLIJ_addImageAndScalar;
import net.haesleinhuepf.clij.ops.CLIJ_blur.CLIJ_blur;
import net.haesleinhuepf.clij.ops.CLIJ_meanBox.CLIJ_meanBox;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.HyperSphereShape;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;

public class PosterTest extends CLIJOpsTest {

	Img input = createAscendingImage();

	@Test
	public void benchmarkScale() {
		int runs = 3;
		for (int i = 0; i < runs; i++) {
			long timeOpStart = System.currentTimeMillis();
			runOpVersion(input);
			long timeOpEnd = System.currentTimeMillis();
			System.out.println("ImageJ2 Op version took " + (timeOpEnd -
				timeOpStart) + " ms");
		}
		for (int i = 0; i < runs; i++) {
			long timeOpCLIJStart = System.currentTimeMillis();
			runOpCLIJVersion(input);
			long timeOpCLIJEnd = System.currentTimeMillis();
			System.out.println("ImageJ2 CLIJ Op version took " + (timeOpCLIJEnd -
				timeOpCLIJStart) + " ms");
		}
	}

	@Test
	public void compareScaleResults() {
		Img outputOp = runOpVersion(input);
		Img outputCLIJ = runOpCLIJVersion(input);
		compare(outputOp, outputCLIJ);
	}

	private Img runOpCLIJVersion(Img _imginput) {
		CLIJ clij = CLIJ.getInstance();
		Img imginput = ij.op().convert().float32(_imginput);
		ClearCLBuffer input = clij.convert(imginput, ClearCLBuffer.class);
		ClearCLBuffer output = clij.create(input);

		float value = 5;
		ij.op().run(CLIJ_addImageAndScalar.class, output, input, value);
		input = output;
		output = clij.create(input);
		double sigma = 5;
		ij.op().run(CLIJ_blur.class, output, input, sigma, sigma, sigma);
		input = output;
		output = clij.create(input);
		int radius = 5;
		ij.op().run(CLIJ_meanBox.class, output, input, radius, radius, radius);
		return (Img) clij.convert(output, RandomAccessibleInterval.class);

	}

	private Img runOpVersion(Img imginput) {
		Img input = ij.op().convert().float32(imginput);
		Img output = input.copy();

		FloatType value = new FloatType(5);
		ij.op().math().add((IterableInterval) output, input, value);
		input = output;
		output = input.copy();
		double sigma = 5;
		ij.op().filter().gauss(output, input, sigma);
		input = output;
		output = input.copy();
		long radius = 5;
		ij.op().filter().mean(output, input, new HyperSphereShape(radius));
		return output;
	}

}

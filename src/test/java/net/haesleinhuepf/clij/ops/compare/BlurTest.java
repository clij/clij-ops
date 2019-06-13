
package net.haesleinhuepf.clij.ops.compare;

import org.junit.Test;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.ops.CLIJOpsTest;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;

/*
 * Compares op.filter().gauss() with CLIJ_blurFast / CLIJ_blur
 */
public class BlurTest extends CLIJOpsTest {

	float sigma = 5;
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
	public void compareResults() {
		Img outputOp = runOpVersion(input);
		Img outputCLIJ = runOpCLIJVersion(input);
		compare(outputOp, outputCLIJ);
	}

	private Img runOpCLIJVersion(Img _imginput) {
		Img imginput = ij.op().convert().float32(_imginput);
		ClearCLBuffer input = clij.convert(imginput, ClearCLBuffer.class);
		ClearCLBuffer output = clij.create(input);
		double sigma = 5;
		printDim("CLIJ version input", _imginput);
		printDim("CLIJ version buffer  input", input);
		ij.op().run("CLIJ_blur", output, input, sigma, sigma, sigma);
		Img _output = (Img) clij.convert(output, RandomAccessibleInterval.class);
		printDim("CLIJ version buffer output", output);
		printDim("CLIJ version output", _output);
		input.close();
		output.close();
		return _output;

	}

	private Img runOpVersion(Img imginput) {
		Img input = ij.op().convert().float32(imginput);
		Img output = input.copy();
		printDim("op version input", input);
		double sigma = 5;
		ij.op().filter().gauss(output, input, sigma);
		printDim("op version output", output);
		return output;
	}
}

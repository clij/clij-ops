package net.haesleinhuepf.clij.ops.reviewed.filter;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.ops.CLIJOpsTest;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import org.junit.Test;

/*
 * Compares op.filter().gauss() with CLIJ_blur
 */
public class GaussTest extends CLIJOpsTest {

	double sigma = 5;
	Img input = createAscendingImage();

	@Test
	public void benchmarkScale() {
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
	public void compareScaleResults() {
		Img outputOp = runOpVersion(input);
		Img outputCLIJ = runOpCLIJVersion(input);
		compare(outputOp, outputCLIJ);
	}

	private Img runOpCLIJVersion(Img _imginput) {
		Img imginput = ij.op().convert().float32(_imginput);
		ClearCLBuffer input = clij.convert(imginput, ClearCLBuffer.class);
		ClearCLBuffer output = clij.create(input);
		printDim("CLIJ version input", _imginput);
		printDim("CLIJ version buffer  input", input);
		ij.op().run(Ops.Filter.Gauss.class, output, input, sigma);
		Img _output = (Img) clij.convert(output, RandomAccessibleInterval.class);
		printDim("CLIJ version buffer output", output);
		printDim("CLIJ version output", _output);
		return _output;

	}

	private Img runOpVersion(Img imginput) {
		Img input = ij.op().convert().float32(imginput);
		Img output = input.copy();
		printDim("op version input", input);
		ij.op().filter().gauss(output, input, sigma);
		printDim("op version output", output);
		return output;
	}

}
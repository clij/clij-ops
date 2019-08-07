
package net.haesleinhuepf.clij.ops.math;

import net.haesleinhuepf.clij.ops.CLIJ_pull.CLIJ_pull;
import org.junit.Ignore;
import org.junit.Test;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.ops.CLIJOpsTest;
import net.haesleinhuepf.clij.ops.CLIJ_addImagesWeighted.CLIJ_addImagesWeighted;
import net.imglib2.IterableInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;

/*
 * Compares op.math().subtract() with CLIJ_addImagesWeighted
 */
public class SubtractTest extends CLIJOpsTest {

	Img input = createAscendingImage();

	@Test
	public void benchmarkSubtract() {
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
	@Ignore // TODO this test currently fails, find out why the result is different for CLIJ vs IJ2
	public void compareSubtractResults() {
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
		ij.op().run(CLIJ_addImagesWeighted.class, output, input, 1, -1);
		Img _output = (Img) ij.op().run(CLIJ_pull.class, output);
		printDim("CLIJ version buffer output", output);
		printDim("CLIJ version output", _output);
		input.close();
		output.close();
		return _output;

	}

	private Img runOpVersion(Img imginput) {
		Img input = ij.op().convert().float32(imginput);
		Img output = ij.op().create().img(input);
		FloatType value = new FloatType(5);
		printDim("op version input", input);
		ij.op().math().subtract((IterableInterval) output, input, value);
		printDim("op version output", output);
		return output;
	}
}

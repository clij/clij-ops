
package net.haesleinhuepf.clij.ops.math;

import org.junit.Test;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.ops.CLIJOpsTest;
import net.haesleinhuepf.clij.ops.CLIJ_addImageAndScalar.CLIJ_addImageAndScalar;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;

/*
 * Compares op.math().add() with CLIJ_addImageAndScalar
 */
public class AddTest extends CLIJOpsTest {

	float addValue = 5;
	Img input = createAscendingImage();

	@Test
	public void benchmarkAdd() {
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
	public void compareAddResults() {
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
		ij.op().run(CLIJ_addImageAndScalar.class, output, input, addValue);
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
		FloatType value = new FloatType(addValue);
		printDim("op version input", input);
		ij.op().math().add((IterableInterval) output, input, value);
		printDim("op version output", output);
		return output;
	}
}

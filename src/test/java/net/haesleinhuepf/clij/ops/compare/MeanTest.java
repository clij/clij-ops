
package net.haesleinhuepf.clij.ops.compare;

import org.junit.Ignore;
import org.junit.Test;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.ops.CLIJOpsTest;
import net.haesleinhuepf.clij.ops.CLIJ_meanBox.CLIJ_meanBox;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.HyperSphereShape;
import net.imglib2.img.Img;

/*
 * Compares op.filter().mean() with CLIJ_meanBox
 */
public class MeanTest extends CLIJOpsTest {

	float radius = 5;
	Img input = createAscendingImage();

	@Test
	public void benchmarkMean() {
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
	public void compareMeanResults() {
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
		ij.op().run(CLIJ_meanBox.class, output, input, radius, radius, radius);
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
		ij.op().filter().mean(output, input, new HyperSphereShape((long) radius));
		printDim("op version output", output);
		return output;
	}

}

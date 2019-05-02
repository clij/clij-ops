package net.haesleinhuepf.clij.ops.reviewed.transform;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.ops.CLIJOpsTest;
import net.haesleinhuepf.clij.ops.reviewed.transform.downsample.DownsampleCLIJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.ImgView;
import net.imglib2.interpolation.randomaccess.NLinearInterpolatorFactory;
import org.junit.Test;

/*
 * Compares op.transform().scaleView() with CLIJ_downsample3D
 */
public class ScaleTest extends CLIJOpsTest {

	double sampleX = 0.52;
	double sampleY = 0.52;
	double sampleZ = 2.0;
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
		compareDimensions(outputOp, outputCLIJ);
		compareValues(outputOp, outputCLIJ);
	}

	private Img runOpCLIJVersion(Img _imginput) {
		Img imginput = ij.op().convert().float32(_imginput);
		ClearCLBuffer input = clij.convert(imginput, ClearCLBuffer.class);
		printDim("CLIJ version input", _imginput);
		printDim("CLIJ version buffer  input", input);
		ClearCLBuffer output = (ClearCLBuffer) ij.op().run(DownsampleCLIJ.class, input, sampleX, sampleY, sampleZ);
		Img _output = (Img) clij.convert(output, RandomAccessibleInterval.class);
		printDim("CLIJ version buffer output", output);
		printDim("CLIJ version output", _output);
		return _output;

	}

	private Img runOpVersion(Img imginput) {
		Img input = ij.op().convert().float32(imginput);
		printDim("op version input", input);
		Img output = ImgView.wrap(ij.op().transform().scaleView(input, new double[]{sampleX, sampleY, sampleZ}, new NLinearInterpolatorFactory()), input.factory());
		printDim("op version output", output);
		return output;
	}
}

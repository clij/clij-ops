package net.haesleinhuepf.clij.ops.misc;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.ops.CLIJOpsTest;
import net.haesleinhuepf.clij.ops.CLIJ_create.CLIJ_create;
import net.haesleinhuepf.clij.ops.CLIJ_maxProjection.CLIJ_maximumZProjection;
import net.haesleinhuepf.clij.ops.CLIJ_pull.CLIJ_pull;
import net.haesleinhuepf.clij.ops.CLIJ_push.CLIJ_push;
import net.imagej.ImageJ;
import net.imglib2.img.Img;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class DocTest {
	@Test
	public void testDocumentation() throws IOException {
		ImageJ ij = new ImageJ();
		String path = getClass().getResource("/samples/t1-head.tif").getPath();
//		ij.get(CLIJService.class).get("Intel(R) HD Graphics Kabylake Desktop GT1.5");
		Img input = (Img) ij.io().open(path);
		Object inputGPU = ij.op().run(CLIJ_push.class, input);
		ClearCLBuffer buffer = (ClearCLBuffer) inputGPU;
		Object targetGPU = ij.op().run(CLIJ_create.class, inputGPU);
		ClearCLBuffer buffer2 = (ClearCLBuffer) targetGPU;
		System.out.println(Arrays.toString(buffer2.getDimensions()));
		Object target2GPU = ij.op().run(CLIJ_create.class, new long[] { buffer.getWidth(), buffer.getHeight() }, buffer.getNativeType());
		targetGPU = ij.op().run(CLIJ_maximumZProjection.class, inputGPU);
		ij.op().run(CLIJ_maximumZProjection.class, target2GPU, inputGPU);
		Object target = ij.op().run(CLIJ_pull.class, targetGPU);
		ij.ui().show(target);
		Object target2 = ij.op().run(CLIJ_pull.class, target2GPU);
		ij.ui().show(target2);
		CLIJOpsTest.compare((Img)target, (Img)target2);
	}

	public static void main(String... args) throws IOException {
		new DocTest().testDocumentation();
	}

}

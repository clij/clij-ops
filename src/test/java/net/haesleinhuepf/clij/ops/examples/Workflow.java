
package net.haesleinhuepf.clij.ops.examples;

import java.io.IOException;

import org.junit.Test;

import net.haesleinhuepf.clij.ops.CLIJ_addImagesWeighted.CLIJ_addImagesWeighted;
import net.haesleinhuepf.clij.ops.CLIJ_blur.CLIJ_blur;
import net.haesleinhuepf.clij.ops.CLIJ_close.CLIJ_close;
import net.haesleinhuepf.clij.ops.CLIJ_maxProjection.CLIJ_maximumZProjection;
import net.haesleinhuepf.clij.ops.CLIJ_maximumImageAndScalar.CLIJ_maximumImageAndScalar;
import net.haesleinhuepf.clij.ops.CLIJ_pull.CLIJ_pull;
import net.haesleinhuepf.clij.ops.CLIJ_push.CLIJ_push;
import net.haesleinhuepf.clij.ops.CLIJ_radialProjection.CLIJ_radialProjection;
import net.imagej.ImageJ;
import net.imglib2.img.Img;

public class Workflow {

	static double smallBlurSigmaInPixels = 2;
	static double blurSigmaInPixels = 6;
	static double sampleX = 0.52;
	static double sampleY = 0.52;
	static double sampleZ = 2.0;

	@Test
	public void run() throws IOException {
		ImageJ ij = new ImageJ();
		Img blobs = (Img) ij.io().open("https://bds.mpi-cbg.de/samples/blobs.png");
		ij.ui().show("input", blobs);
		Img imginput = ij.op().convert().float32(blobs);

		Object original = ij.op().run(CLIJ_push.class, imginput);

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
		Object reslicedFromTop = ij.op().run(
			net.haesleinhuepf.clij.ops.filter.reslice.CLIJ_resliceTop.class, scaled);
		Object radialResliced = ij.op().run(CLIJ_radialProjection.class,
			reslicedFromTop, 360, 1.0);
		Object reslicedFromLeft = ij.op().run(
			net.haesleinhuepf.clij.ops.filter.reslice.CLIJ_resliceLeft.class,
			radialResliced);
		Object maxProjected = ij.op().run(CLIJ_maximumZProjection.class,
			reslicedFromLeft);
		Object result = ij.op().run(CLIJ_pull.class, maxProjected);
		ij.ui().show("workflow result", result);

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
		ij.op().run(CLIJ_close.class);

	}

	public static void main(String... args) throws IOException {
		Workflow task = new Workflow();
		task.run();
	}
}

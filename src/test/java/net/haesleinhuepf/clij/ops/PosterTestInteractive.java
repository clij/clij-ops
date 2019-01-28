package net.haesleinhuepf.clij.ops;

import net.imagej.ImageJ;
import net.imglib2.img.Img;

import java.io.IOException;

import static net.haesleinhuepf.clij.ops.PosterTest.runOpCLIJVersion;
import static net.haesleinhuepf.clij.ops.PosterTest.runOpVersion;

public class PosterTestInteractive {

	public static void main(String ... args) throws IOException {
		ImageJ ij = new ImageJ();
		ij.ui().showUI();
		Img input = (Img) ij.io().open("/home/random/lena.tif");

		long timeOpStart = System.currentTimeMillis();

		ij.ui().show("CPU", runOpVersion(ij, input));

		long timeOpEnd = System.currentTimeMillis();

		System.out.println("ImageJ2 Op version took " + (timeOpEnd - timeOpStart) + " ms");

		long timeOpCLIJStart = System.currentTimeMillis();

		ij.ui().show("GPU", runOpCLIJVersion(ij, input));

		long timeOpCLIJEnd = System.currentTimeMillis();

		System.out.println("ImageJ2 CLIJ Op version took " + (timeOpCLIJEnd - timeOpCLIJStart) + " ms");
	}
}

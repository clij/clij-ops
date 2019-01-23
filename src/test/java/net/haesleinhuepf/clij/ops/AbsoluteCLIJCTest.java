package net.haesleinhuepf.clij.ops;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.imagej.ImageJ;
import net.imglib2.FinalDimensions;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;
import org.junit.Test;

public class AbsoluteCLIJCTest {

	@Test
	public void absoluteCLIJCTest () {
		ImageJ ij = new ImageJ();
		Img img1 = ij.op().create().img(new FinalDimensions(10, 10, 10), new FloatType());
		Img img2 = img1.copy();
		CLIJ clij = CLIJ.getInstance();
		ClearCLBuffer input = clij.convert(img1, ClearCLBuffer.class);
		ClearCLBuffer output = clij.convert(img2, ClearCLBuffer.class);
		ij.op().run(AbsoluteCLIJC.class, input, output);
	}

}


package net.haesleinhuepf.clij.ops;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.imagej.ImageJ;
import net.imglib2.*;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;

public abstract class CLIJOpsTest {

	protected ImageJ ij = new ImageJ();
	protected CLIJ clij = CLIJ.getInstance();

	protected Img createAscendingImage() {
		return createAscendingImage(new FinalDimensions(10, 20, 30));
	}

	protected Img createAscendingImage(Dimensions dims) {
		Img img = ij.op().create().img(dims, new FloatType());
		Cursor<FloatType> cursor = img.localizingCursor();
		int pix = 0;
		while (cursor.hasNext()) {
			cursor.next();
			cursor.get().set(++pix);
		}
		cursor.reset();
		return img;
	}

	protected static void compare(Img img1, Img img2) {
		compareDimensions(img1, img2);
		compareValues(img1, img2);
	}

	protected static void compareValues(Img img1, Img img2) {
		RandomAccess<FloatType> ra = img1.randomAccess();
		Cursor<FloatType> cursor = img2.cursor();
		cursor.reset();
		// printImg("img1", img1);
		// System.out.println();
		// printImg("img2", img2);
		while (cursor.hasNext()) {
			cursor.next();
			ra.setPosition(cursor);
			// System.out.println("img1: " + ra.get() + " img2: " + cursor.get());
			assertEquals(ra.get(), cursor.get());
		}
	}

	protected static void compareDimensions(Img img1, Img img2) {
		long[] img1Dims = new long[img1.numDimensions()];
		img1.dimensions(img1Dims);
		long[] img2Dims = new long[img2.numDimensions()];
		img2.dimensions(img2Dims);
		for (int i = 0; i < img1Dims.length; i++) {
			assertEquals(img1Dims[i], img2Dims[i]);
		}
	}

	protected static void printImg(String name, Img input) {
		Cursor cursor = input.cursor();
		while (cursor.hasNext()) {
			cursor.next();
			System.out.println(name + ": " + cursor.get());
		}
	}

	protected static void printDim(String name, RandomAccessibleInterval input) {
		long[] imgDims = new long[input.numDimensions()];
		input.dimensions(imgDims);
		System.out.println(name + ": " + Arrays.toString(imgDims));
	}

	protected static void printDim(String name, ClearCLBuffer input) {
		System.out.println(name + ": " + Arrays.toString(input.getDimensions()));
	}

}

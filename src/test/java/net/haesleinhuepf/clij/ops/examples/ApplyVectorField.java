
package net.haesleinhuepf.clij.ops.examples;

import java.io.IOException;

import org.junit.Test;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.ops.CLIJ_affineTransform.CLIJ_affineTransform;
import net.haesleinhuepf.clij.ops.CLIJ_applyVectorfield.CLIJ_applyVectorfield;
import net.haesleinhuepf.clij.ops.CLIJ_close.CLIJ_close;
import net.haesleinhuepf.clij.ops.CLIJ_copySlice.CLIJ_copySlice;
import net.haesleinhuepf.clij.ops.CLIJ_create.CLIJ_create;
import net.haesleinhuepf.clij.ops.CLIJ_pull.CLIJ_pull;
import net.haesleinhuepf.clij.ops.CLIJ_push.CLIJ_push;
import net.imagej.ImageJ;
import net.imglib2.*;
import net.imglib2.algorithm.region.hypersphere.HyperSphere;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;

public class ApplyVectorField {

	@Test
	public void run() throws IOException {
		ImageJ ij = new ImageJ();
		ij.ui().showUI();

		Img blobs = (Img) ij.io().open("https://bds.mpi-cbg.de/samples/blobs.png");
		ij.ui().show("input", blobs);

		Img blobs32 = ij.op().convert().float32(blobs);

		// create two images describing local shift
		Img<DoubleType> shiftX = ij.op().create().img(new long[] { 256, 254 });
		Img shiftX32 = ij.op().convert().float32(shiftX);
		Img shiftY = ij.op().create().img(new long[] { 256, 254 });
		Img shiftY32 = ij.op().convert().float32(shiftY);

		RandomAccess<DoubleType> ra = shiftX32.randomAccess();
		ra.setPosition(new long[] { 70, 98 });

		HyperSphere<FloatType> hyperSphere = new HyperSphere<>(shiftX32, ra, 30);
		for (FloatType value : hyperSphere)
			value.set(25);

		shiftX32 = (Img) ij.op().filter().gauss(shiftX32, 15.);

		// push images to GPU
		Object blobsGPU = ij.op().run(CLIJ_push.class, blobs32);
		Object shiftXGPU = ij.op().run(CLIJ_push.class, shiftX32);
		Object shiftYGPU = ij.op().run(CLIJ_push.class, shiftY32);
		Object resultStackGPU = ij.op().run(CLIJ_create.class, new long[] { 256,
			254, 36 }, ((ClearCLBuffer) blobsGPU).getNativeType());

		for (int i = 0; i < 36; i++) {

			// change the shift from slice to slice
			Object rotatedShiftX = ij.op().run(CLIJ_affineTransform.class, shiftXGPU,
				"center rotate=" + (i * 10) + " -center");

			// apply transform
			Object transformed = ij.op().run(CLIJ_applyVectorfield.class, blobsGPU,
				rotatedShiftX, shiftYGPU);

			// put resulting 2D image in the right plane
			ij.op().run(CLIJ_copySlice.class, resultStackGPU, transformed, i);

			ij.op().run(CLIJ_close.class, rotatedShiftX);
			ij.op().run(CLIJ_close.class, transformed);
		}

		// show result
		Object result = ij.op().run(CLIJ_pull.class, resultStackGPU);
		ij.ui().show("applied vector field", result);
		// cleanup
		ij.op().run(CLIJ_close.class, blobsGPU);
		ij.op().run(CLIJ_close.class, shiftXGPU);
		ij.op().run(CLIJ_close.class, shiftYGPU);
		ij.op().run(CLIJ_close.class, resultStackGPU);

	}

	public static void main(String... args) throws IOException {
		ApplyVectorField task = new ApplyVectorField();
		task.run();
	}
}

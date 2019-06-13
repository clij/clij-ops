
package net.haesleinhuepf.clij.ops.examples;

import java.io.IOException;

import org.junit.Test;
import org.scijava.table.DefaultGenericTable;
import org.scijava.table.GenericTable;

import net.haesleinhuepf.clij.ops.CLIJ_affineTransform.CLIJ_affineTransform;
import net.haesleinhuepf.clij.ops.CLIJ_centerOfMass.CLIJ_centerOfMass;
import net.haesleinhuepf.clij.ops.CLIJ_close.CLIJ_close;
import net.haesleinhuepf.clij.ops.CLIJ_copySlice.CLIJ_copySlice;
import net.haesleinhuepf.clij.ops.CLIJ_pull.CLIJ_pull;
import net.haesleinhuepf.clij.ops.CLIJ_push.CLIJ_push;
import net.haesleinhuepf.clij.ops.CLIJ_threshold.CLIJ_threshold;
import net.imagej.ImageJ;
import net.imglib2.img.Img;

public class MotionCorrection {

	@Test
	public void run() throws IOException {

		// define a threshold to differentiate object and background
		int threshold = 50;

		ImageJ ij = new ImageJ();

		Img input = (Img) ij.io().open(getClass().getResource(
			"/samples/motion_correction_Drosophila_DSmanila1.tif").getPath());
		ij.ui().show("input", input);

		Object input32 = ij.op().convert().float32(input);
		// push image to GPU
		Object inputGPU = ij.op().run(CLIJ_push.class, input32);

		double formerX = 0;
		double formerY = 0;

		GenericTable mass = new DefaultGenericTable();

		// process all slices; only the first stays where it is
		for (int z = 0; z < input.dimension(2); z++) {
			ij.log().info("z: " + z);

			Object slice = ij.op().run(CLIJ_copySlice.class, inputGPU, z);

			// determine center of mass
			Object binary = ij.op().run(CLIJ_threshold.class, slice, threshold);
			ij.op().run(CLIJ_centerOfMass.class, mass, binary);
			double x = (double) mass.get("MassX", mass.getRowCount() - 1);
			double y = (double) mass.get("MassY", mass.getRowCount() - 1);

			if (z > 0) {

				// determine shift
				double deltaX = x - formerX;
				double deltaY = y - formerY;

				// apply translation transformation
				Object shifted = ij.op().run(CLIJ_affineTransform.class, slice,
					"translatex=" + deltaX + " translatey=" + deltaY);

				// copy result back
				ij.op().run(CLIJ_copySlice.class, inputGPU, shifted, z);

				ij.op().run(CLIJ_close.class, shifted);
			}
			else {
				formerX = x;
				formerY = y;
			}

			ij.op().run(CLIJ_close.class, slice);
			ij.op().run(CLIJ_close.class, binary);
		}

		ij.ui().show("center of mass per slice", mass);

		// show result
		Object result = ij.op().run(CLIJ_pull.class, inputGPU);
		ij.ui().show("motion correction", result);

		// cleanup
		ij.op().run(CLIJ_close.class, inputGPU);


	}

	public static void main(String... args) throws IOException {
		MotionCorrection task = new MotionCorrection();
		task.run();
	}
}

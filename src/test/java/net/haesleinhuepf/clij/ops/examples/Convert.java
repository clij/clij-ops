
package net.haesleinhuepf.clij.ops.examples;

import java.io.IOException;

import org.junit.Test;

import net.haesleinhuepf.clij.ops.CLIJ_close.CLIJ_close;
import net.haesleinhuepf.clij.ops.CLIJ_convertFloat.CLIJ_convertFloat;
import net.haesleinhuepf.clij.ops.CLIJ_convertUInt16.CLIJ_convertUInt16;
import net.haesleinhuepf.clij.ops.CLIJ_pull.CLIJ_pull;
import net.haesleinhuepf.clij.ops.CLIJ_push.CLIJ_push;
import net.imagej.ImageJ;

public class Convert {

	@Test
	public void run() throws IOException {
		ImageJ ij = new ImageJ();
		Object blobs = ij.io().open("https://bds.mpi-cbg.de/samples/blobs.png");
		ij.ui().show("input", blobs);
		// push images to GPU
		Object blobsGPU = ij.op().run(CLIJ_push.class, blobs);

		Object blobsFloat = ij.op().run(CLIJ_convertFloat.class, blobsGPU);
		Object blobsUInt8 = ij.op().run(CLIJ_convertUInt16.class, blobsFloat);
		Object blobsUInt16 = ij.op().run(CLIJ_convertUInt16.class, blobsUInt8);

		// show result
		Object result = ij.op().run(CLIJ_pull.class, blobsUInt16);
		ij.ui().show("UInt16", result);

		// cleanup
		ij.op().run(CLIJ_close.class, blobsGPU);
		ij.op().run(CLIJ_close.class, blobsFloat);
		ij.op().run(CLIJ_close.class, blobsUInt8);
		ij.op().run(CLIJ_close.class, blobsUInt16);
		ij.op().run(CLIJ_close.class);

	}

	public static void main(String... args) throws IOException {
		Convert task = new Convert();
		task.run();
	}
}


package net.haesleinhuepf.clij.ops.examples;

import java.io.IOException;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import org.junit.Test;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.ops.CLIJ_affineTransform.CLIJ_affineTransform;
import net.haesleinhuepf.clij.ops.CLIJ_close.CLIJ_close;
import net.haesleinhuepf.clij.ops.CLIJ_pull.CLIJ_pull;
import net.haesleinhuepf.clij.ops.CLIJ_push.CLIJ_push;
import net.imagej.ImageJ;
import net.imglib2.img.Img;

public class AffineTransform {

	ImageJ ij = new ImageJ();

	@Test
	public void run() throws IOException {
		Img blobs = (Img) ij.io().open("https://bds.mpi-cbg.de/samples/blobs.png");
		ij.ui().show("input", blobs);

		Img blobs32 = ij.op().convert().float32(blobs);

		// to use a specific GPU version, uncomment the following line and insert
		// your GPU name
		// ij.get(CLIJService.class).get("Intel(R) HD Graphics Kabylake Desktop
		// GT1.5");

		Object blobsGPU = ij.op().run(CLIJ_push.class, blobs32);

		String transform = "center ";
		transform = transform + " rotate=45"; // degrees
		transform = transform + " scaleX=2"; // relative zoom factor
		transform = transform + " translateY=25"; // pixels
		transform = transform + " -center";

		Object target = ij.op().run(CLIJ_affineTransform.class, blobsGPU,
			transform);

		Object result = ij.op().run(CLIJ_pull.class, target);
		// show result
		ij.ui().show("affine transform", result);
		// cleanup
		ij.op().run(CLIJ_close.class, blobsGPU);
		ij.op().run(CLIJ_close.class, target);
		ij.op().run(CLIJ_close.class);

	}

	public static void main(String... args) throws IOException {
		AffineTransform task = new AffineTransform();
		task.run();
		CLIJ clij = CLIJ.getInstance();
	}
}

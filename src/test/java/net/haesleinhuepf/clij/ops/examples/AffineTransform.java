package net.haesleinhuepf.clij.ops.examples;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.imagej.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;

import java.io.IOException;

public class AffineTransform {

	public void run() throws IOException {
		ImageJ ij = new ImageJ();
		Img blobs = (Img) ij.io().open("https://imagej.nih.gov/ij/images/blobs.gif");
		Img blobs32 = ij.op().convert().float32(blobs);
		// init GPU
		CLIJ clij = CLIJ.getInstance();
		// push images to GPU
		ClearCLBuffer blobsGPU = clij.convert(blobs32, ClearCLBuffer.class);
		String transform = "center ";
		transform = transform + " rotate=45"; // degrees
				transform = transform + " scaleX=2"; // relative zoom factor
				transform = transform + " translateY=25"; // pixels
				transform = transform + " -center";

		ClearCLBuffer target = (ClearCLBuffer) ij.op().run("affineTransformCLIJ", blobsGPU, transform);

		// show result
		RandomAccessibleInterval result = clij.convert(target, RandomAccessibleInterval.class);
		// TODO how to invert look up table? is this neccessary?
		ij.ui().show(result);
	}

	public static void main(String ... args) throws IOException {
		AffineTransform task = new AffineTransform();
		task.run();
	}
}

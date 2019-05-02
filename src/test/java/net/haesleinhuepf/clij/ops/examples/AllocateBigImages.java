package net.haesleinhuepf.clij.ops.examples;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.imagej.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.view.Views;

import java.io.IOException;

public class AllocateBigImages {
	public void run() throws IOException {
		ImageJ ij = new ImageJ();
		Img input = (Img) ij.io().open("https://imagej.nih.gov/ij/images/blobs.gif");
		RandomAccessibleInterval singleChannelInput = ij.op().transform().hyperSliceView(input, 2, 0);
		// init GPU
		CLIJ clij = CLIJ.getInstance("Intel(R) HD Graphics Kabylake Desktop GT1.5");
		// push images to GPU
		ClearCLBuffer inputGPU = clij.push(singleChannelInput);

		// create an 800 MB image in GPU memory
		ClearCLBuffer bigStack = clij.create(new long[]{2048, 2048, 100}, NativeTypeEnum.UnsignedShort);

		for(int i = 0; i < 10; i++) {
			//fill the image with content
			//TODO WHY is input and output the other way around than in Macro script
			ij.op().run("copySliceCLIJ", bigStack, inputGPU, i);
		}

		ClearCLBuffer crop = (ClearCLBuffer) ij.op().run("cropCLIJ", bigStack, 0, 0, 0, 150, 150, 10);

		// Get results back from GPU
		RandomAccessibleInterval result = clij.convert(crop, RandomAccessibleInterval.class);

		// report about what's allocated in the GPU memory
//		clij.reportMemory();

		// Cleanup by the end
		clij.close();
		ij.ui().show(result);
	}

	public static void main(String ... args) throws IOException {
		AllocateBigImages task = new AllocateBigImages();
		task.run();
	}

}

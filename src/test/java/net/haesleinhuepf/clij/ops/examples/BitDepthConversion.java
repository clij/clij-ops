package net.haesleinhuepf.clij.ops.examples;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.ops.reviewed.convertUInt16CLIJ.ConvertUInt16CLIJ;
import net.haesleinhuepf.clij.ops.reviewed.dilateBoxCLIJ.DilateBoxCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.erodeBoxCLIJ.ErodeBoxCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.multiplyImageAndScalarCLIJ.MultiplyImageAndScalarCLIJ;
import net.haesleinhuepf.clij.ops.reviewed.thresholdCLIJ.ThresholdCLIJ;
import net.imagej.ImageJ;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.loops.LoopBuilder;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;

import java.io.IOException;

public class BitDepthConversion {

	public void run() {
		ImageJ ij = new ImageJ();
		//note that we cannot use DoubleType Images since they cannot be converted to ClearCLBuffer
		Img<DoubleType> image64 = ij.op().create().img(new long[]{20, 20});
		Img<FloatType> image32 = ij.op().convert().float32(image64);
		//TODO is this a ramp?
		LoopBuilder.setImages(image32).forEachPixel(pixel -> pixel.set(pixel.getIndex()));
		// init GPU
		CLIJ clij = CLIJ.getInstance();
		// push image to GPU
		ClearCLBuffer inputGPU = clij.push(image32);

		float intensityScalingFactor = 255;
		Object temp = ij.op().run(MultiplyImageAndScalarCLIJ.class, inputGPU, intensityScalingFactor);
		Object image16 = ij.op().run(ConvertUInt16CLIJ.class, temp);
		// show result
		RandomAccessibleInterval result = clij.convert(image16, RandomAccessibleInterval.class);
		ij.ui().show(result);
		clij.close();

	}

	public static void main(String ... args) throws IOException {
		BitDepthConversion task = new BitDepthConversion();
		task.run();
	}
}

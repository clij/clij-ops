
package net.haesleinhuepf.clij.ops.transform.downsample;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.haesleinhuepf.clij.CLIJService;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;

/**
 * @author Deborah Schmidt
 */

@Plugin(type = CLIJ_downsample.class)
public class Downsample3DHCFClearCLBufferClearCLBuffer extends
	AbstractUnaryHybridCF<ClearCLBuffer, ClearCLBuffer> implements
	CLIJ_downsample, Contingent
{

	@Parameter
	private Float factorX;

	@Parameter
	private Float factorY;

	@Parameter
	private Float factorZ;

	@Parameter
	CLIJService clij;

	@Override
	public void compute(final ClearCLBuffer input, final ClearCLBuffer output) {
		Kernels.downsample(clij.get(), input, output, factorX, factorY, factorZ);
	}

	@Override
	public boolean conforms() {
		return true;
	}

	@Override
	public ClearCLBuffer createOutput(ClearCLBuffer input) {
		long[] dims = new long[input.getDimensions().length];
		for (int i = 0; i < dims.length; i++) {
			dims[i] = input.getDimensions()[i];
		}
		if (dims.length > 0) dims[0] *= factorX;
		if (dims.length > 1) dims[1] *= factorY;
		if (dims.length > 2) dims[2] *= factorZ;
		ClearCLBuffer output = clij.get().createCLBuffer(dims, input
			.getNativeType());
		return output;
	}

}

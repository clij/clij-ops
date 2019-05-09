
package net.haesleinhuepf.clij.ops.transform.downsample;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.haesleinhuepf.clij.CLIJService;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;

/**
 * @author Deborah Schmidt
 */

@Plugin(type = CLIJ_downsample.class)
public class Downsample2DHCFClearCLImageClearCLImage extends
	AbstractUnaryHybridCF<ClearCLImage, ClearCLImage> implements CLIJ_downsample,
	Contingent
{

	@Parameter
	private Float factorX;

	@Parameter
	private Float factorY;

	@Parameter
	CLIJService clij;

	@Override
	public void compute(final ClearCLImage input, final ClearCLImage output) {

		Kernels.downsample(clij.get(), input, output, factorX, factorY);
	}

	@Override
	public boolean conforms() {
		return true;
	}

	@Override
	public ClearCLImage createOutput(ClearCLImage input) {
		long[] dims = input.getDimensions();
		if (dims.length > 0) dims[0] *= factorX;
		if (dims.length > 1) dims[1] *= factorY;
		return clij.get().createCLImage(dims, null);
	}
}


package net.haesleinhuepf.clij.ops.CLIJ_argMaximumZProjection;

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

@Plugin(type = CLIJ_argMaximumZProjection.class)
public class ArgMaximumZProjectionHCFClearCLImageClearCLImage extends
	AbstractUnaryHybridCF<ClearCLImage, ClearCLImage> implements
	CLIJ_argMaximumZProjection, Contingent
{

	@Parameter
	private ClearCLImage dst_arg;

	@Parameter
	CLIJService clij;

	@Override
	public void compute(final ClearCLImage input, final ClearCLImage output) {

		Kernels.argMaximumZProjection(clij.get(), input, output, dst_arg);
	}

	@Override
	public boolean conforms() {
		return true;
	}

	@Override
	public ClearCLImage createOutput(ClearCLImage input) {
		return clij.get().createCLImage(new long[] { input.getWidth(), input
			.getHeight() }, input.getChannelDataType());
	}

}

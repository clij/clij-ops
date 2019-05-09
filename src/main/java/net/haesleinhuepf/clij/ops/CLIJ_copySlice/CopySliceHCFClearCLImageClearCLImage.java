
package net.haesleinhuepf.clij.ops.CLIJ_copySlice;

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

@Plugin(type = CLIJ_copySlice.class)
public class CopySliceHCFClearCLImageClearCLImage extends
	AbstractUnaryHybridCF<ClearCLImage, ClearCLImage> implements CLIJ_copySlice,
	Contingent
{

	@Parameter
	private Integer planeIndex;

	@Parameter
	CLIJService clij;

	@Override
	public void compute(final ClearCLImage input, final ClearCLImage output) {

		Kernels.copySlice(clij.get(), input, output, planeIndex);
	}

	@Override
	public boolean conforms() {
		return true;
	}

	@Override
	public ClearCLImage createOutput(ClearCLImage input) {
		if (input.getDimension() == 2) {
			return clij.get().createCLImage(new long[] { input.getWidth(), input
				.getHeight(), planeIndex }, input.getChannelDataType());
		}
		else {
			return clij.get().createCLImage(new long[] { input.getWidth(), input
				.getHeight() }, input.getChannelDataType());
		}
	}

}

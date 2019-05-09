
package net.haesleinhuepf.clij.ops.CLIJ_copySlice;

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

@Plugin(type = CLIJ_copySlice.class)
public class CopySliceHCFClearCLBufferClearCLBuffer extends
	AbstractUnaryHybridCF<ClearCLBuffer, ClearCLBuffer> implements CLIJ_copySlice,
	Contingent
{

	@Parameter
	private Integer planeIndex;

	@Parameter
	CLIJService clij;

	@Override
	public void compute(final ClearCLBuffer input, final ClearCLBuffer output) {
		Kernels.copySlice(clij.get(), input, output, planeIndex);
	}

	@Override
	public boolean conforms() {
		return true;
	}

	@Override
	public ClearCLBuffer createOutput(ClearCLBuffer input) {
		if (input.getDimension() == 2) {
			return clij.get().createCLBuffer(new long[] { input.getWidth(), input
				.getHeight(), planeIndex + 1 }, input.getNativeType());
		}
		else {
			return clij.get().createCLBuffer(new long[] { input.getWidth(), input
				.getHeight() }, input.getNativeType());
		}
	}

}

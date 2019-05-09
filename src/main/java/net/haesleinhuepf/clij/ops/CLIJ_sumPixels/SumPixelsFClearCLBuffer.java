
package net.haesleinhuepf.clij.ops.CLIJ_sumPixels;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.haesleinhuepf.clij.CLIJService;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;

/**
 * @author Deborah Schmidt
 */

@Plugin(type = CLIJ_sumPixels.class)
public class SumPixelsFClearCLBuffer extends
	AbstractUnaryFunctionOp<ClearCLBuffer, Double> implements CLIJ_sumPixels,
	Contingent
{

	@Parameter
	CLIJService clij;

	@Override
	public Double calculate(final ClearCLBuffer input) {

		return Kernels.sumPixels(clij.get(), input);
	}

	@Override
	public boolean conforms() {
		return true;
	}

}

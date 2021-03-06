
package net.haesleinhuepf.clij.ops.CLIJ_pull;

import net.haesleinhuepf.clij.CLIJService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.RandomAccessibleInterval;

/**
 * @author Deborah Schmidt
 */

@Plugin(type = CLIJ_pull.class)
public class PullFRandomAccessibleIntervalClearCLBuffer extends
	AbstractUnaryFunctionOp<ClearCLBuffer, RandomAccessibleInterval> implements
	CLIJ_pull, Contingent
{

	@Parameter
	CLIJService clij;

	@Override
	public boolean conforms() {
		return true;
	}

	@Override
	public RandomAccessibleInterval calculate(ClearCLBuffer input) {
		return clij.get().pullRAI(input);
	}
}

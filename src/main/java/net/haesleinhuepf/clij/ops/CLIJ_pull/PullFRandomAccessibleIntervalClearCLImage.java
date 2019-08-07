
package net.haesleinhuepf.clij.ops.CLIJ_pull;

import net.haesleinhuepf.clij.CLIJService;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.RandomAccessibleInterval;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * @author Deborah Schmidt
 */

@Plugin(type = CLIJ_pull.class)
public class PullFRandomAccessibleIntervalClearCLImage extends
	AbstractUnaryFunctionOp<ClearCLImage, RandomAccessibleInterval> implements
	CLIJ_pull, Contingent
{

	@Parameter
	CLIJService clij;

	@Override
	public boolean conforms() {
		return true;
	}

	@Override
	public RandomAccessibleInterval calculate(ClearCLImage input) {
		return clij.get().convert(input, RandomAccessibleInterval.class);
	}
}

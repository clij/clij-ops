
package net.haesleinhuepf.clij.ops.CLIJ_pull;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.RandomAccessibleInterval;
import org.scijava.plugin.Plugin;

/**
 * @author Deborah Schmidt
 */

@Plugin(type = CLIJ_pull.class)
public class PullFRandomAccessibleIntervalClearCLImage extends
	AbstractUnaryFunctionOp<ClearCLImage, RandomAccessibleInterval> implements
	CLIJ_pull, Contingent
{

	@Override
	public boolean conforms() {
		return true;
	}

	@Override
	public RandomAccessibleInterval calculate(ClearCLImage input) {
		CLIJ clij = CLIJ.getInstance();
		return clij.convert(input, RandomAccessibleInterval.class);
	}
}

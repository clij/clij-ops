
package net.haesleinhuepf.clij.ops.CLIJ_push;

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

@Plugin(type = CLIJ_push.class)
public class PushFRandomAccessibleIntervalClearCLBuffer extends
	AbstractUnaryFunctionOp<RandomAccessibleInterval, ClearCLBuffer> implements
	CLIJ_push, Contingent
{

	@Parameter
	CLIJService clij;

	@Override
	public boolean conforms() {
		return true;
	}

	@Override
	public ClearCLBuffer calculate(RandomAccessibleInterval input) {
		return clij.get().push(input);
	}
}

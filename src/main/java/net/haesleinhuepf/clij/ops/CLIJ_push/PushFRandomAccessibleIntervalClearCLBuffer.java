
package net.haesleinhuepf.clij.ops.CLIJ_push;

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

	@Override
	public boolean conforms() {
		return true;
	}

	@Override
	public ClearCLBuffer calculate(RandomAccessibleInterval input) {
		CLIJ clij = CLIJ.getInstance();
		return clij.push(input);
	}
}

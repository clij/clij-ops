
package net.haesleinhuepf.clij.ops.CLIJ_close;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.haesleinhuepf.clij.CLIJService;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;

/**
 * @author Deborah Schmidt
 */

@Plugin(type = CLIJ_close.class)
public class CloseFClearCLBuffer extends
	AbstractUnaryFunctionOp<ClearCLBuffer, ClearCLBuffer> implements CLIJ_close
{

	@Parameter
	CLIJService clij;

	@Override
	public ClearCLBuffer calculate(ClearCLBuffer input) {
		input.close();
		return null;
	}
}

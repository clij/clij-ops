
package net.haesleinhuepf.clij.ops.CLIJ_create;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.CLIJService;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.function.AbstractNullaryFunctionOp;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * @author Deborah Schmidt
 */

@Plugin(type = CLIJ_create.class)
public class CreateCClearCLBufferClearCLBuffer extends
		AbstractUnaryFunctionOp<ClearCLBuffer, ClearCLBuffer> implements CLIJ_create, Contingent
{

	@Override
	public boolean conforms() {
		return true;
	}

	@Parameter
	private CLIJService clij;

	@Override
	public ClearCLBuffer calculate(ClearCLBuffer input) {
		return clij.get().create(input);
	}
}

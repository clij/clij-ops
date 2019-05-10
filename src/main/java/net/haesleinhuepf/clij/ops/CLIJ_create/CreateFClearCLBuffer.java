
package net.haesleinhuepf.clij.ops.CLIJ_create;

import net.haesleinhuepf.clij.CLIJService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.function.AbstractNullaryFunctionOp;

/**
 * @author Deborah Schmidt
 */

@Plugin(type = CLIJ_create.class)
public class CreateFClearCLBuffer extends
	AbstractNullaryFunctionOp<ClearCLBuffer> implements CLIJ_create, Contingent
{

	@Parameter
	private long[] dimensions;

	@Parameter
	private NativeTypeEnum type;

	@Parameter
	private CLIJService clij;

	@Override
	public boolean conforms() {
		return true;
	}

	@Override
	public ClearCLBuffer calculate() {
		return clij.get().create(dimensions, type);
	}
}

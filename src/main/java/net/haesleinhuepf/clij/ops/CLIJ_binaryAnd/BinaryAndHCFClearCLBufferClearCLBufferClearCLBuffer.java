
package net.haesleinhuepf.clij.ops.CLIJ_binaryAnd;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.haesleinhuepf.clij.CLIJService;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.hybrid.AbstractBinaryHybridCF;

/**
 * @author Deborah Schmidt
 */

@Plugin(type = CLIJ_binaryAnd.class)
public class BinaryAndHCFClearCLBufferClearCLBufferClearCLBuffer extends
	AbstractBinaryHybridCF<ClearCLBuffer, ClearCLBuffer, ClearCLBuffer> implements
	CLIJ_binaryAnd, Contingent
{

	@Parameter
	CLIJService clij;

	@Override
	public void compute(final ClearCLBuffer input1, final ClearCLBuffer input2,
		final ClearCLBuffer output)
	{

		Kernels.binaryAnd(clij.get(), input1, input2, output);
	}

	@Override
	public boolean conforms() {
		return true;
	}

	@Override
	public ClearCLBuffer createOutput(ClearCLBuffer input1,
		ClearCLBuffer input2)
	{
		return clij.get().create(input1);
	}

}

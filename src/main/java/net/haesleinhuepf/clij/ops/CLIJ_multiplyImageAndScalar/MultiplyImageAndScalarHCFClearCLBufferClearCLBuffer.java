
package net.haesleinhuepf.clij.ops.CLIJ_multiplyImageAndScalar;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.CLIJService;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;

/**
 * @author Deborah Schmidt
 */

@Plugin(type = CLIJ_multiplyImageAndScalar.class)
public class MultiplyImageAndScalarHCFClearCLBufferClearCLBuffer extends
	AbstractUnaryHybridCF<ClearCLBuffer, ClearCLBuffer> implements
	CLIJ_multiplyImageAndScalar, Contingent
{

	@Parameter
	private Float scalar;

	@Parameter
	CLIJService clij;

	@Override
	public void compute(final ClearCLBuffer input, final ClearCLBuffer output) {

		Kernels.multiplyImageAndScalar(clij.get(), input, output, scalar);
	}

	@Override
	public boolean conforms() {
		return true;
	}

	@Override
	public ClearCLBuffer createOutput(ClearCLBuffer input) {
		CLIJ clij = CLIJ.getInstance();
		return clij.create(input);
	}

}

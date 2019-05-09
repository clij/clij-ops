
package net.haesleinhuepf.clij.ops.CLIJ_multiplyImageAndCoordinate;

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

@Plugin(type = CLIJ_multiplyImageAndCoordinate.class)
public class MultiplyImageAndCoordinateHCFClearCLBufferClearCLBuffer extends
	AbstractUnaryHybridCF<ClearCLBuffer, ClearCLBuffer> implements
	CLIJ_multiplyImageAndCoordinate, Contingent
{

	@Parameter
	private Integer dimension;

	@Parameter
	CLIJService clij;

	@Override
	public void compute(final ClearCLBuffer input, final ClearCLBuffer output) {

		Kernels.multiplyImageAndCoordinate(clij.get(), input, output, dimension);
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

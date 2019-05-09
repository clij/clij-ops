
package net.haesleinhuepf.clij.ops.CLIJ_erodeBox;

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

@Plugin(type = CLIJ_erodeBox.class)
public class ErodeBoxHCFClearCLBufferClearCLBuffer extends
	AbstractUnaryHybridCF<ClearCLBuffer, ClearCLBuffer> implements CLIJ_erodeBox,
	Contingent
{

	@Parameter
	CLIJService clij;

	@Override
	public void compute(final ClearCLBuffer input, final ClearCLBuffer output) {

		Kernels.erodeBox(clij.get(), input, output);
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

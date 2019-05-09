
package net.haesleinhuepf.clij.ops.CLIJ_copy;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.haesleinhuepf.clij.CLIJService;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;

/**
 * @author Deborah Schmidt
 */

@Plugin(type = CLIJ_copy.class)
public class CopyCClearCLImageClearCLBuffer extends
	AbstractUnaryComputerOp<ClearCLImage, ClearCLBuffer> implements CLIJ_copy,
	Contingent
{

	@Parameter
	CLIJService clij;

	@Override
	public void compute(final ClearCLImage input, final ClearCLBuffer output) {

		Kernels.copy(clij.get(), input, output);
	}

	@Override
	public boolean conforms() {
		return true;
	}

}

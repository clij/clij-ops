
package net.haesleinhuepf.clij.ops.CLIJ_applyVectorfield;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.CLIJService;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;

/**
 * @author Deborah Schmidt
 */

@Plugin(type = CLIJ_applyVectorfield.class)
public class ApplyVectorfield3DHCFClearCLImageClearCLImage extends
	AbstractUnaryHybridCF<ClearCLImage, ClearCLImage> implements
	CLIJ_applyVectorfield, Contingent
{

	@Parameter
	private ClearCLImage vectorX;

	@Parameter
	private ClearCLImage vectorY;

	@Parameter
	private ClearCLImage vectorZ;

	@Parameter
	CLIJService clij;

	@Override
	public void compute(final ClearCLImage input, final ClearCLImage output) {

		Kernels.applyVectorfield(clij.get(), input, vectorX, vectorY, vectorZ,
			output);
	}

	@Override
	public boolean conforms() {
		return true;
	}

	@Override
	public ClearCLImage createOutput(ClearCLImage input) {
		CLIJ clij = CLIJ.getInstance();
		return clij.create(input);
	}

}

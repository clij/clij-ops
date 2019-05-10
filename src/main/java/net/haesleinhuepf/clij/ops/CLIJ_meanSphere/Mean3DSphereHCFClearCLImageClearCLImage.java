
package net.haesleinhuepf.clij.ops.CLIJ_meanSphere;

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

@Plugin(type = CLIJ_meanSphere.class)
public class Mean3DSphereHCFClearCLImageClearCLImage extends
	AbstractUnaryHybridCF<ClearCLImage, ClearCLImage> implements CLIJ_meanSphere,
	Contingent
{

	@Parameter
	private Integer kernelSizeX;

	@Parameter
	private Integer kernelSizeY;

	@Parameter
	private Integer kernelSizeZ;

	@Parameter
	CLIJService clij;

	@Override
	public void compute(final ClearCLImage input, final ClearCLImage output) {

		Kernels.meanSphere(clij.get(), input, output, kernelSizeX, kernelSizeY,
			kernelSizeZ);
	}

	@Override
	public boolean conforms() {
		return true;
	}

	@Override
	public ClearCLImage createOutput(ClearCLImage input) {
		return clij.get().create(input);
	}

}

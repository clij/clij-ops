
package net.haesleinhuepf.clij.ops.CLIJ_medianSphere;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.radiusToKernelSize;

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

@Plugin(type = CLIJ_medianSphere.class)
public class Median3DSphereHCFClearCLBufferClearCLBuffer extends
	AbstractUnaryHybridCF<ClearCLBuffer, ClearCLBuffer> implements
	CLIJ_medianSphere, Contingent
{

	@Parameter
	private Integer radiusX;

	@Parameter
	private Integer radiusY;

	@Parameter
	private Integer radiusZ;

	@Parameter
	CLIJService clij;

	@Override
	public void compute(final ClearCLBuffer input, final ClearCLBuffer output) {

		int kernelSizeX = radiusToKernelSize(radiusX);
		int kernelSizeY = radiusToKernelSize(radiusY);
		int kernelSizeZ = radiusToKernelSize(radiusZ);

		Kernels.medianSphere(clij.get(), input, output, kernelSizeX, kernelSizeY,
			kernelSizeZ);
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


package net.haesleinhuepf.clij.ops.CLIJ_meanSliceBySliceSphere;

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

@Plugin(type = CLIJ_meanSliceBySliceSphere.class)
public class MeanSliceBySliceSphereHCFClearCLBufferClearCLBuffer extends
	AbstractUnaryHybridCF<ClearCLBuffer, ClearCLBuffer> implements
	CLIJ_meanSliceBySliceSphere, Contingent
{

	@Parameter
	private Integer radiusX;

	@Parameter
	private Integer radiusY;

	@Parameter
	CLIJService clij;

	@Override
	public void compute(final ClearCLBuffer input, final ClearCLBuffer output) {

		int kernelSizeX = radiusToKernelSize(radiusX);
		int kernelSizeY = radiusToKernelSize(radiusY);

		Kernels.meanSliceBySliceSphere(clij.get(), input, output, kernelSizeX,
			kernelSizeY);
	}

	@Override
	public boolean conforms() {
		return true;
	}

	@Override
	public ClearCLBuffer createOutput(ClearCLBuffer input) {
		return clij.get().create(input);
	}

}

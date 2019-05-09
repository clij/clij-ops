
package net.haesleinhuepf.clij.ops.CLIJ_maximumXYZProjection;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.haesleinhuepf.clij.CLIJService;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;

/**
 * @author Deborah Schmidt
 */

@Plugin(type = CLIJ_maximumXYZProjection.class)
public class MaximumXYZProjectionHCFClearCLImageClearCLImage extends
	AbstractUnaryHybridCF<ClearCLImage, ClearCLImage> implements
	CLIJ_maximumXYZProjection, Contingent
{

	@Parameter
	private Integer projectedDimensionX;

	@Parameter
	private Integer projectedDimensionY;

	@Parameter
	private Integer projectedDimension;

	@Parameter
	CLIJService clij;

	@Override
	public void compute(final ClearCLImage input, final ClearCLImage output) {

		Kernels.maximumXYZProjection(clij.get(), input, output, projectedDimensionX,
			projectedDimensionY, projectedDimension);
	}

	@Override
	public boolean conforms() {
		return true;
	}

	@Override
	public ClearCLImage createOutput(ClearCLImage input) {
		return clij.get().createCLImage(new long[] { input.getWidth(), input
			.getHeight() }, input.getChannelDataType());
	}

}

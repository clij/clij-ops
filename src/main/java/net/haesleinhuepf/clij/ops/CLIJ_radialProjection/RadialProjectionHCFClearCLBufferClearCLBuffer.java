
package net.haesleinhuepf.clij.ops.CLIJ_radialProjection;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.haesleinhuepf.clij.CLIJService;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;

/**
 * @author Deborah Schmidt
 */

@Plugin(type = CLIJ_radialProjection.class)
public class RadialProjectionHCFClearCLBufferClearCLBuffer extends
	AbstractUnaryHybridCF<ClearCLBuffer, ClearCLBuffer> implements
	CLIJ_radialProjection, Contingent
{

	@Parameter
	private Integer numberOfAngles;
	@Parameter
	private Float angleStepSize;

	@Parameter
	CLIJService clij;

	@Override
	public void compute(final ClearCLBuffer input, final ClearCLBuffer output) {

		Kernels.radialProjection(clij.get(), input, output, angleStepSize);
	}

	@Override
	public boolean conforms() {
		return true;
	}

	@Override
	public ClearCLBuffer createOutput(ClearCLBuffer input) {
		int effectiveNumberOfAngles = (int) ((float) numberOfAngles /
			angleStepSize);
		int maximumRadius = (int) Math.sqrt(Math.pow(input.getWidth() / 2, 2) + Math
			.pow(input.getHeight() / 2, 2));
		return clij.get().createCLBuffer(new long[] { maximumRadius, input
			.getDepth(), effectiveNumberOfAngles }, input.getNativeType());
	}
}

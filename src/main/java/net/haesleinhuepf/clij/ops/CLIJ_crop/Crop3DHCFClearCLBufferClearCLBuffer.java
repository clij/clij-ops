
package net.haesleinhuepf.clij.ops.CLIJ_crop;

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

@Plugin(type = CLIJ_crop.class)
public class Crop3DHCFClearCLBufferClearCLBuffer extends
	AbstractUnaryHybridCF<ClearCLBuffer, ClearCLBuffer> implements CLIJ_crop,
	Contingent
{

	@Parameter
	private Integer startX;

	@Parameter
	private Integer startY;

	@Parameter
	private Integer startZ;

	@Parameter
	private Integer width;

	@Parameter
	private Integer height;

	@Parameter
	private Integer depth;

	@Parameter
	CLIJService clij;

	@Override
	public void compute(final ClearCLBuffer input, final ClearCLBuffer output) {
		Kernels.crop(clij.get(), input, output, startX, startY, startZ);
	}

	@Override
	public boolean conforms() {
		return true;
	}

	@Override
	public ClearCLBuffer createOutput(ClearCLBuffer input) {
		return clij.get().createCLBuffer(new long[] { width, height, depth }, input
			.getNativeType());
	}

}

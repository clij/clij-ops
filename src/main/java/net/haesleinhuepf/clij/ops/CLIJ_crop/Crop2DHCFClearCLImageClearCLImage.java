
package net.haesleinhuepf.clij.ops.CLIJ_crop;

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

@Plugin(type = CLIJ_crop.class)
public class Crop2DHCFClearCLImageClearCLImage extends
	AbstractUnaryHybridCF<ClearCLImage, ClearCLImage> implements CLIJ_crop,
	Contingent
{

	@Parameter
	private Integer startX;

	@Parameter
	private Integer startY;

	@Parameter
	private Integer width;

	@Parameter
	private Integer height;

	@Parameter
	CLIJService clij;

	@Override
	public void compute(final ClearCLImage input, final ClearCLImage output) {

		Kernels.crop(clij.get(), input, output, startX, startY);
	}

	@Override
	public boolean conforms() {
		return true;
	}

	@Override
	public ClearCLImage createOutput(ClearCLImage input) {
		return clij.get().createCLImage(new long[] { width, height }, input
			.getChannelDataType());
	}

}

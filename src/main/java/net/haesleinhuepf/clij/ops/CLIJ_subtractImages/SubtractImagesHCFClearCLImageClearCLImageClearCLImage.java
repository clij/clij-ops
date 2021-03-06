
package net.haesleinhuepf.clij.ops.CLIJ_subtractImages;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.haesleinhuepf.clij.CLIJService;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.hybrid.AbstractBinaryHybridCF;

/**
 * @author Deborah Schmidt
 */

@Plugin(type = CLIJ_subtractImages.class)
public class SubtractImagesHCFClearCLImageClearCLImageClearCLImage extends
	AbstractBinaryHybridCF<ClearCLImage, ClearCLImage, ClearCLImage> implements
	CLIJ_subtractImages, Contingent
{

	@Parameter
	CLIJService clij;

	@Override
	public void compute(final ClearCLImage input1, final ClearCLImage input2,
		final ClearCLImage output)
	{

		Kernels.subtractImages(clij.get(), input1, input2, output);
	}

	@Override
	public boolean conforms() {
		return true;
	}

	@Override
	public ClearCLImage createOutput(ClearCLImage input1, ClearCLImage input2) {
		return clij.get().create(input1);
	}

}

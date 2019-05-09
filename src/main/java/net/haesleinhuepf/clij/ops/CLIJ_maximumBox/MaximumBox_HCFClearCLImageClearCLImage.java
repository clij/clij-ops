
package net.haesleinhuepf.clij.ops.CLIJ_maximumBox;

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

@Plugin(type = CLIJ_maximumBox.class)
public class MaximumBox_HCFClearCLImageClearCLImage extends
	AbstractUnaryHybridCF<ClearCLImage, ClearCLImage> implements CLIJ_maximumBox,
	Contingent
{

	@Parameter
	private int radiusX;

	@Parameter
	private int radiusY;

	@Parameter
	private int radiusZ;

	@Parameter
	CLIJService clij;

	@Override
	public void compute(final ClearCLImage input, final ClearCLImage output) {

		Kernels.maximumBox(clij.get(), input, output, radiusX, radiusY, radiusZ);
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
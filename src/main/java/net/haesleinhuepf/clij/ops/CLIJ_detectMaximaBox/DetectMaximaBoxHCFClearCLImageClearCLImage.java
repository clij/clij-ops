
package net.haesleinhuepf.clij.ops.CLIJ_detectMaximaBox;

import net.haesleinhuepf.clij.CLIJService;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * @author Deborah Schmidt
 */

@Plugin(type = CLIJ_detectMaximaBox.class)
public class DetectMaximaBoxHCFClearCLImageClearCLImage extends
	AbstractUnaryHybridCF<ClearCLImage, ClearCLImage> implements CLIJ_detectMaximaBox,
	Contingent
{

	@Parameter
	CLIJService clij;

	@Parameter
	private Integer radius;

	@Override
	public void compute(final ClearCLImage input, final ClearCLImage output) {

		Kernels.detectMaximaBox(clij.get(), input, output, radius);
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

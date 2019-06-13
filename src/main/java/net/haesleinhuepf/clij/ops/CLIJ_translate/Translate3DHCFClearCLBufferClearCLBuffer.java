
package net.haesleinhuepf.clij.ops.CLIJ_translate3D;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.haesleinhuepf.clij.CLIJService;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.utilities.AffineTransform;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.realtransform.AffineTransform3D;

/**
 * @author Deborah Schmidt
 */

@Plugin(type = CLIJ_translate3D.class)
public class Translate3DHCFClearCLBufferClearCLBuffer extends
	AbstractUnaryHybridCF<ClearCLBuffer, ClearCLBuffer> implements
	CLIJ_translate3D, Contingent
{

	@Parameter
	private float translateX;

	@Parameter
	private float translateY;

	@Parameter
	private float translateZ;

	@Parameter
	CLIJService clij;

	@Override
	public void compute(final ClearCLBuffer input, final ClearCLBuffer output) {

		float translateX = -this.translateX;
		float translateY = -this.translateY;
		float translateZ = -this.translateZ;
		AffineTransform3D at = new AffineTransform3D();
		at.translate(translateX, translateY, translateZ);

		Kernels.affineTransform(clij.get(), input, output, AffineTransform
			.matrixToFloatArray(at));
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

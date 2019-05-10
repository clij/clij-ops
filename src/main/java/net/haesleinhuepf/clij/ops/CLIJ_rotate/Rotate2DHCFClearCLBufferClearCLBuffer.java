
package net.haesleinhuepf.clij.ops.CLIJ_rotate;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.haesleinhuepf.clij.CLIJ;
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

@Plugin(type = CLIJ_rotate2D.class)
public class Rotate2DHCFClearCLBufferClearCLBuffer extends
	AbstractUnaryHybridCF<ClearCLBuffer, ClearCLBuffer> implements CLIJ_rotate2D,
	Contingent
{

	@Parameter
	float angle;

	@Parameter
	boolean rotateAroundCenter;

	@Parameter
	CLIJService clij;

	@Override
	public void compute(final ClearCLBuffer input, final ClearCLBuffer output) {
		float angleZ = (float) ((-angle) / 180.0f * Math.PI);

		AffineTransform3D at = new AffineTransform3D();

		if (rotateAroundCenter) {
			at.translate(-input.getWidth() / 2, -input.getHeight() / 2, -input
				.getDepth() / 2);
		}
		at.rotate(2, angleZ);
		if (rotateAroundCenter) {
			at.translate(input.getWidth() / 2, input.getHeight() / 2, input
				.getDepth() / 2);
		}

		Kernels.affineTransform(clij.get(), input, output, AffineTransform
			.matrixToFloatArray(at));

		// TODO neccessary?
		// Kernels.copy(clij.get(), output, (ClearCLBuffer)args[1]);
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

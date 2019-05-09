
package net.haesleinhuepf.clij.ops.CLIJ_automaticThreshold;

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

@Plugin(type = CLIJ_automaticThreshold.class)
public class AutomaticThresholdHCFClearCLBufferClearCLBuffer2 extends
	AbstractUnaryHybridCF<ClearCLBuffer, ClearCLBuffer> implements
	CLIJ_automaticThreshold, Contingent
{

	@Parameter
	private String userSelectedMethod;

	@Parameter
	private Float minimumGreyValue;

	@Parameter
	private Float maximumGreyValue;

	@Parameter
	private Integer numberOfBins;

	@Parameter
	CLIJService clij;

	@Override
	public void compute(final ClearCLBuffer input, final ClearCLBuffer output) {

		Kernels.automaticThreshold(clij.get(), input, output, userSelectedMethod,
			minimumGreyValue, maximumGreyValue, numberOfBins);
	}

	@Override
	public boolean conforms() {
		return true;
	}

	@Override
	public ClearCLBuffer createOutput(ClearCLBuffer input) {
		CLIJ clij = CLIJ.getInstance();
		return clij.create(input);
	}

}

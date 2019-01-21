/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.haesleinhuepf.clij.ops.filter.convolve;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.customconvolutionplugin.Convolve;
import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imglib2.type.numeric.RealType;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Convolves an image naively.
 */
@Plugin(type = Ops.Filter.Convolve.class)
public class ConvolveCLIJ<I extends RealType<I>, K extends RealType<K>, O extends RealType<O>>
	extends
	AbstractUnaryComputerOp<ClearCLBuffer, ClearCLBuffer>
	implements Ops.Filter.Convolve, Contingent
{
	
	@Parameter
	private ClearCLBuffer kernel;
	

	@Override
	public void compute(final ClearCLBuffer input,
		final ClearCLBuffer output)
	{
		final CLIJ clij = CLIJ.getInstance();
		convolveWithCustomKernel(clij, input, kernel, output);
	}

	static boolean convolveWithCustomKernel(CLIJ clij, ClearCLBuffer src, ClearCLBuffer kernel, ClearCLBuffer dst) {
		HashMap<String, Object> parameters = new HashMap<>();
		parameters.put("src", src);
		parameters.put("kernelImage", kernel);
		parameters.put("dst", dst);

		return clij.execute(Convolve.class,
				"customConvolution.cl",
				"custom_convolution_" + src.getDimension() + "d",
				parameters);
	}

	@Override
	public boolean conforms() {
		return true;
	}

}

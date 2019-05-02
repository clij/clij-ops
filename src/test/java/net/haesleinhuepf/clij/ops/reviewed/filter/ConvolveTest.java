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

package net.haesleinhuepf.clij.ops.reviewed.filter;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.imagej.ops.AbstractOpTest;
import net.imglib2.*;
import net.imglib2.algorithm.region.hypersphere.HyperSphere;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;
import org.junit.Test;

import static net.haesleinhuepf.clij.utilities.CLInfo.listSupportedTypes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Tests involving convolvers.
 */
public class ConvolveTest extends AbstractOpTest {

	final int xSize = 128;
	final int ySize = 128;
	final int zSize = 128;

	@Test
	public void testConvolveWithoutCLIJ() {

		final Img<FloatType> phantom = createPhantom();

		final RandomAccessibleInterval<FloatType> psf = createKernel(phantom);
		final RandomAccessibleInterval<FloatType> output = ops.filter().convolve(phantom, psf);
	}
	
	@Test
	public void testConvolveCLIJ() {

		final Img<FloatType> phantom = createPhantom();

		final RandomAccessibleInterval<FloatType> psf = createKernel(phantom);
		
		final CLIJ clij = CLIJ.getInstance();
		listSupportedTypes(clij.getClearCLContext());
		final ClearCLBuffer input = clij.convert(phantom, ClearCLBuffer.class);
		final ClearCLBuffer kernel = clij.convert(psf, ClearCLBuffer.class);
		final ClearCLBuffer output = clij.convert(phantom.factory().create(phantom), ClearCLBuffer.class);

		ops.run("convolve", output, input, kernel);
	}

	private RandomAccessibleInterval<FloatType> createKernel(Img<FloatType> phantom) {
		final RandomAccess<FloatType> randomAccess = phantom.randomAccess();

		randomAccess.setPosition(new long[] { xSize / 2, ySize / 2, zSize / 2 });
		randomAccess.get().setReal(255.0);

		randomAccess.setPosition(new long[] { xSize / 4, ySize / 4, zSize / 4 });
		randomAccess.get().setReal(255.0);

		final Point location = new Point(phantom.numDimensions());
		location.setPosition(new long[] { 3 * xSize / 4, 3 * ySize / 4, 3 * zSize /
				4 });

		final HyperSphere<FloatType> hyperSphere = new HyperSphere<>(phantom, location,
				5);

		for (FloatType value : hyperSphere) {
			value.setReal(16);
		}

		return ops.create().kernelGauss(new double[] { 5, 5, 5 }, new FloatType());

	}

	private Img<FloatType> createPhantom() {
		return ops.create().img(new FinalInterval(xSize, ySize, zSize), new FloatType());
	}

}

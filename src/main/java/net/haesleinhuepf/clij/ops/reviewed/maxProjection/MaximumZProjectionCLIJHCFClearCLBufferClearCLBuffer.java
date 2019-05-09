package net.haesleinhuepf.clij.ops.reviewed.maxProjection;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import org.scijava.plugin.Plugin;
// This is generated code. See src/test/java/net/haesleinhuepf/clij/opsgenerator for details

@Plugin(type = MaximumZProjectionCLIJ.class)
public class MaximumZProjectionCLIJHCFClearCLBufferClearCLBuffer
        extends
        AbstractUnaryHybridCF<ClearCLBuffer,ClearCLBuffer>
        implements MaximumZProjectionCLIJ, Contingent
{

    @Override
    public void compute(final ClearCLBuffer input, final ClearCLBuffer output)
    {
        final CLIJ clij = CLIJ.getInstance();
        Kernels.maximumZProjection(clij, input, output);
    }

    @Override
    public boolean conforms() {
        return true;
    }

    @Override
    public ClearCLBuffer createOutput(ClearCLBuffer input) {
        final CLIJ clij = CLIJ.getInstance();
        return clij.createCLBuffer(new long[]{input.getWidth(), input.getHeight()}, input.getNativeType());
    }
}
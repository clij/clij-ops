package net.haesleinhuepf.clij.ops.reviewed.filter.reslice;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import org.scijava.plugin.Plugin;
// This is generated code. See src/test/java/net/haesleinhuepf/clij/opsgenerator for details

@Plugin(type = ResliceLeftCLIJ.class)
public class ResliceLeftCLIJHCFClearCLBufferClearCLBuffer
        extends
        AbstractUnaryHybridCF<ClearCLBuffer,ClearCLBuffer>
        implements ResliceLeftCLIJ, Contingent
{

    @Override
    public void compute(final ClearCLBuffer input, final ClearCLBuffer output)
    {
        final CLIJ clij = CLIJ.getInstance();

            Kernels.resliceLeft(clij, input, output);
    }

    @Override
    public boolean conforms() {
        return true;
    }

    @Override
    public ClearCLBuffer createOutput(ClearCLBuffer input) {
        final CLIJ clij = CLIJ.getInstance();
        return clij.createCLBuffer(new long[]{input.getHeight(), input.getDepth(), input.getWidth()}, input.getNativeType());
    }
}

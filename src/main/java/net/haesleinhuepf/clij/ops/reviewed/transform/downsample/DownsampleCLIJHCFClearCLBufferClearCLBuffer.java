package net.haesleinhuepf.clij.ops.reviewed.transform.downsample;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = DownsampleCLIJ.class)
public class DownsampleCLIJHCFClearCLBufferClearCLBuffer
        extends
        AbstractUnaryHybridCF<ClearCLBuffer,ClearCLBuffer>
        implements DownsampleCLIJ, Contingent
{

    @Parameter
    private Float factorX;

    @Parameter
    private Float factorY;

    @Parameter
    private Float factorZ;

    @Override
    public void compute(final ClearCLBuffer input, final ClearCLBuffer output) {
        final CLIJ clij = CLIJ.getInstance();
        Kernels.downsample(clij, input, output, factorX, factorY, factorZ);
    }

    @Override
    public boolean conforms() {
        return true;
    }

    @Override
    public ClearCLBuffer createOutput(ClearCLBuffer input) {
        final CLIJ clij = CLIJ.getInstance();
        long[] dims = new long[input.getDimensions().length];
        for (int i = 0; i < dims.length; i++) {
            dims[i] = input.getDimensions()[i];
        }
        if(dims.length > 0) dims[0] *= factorX;
        if(dims.length > 1) dims[1] *= factorY;
        if(dims.length > 2) dims[2] *= factorZ;
        ClearCLBuffer output = clij.createCLBuffer(dims, input.getNativeType());
        return output;
    }

}

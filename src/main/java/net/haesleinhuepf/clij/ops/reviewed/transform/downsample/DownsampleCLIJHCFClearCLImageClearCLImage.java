package net.haesleinhuepf.clij.ops.reviewed.transform.downsample;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = DownsampleCLIJ.class)
public class DownsampleCLIJHCFClearCLImageClearCLImage
        extends
        AbstractUnaryHybridCF<ClearCLImage,ClearCLImage>
        implements DownsampleCLIJ, Contingent
{

    @Parameter
    private Float factorX;

    @Parameter
    private Float factorY;

    @Parameter
    private Float factorZ;

    @Override
    public void compute(final ClearCLImage input, final ClearCLImage output)
    {
        final CLIJ clij = CLIJ.getInstance();

            Kernels.downsample(clij, input, output, factorX, factorY, factorZ);
    }

    @Override
    public boolean conforms() {
        return true;
    }

    @Override
    public ClearCLImage createOutput(ClearCLImage input) {
        final CLIJ clij = CLIJ.getInstance();
        long[] dims = input.getDimensions();
        if(dims.length > 0) dims[0] *= factorX;
        if(dims.length > 1) dims[1] *= factorY;
        if(dims.length > 2) dims[2] *= factorZ;
        return clij.createCLImage(dims, null);
    }
}

package net.haesleinhuepf.clij.ops.reviewed.radialProjection;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
// This is generated code. See src/test/java/net/haesleinhuepf/clij/opsgenerator for details

@Plugin(type = RadialProjectionCLIJ.class)
public class RadialProjectionCLIJHCFClearCLBufferClearCLBuffer
        extends
        AbstractUnaryHybridCF<ClearCLBuffer,ClearCLBuffer>
        implements RadialProjectionCLIJ, Contingent
{

    @Parameter
    private Integer numberOfAngles;
    @Parameter
    private Float angleStepSize;

    @Override
    public void compute(final ClearCLBuffer input, final ClearCLBuffer output)
    {
        final CLIJ clij = CLIJ.getInstance();

            Kernels.radialProjection(clij, input, output, angleStepSize);
    }

    @Override
    public boolean conforms() {
        return true;
    }

    @Override
    public ClearCLBuffer createOutput(ClearCLBuffer input) {
        final CLIJ clij = CLIJ.getInstance();
        int effectiveNumberOfAngles = (int)((float)numberOfAngles / angleStepSize);
        int maximumRadius = (int)Math.sqrt(Math.pow(input.getWidth() / 2, 2) + Math.pow(input.getHeight() / 2, 2));
        return clij.createCLBuffer(new long[]{maximumRadius, input.getDepth(), effectiveNumberOfAngles}, input.getNativeType());
    }
}

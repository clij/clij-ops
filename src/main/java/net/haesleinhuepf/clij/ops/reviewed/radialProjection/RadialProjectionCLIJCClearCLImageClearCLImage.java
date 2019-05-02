package net.haesleinhuepf.clij.ops.reviewed.radialProjection;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
// This is generated code. See src/test/java/net/haesleinhuepf/clij/opsgenerator for details

@Plugin(type = RadialProjectionCLIJ.class)
public class RadialProjectionCLIJCClearCLImageClearCLImage
        extends
        AbstractUnaryComputerOp<ClearCLImage,ClearCLImage>
        implements RadialProjectionCLIJ, Contingent
{

    @Parameter
    private Float deltaAngle;

    @Override
    public void compute(final ClearCLImage input, final ClearCLImage output)
    {
        final CLIJ clij = CLIJ.getInstance();

            Kernels.radialProjection(clij, input, output, deltaAngle);
    }

    @Override
    public boolean conforms() {
        return true;
    }

}

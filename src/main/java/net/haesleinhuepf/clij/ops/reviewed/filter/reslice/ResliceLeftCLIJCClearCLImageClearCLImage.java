package net.haesleinhuepf.clij.ops.reviewed.filter.reslice;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import org.scijava.plugin.Plugin;
// This is generated code. See src/test/java/net/haesleinhuepf/clij/opsgenerator for details

@Plugin(type = ResliceLeftCLIJ.class)
public class ResliceLeftCLIJCClearCLImageClearCLImage
        extends
        AbstractUnaryComputerOp<ClearCLImage,ClearCLImage>
        implements ResliceLeftCLIJ, Contingent
{

    @Override
    public void compute(final ClearCLImage input, final ClearCLImage output)
    {
        final CLIJ clij = CLIJ.getInstance();

            Kernels.resliceLeft(clij, input, output);
    }

    @Override
    public boolean conforms() {
        return true;
    }

}

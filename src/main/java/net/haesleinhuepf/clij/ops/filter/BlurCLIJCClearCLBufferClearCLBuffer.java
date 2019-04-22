package net.haesleinhuepf.clij.ops.filter;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
// This is generated code. See src/test/java/net/haesleinhuepf/clij/opsgenerator for details

@Plugin(type = Ops.Filter.Gauss.class)
public class BlurCLIJCClearCLBufferClearCLBuffer
        extends
        AbstractUnaryComputerOp<ClearCLBuffer,ClearCLBuffer>
        implements Ops.Filter.Gauss, Contingent
{

    @Parameter
    private Float sigma;

    @Override
    public void compute(final ClearCLBuffer input, final ClearCLBuffer output)
    {
        final CLIJ clij = CLIJ.getInstance();
        Kernels.blur(clij, input, output, sigma, sigma, sigma);
    }

    @Override
    public boolean conforms() {
        return true;
    }

}

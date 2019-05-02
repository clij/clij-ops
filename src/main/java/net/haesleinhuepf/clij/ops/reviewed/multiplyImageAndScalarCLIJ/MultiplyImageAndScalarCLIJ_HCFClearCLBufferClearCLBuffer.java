package net.haesleinhuepf.clij.ops.reviewed.multiplyImageAndScalarCLIJ;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
// This is generated code. See src/test/java/net/haesleinhuepf/clij/opsgenerator for details


@Plugin(type = MultiplyImageAndScalarCLIJ.class)
public class MultiplyImageAndScalarCLIJ_HCFClearCLBufferClearCLBuffer
        extends
        AbstractUnaryHybridCF<ClearCLBuffer,ClearCLBuffer>
        implements MultiplyImageAndScalarCLIJ, Contingent
{

    @Parameter
    private Float scalar;

    @Override
    public void compute(final ClearCLBuffer input, final ClearCLBuffer output)
    {
        final CLIJ clij = CLIJ.getInstance();

            Kernels.multiplyImageAndScalar(clij, input, output, scalar);
    }

    @Override
    public boolean conforms() {
        return true;
    }

    @Override
    public ClearCLBuffer createOutput(ClearCLBuffer input) {
        CLIJ clij = CLIJ.getInstance();
        return clij.create(input);
    }

}

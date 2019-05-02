package net.haesleinhuepf.clij.ops.reviewed.meanBoxCLIJ;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
// This is generated code. See src/test/java/net/haesleinhuepf/clij/opsgenerator for details


@Plugin(type = MeanBoxCLIJ.class)
public class MeanBoxCLIJ_HCFClearCLBufferClearCLBuffer
        extends
        AbstractUnaryHybridCF<ClearCLBuffer,ClearCLBuffer>
        implements MeanBoxCLIJ, Contingent
{

    @Parameter
    private int radiusX;

    @Parameter
    private int radiusY;

    @Parameter
    private int radiusZ;

    @Override
    public void compute(final ClearCLBuffer input, final ClearCLBuffer output)
    {
        final CLIJ clij = CLIJ.getInstance();

            Kernels.meanBox(clij, input, output, radiusX, radiusY, radiusZ);
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

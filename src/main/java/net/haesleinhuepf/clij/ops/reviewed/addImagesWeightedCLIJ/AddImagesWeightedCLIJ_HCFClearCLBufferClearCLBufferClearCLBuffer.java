package net.haesleinhuepf.clij.ops.reviewed.addImagesWeightedCLIJ;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.hybrid.AbstractBinaryHybridCF;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
// This is generated code. See src/test/java/net/haesleinhuepf/clij/opsgenerator for details


@Plugin(type = AddImagesWeightedCLIJ.class)
public class AddImagesWeightedCLIJ_HCFClearCLBufferClearCLBufferClearCLBuffer
        extends
        AbstractBinaryHybridCF<ClearCLBuffer, ClearCLBuffer, ClearCLBuffer>
        implements AddImagesWeightedCLIJ, Contingent
{

    @Parameter
    private Float factor;

    @Parameter
    private Float factor1;

    @Override
    public void compute(final ClearCLBuffer input1, final ClearCLBuffer input2, final ClearCLBuffer output)
    {
        final CLIJ clij = CLIJ.getInstance();

        Kernels.addImagesWeighted(clij, input1, input2, output, factor, factor1);
    }

    @Override
    public boolean conforms() {
        return true;
    }

    @Override
    public ClearCLBuffer createOutput(ClearCLBuffer input1, ClearCLBuffer input2) {
        CLIJ clij = CLIJ.getInstance();
        return clij.create(input1);
    }

}

package net.haesleinhuepf.clij.ops.reviewed.addImagesWeightedCLIJ;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.hybrid.AbstractBinaryHybridCF;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
// This is generated code. See src/test/java/net/haesleinhuepf/clij/opsgenerator for details


@Plugin(type = AddImagesWeightedCLIJ.class)
public class AddImagesWeightedCLIJ_HCFClearCLImageClearCLImageClearCLImage
        extends
        AbstractBinaryHybridCF<ClearCLImage, ClearCLImage, ClearCLImage>
        implements AddImagesWeightedCLIJ, Contingent
{

    @Parameter
    private Float factor;

    @Parameter
    private Float factor1;

    @Override
    public void compute(final ClearCLImage input1, final ClearCLImage input2, final ClearCLImage output)
    {
        final CLIJ clij = CLIJ.getInstance();

        Kernels.addImagesWeighted(clij, input1, input2, output, factor, factor1);
    }

    @Override
    public boolean conforms() {
        return true;
    }

    @Override
    public ClearCLImage createOutput(ClearCLImage input1, ClearCLImage input2) {
        CLIJ clij = CLIJ.getInstance();
        return clij.create(input1);
    }

}

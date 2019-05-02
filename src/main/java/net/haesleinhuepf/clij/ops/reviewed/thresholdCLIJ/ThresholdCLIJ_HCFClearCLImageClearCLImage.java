package net.haesleinhuepf.clij.ops.reviewed.thresholdCLIJ;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
// This is generated code. See src/test/java/net/haesleinhuepf/clij/opsgenerator for details


@Plugin(type = ThresholdCLIJ.class)
public class ThresholdCLIJ_HCFClearCLImageClearCLImage
        extends
        AbstractUnaryHybridCF<ClearCLImage,ClearCLImage>
        implements ThresholdCLIJ, Contingent
{

    @Parameter
    private Float threshold;

    @Override
    public void compute(final ClearCLImage input, final ClearCLImage output)
    {
        final CLIJ clij = CLIJ.getInstance();

            Kernels.threshold(clij, input, output, threshold);
    }

    @Override
    public boolean conforms() {
        return true;
    }

    @Override
    public ClearCLImage createOutput(ClearCLImage input) {
        CLIJ clij = CLIJ.getInstance();
        return clij.create(input);
    }

}

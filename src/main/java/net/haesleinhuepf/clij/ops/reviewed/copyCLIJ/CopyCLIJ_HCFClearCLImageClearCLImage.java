package net.haesleinhuepf.clij.ops.reviewed.copyCLIJ;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import org.scijava.plugin.Plugin;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
// This is generated code. See src/test/java/net/haesleinhuepf/clij/opsgenerator for details


@Plugin(type = CopyCLIJ.class)
public class CopyCLIJ_HCFClearCLImageClearCLImage
        extends
        AbstractUnaryHybridCF<ClearCLImage,ClearCLImage>
        implements CopyCLIJ, Contingent
{

    @Override
    public void compute(final ClearCLImage input, final ClearCLImage output)
    {
        final CLIJ clij = CLIJ.getInstance();

            Kernels.copy(clij, input, output);
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
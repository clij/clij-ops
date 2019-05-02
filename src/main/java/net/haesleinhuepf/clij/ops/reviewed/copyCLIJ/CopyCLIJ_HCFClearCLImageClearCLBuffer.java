package net.haesleinhuepf.clij.ops.reviewed.copyCLIJ;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import org.scijava.plugin.Plugin;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;


@Plugin(type = CopyCLIJ.class)
public class CopyCLIJ_HCFClearCLImageClearCLBuffer
        extends
        AbstractUnaryHybridCF<ClearCLImage,ClearCLBuffer>
        implements CopyCLIJ, Contingent
{

    @Override
    public void compute(final ClearCLImage input, final ClearCLBuffer output)
    {
        final CLIJ clij = CLIJ.getInstance();

            Kernels.copy(clij, input, output);
    }

    @Override
    public boolean conforms() {
        return true;
    }

    @Override
    public ClearCLBuffer createOutput(ClearCLImage input) {
        CLIJ clij = CLIJ.getInstance();
        return clij.createCLBuffer(input.getDimensions(), input.getNativeType());
    }

}

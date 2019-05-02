package net.haesleinhuepf.clij.ops.reviewed.copyCLIJ;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.converters.ConverterUtilities;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import org.scijava.plugin.Plugin;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
// This is generated code. See src/test/java/net/haesleinhuepf/clij/opsgenerator for details


@Plugin(type = CopyCLIJ.class)
public class CopyCLIJ_HCFClearCLBufferClearCLImage
        extends
        AbstractUnaryHybridCF<ClearCLBuffer,ClearCLImage>
        implements CopyCLIJ, Contingent
{

    @Override
    public void compute(final ClearCLBuffer input, final ClearCLImage output)
    {
        final CLIJ clij = CLIJ.getInstance();

            Kernels.copy(clij, input, output);
    }

    @Override
    public boolean conforms() {
        return true;
    }

    @Override
    public ClearCLImage createOutput(ClearCLBuffer input) {
        CLIJ clij = CLIJ.getInstance();
        return clij.createCLImage(input.getDimensions(), ConverterUtilities.nativeTypeToImageChannelDataType(input.getNativeType()));
    }

}

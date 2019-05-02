package net.haesleinhuepf.clij.ops.reviewed.cropCLIJ;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
// This is generated code. See src/test/java/net/haesleinhuepf/clij/opsgenerator for details


@Plugin(type = CropCLIJ.class)
public class Crop3DCLIJ_HCFClearCLImageClearCLImage
        extends
        AbstractUnaryHybridCF<ClearCLImage,ClearCLImage>
        implements CropCLIJ, Contingent
{

    @Parameter
    private Integer startX;

    @Parameter
    private Integer startY;

    @Parameter
    private Integer startZ;

    @Parameter
    private Integer width;

    @Parameter
    private Integer height;

    @Parameter
    private Integer depth;


    @Override
    public void compute(final ClearCLImage input, final ClearCLImage output)
    {
        final CLIJ clij = CLIJ.getInstance();

            Kernels.crop(clij, input, output, startX, startY, startZ);
    }

    @Override
    public boolean conforms() {
        return true;
    }

    @Override
    public ClearCLImage createOutput(ClearCLImage input) {
        final CLIJ clij = CLIJ.getInstance();
        return clij.createCLImage(new long[]{width, height, depth}, input.getChannelDataType());
    }

}

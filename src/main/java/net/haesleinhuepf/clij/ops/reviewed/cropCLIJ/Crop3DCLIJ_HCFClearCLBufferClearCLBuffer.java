package net.haesleinhuepf.clij.ops.reviewed.cropCLIJ;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
// This is generated code. See src/test/java/net/haesleinhuepf/clij/opsgenerator for details


@Plugin(type = CropCLIJ.class)
public class Crop3DCLIJ_HCFClearCLBufferClearCLBuffer
        extends
        AbstractUnaryHybridCF<ClearCLBuffer,ClearCLBuffer>
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
    public void compute(final ClearCLBuffer input, final ClearCLBuffer output)
    {
        final CLIJ clij = CLIJ.getInstance();
        Kernels.crop(clij, input, output, startX, startY, startZ);
    }

    @Override
    public boolean conforms() {
        return true;
    }

    @Override
    public ClearCLBuffer createOutput(ClearCLBuffer input) {
        final CLIJ clij = CLIJ.getInstance();
        return clij.createCLBuffer(new long[]{width, height, depth}, input.getNativeType());
    }

}

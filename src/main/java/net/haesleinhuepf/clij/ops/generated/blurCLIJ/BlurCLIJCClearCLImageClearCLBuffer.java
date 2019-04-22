package net.haesleinhuepf.clij.ops.generated.blurCLIJ;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
// This is generated code. See src/test/java/net/haesleinhuepf/clij/opsgenerator for details

@Plugin(type = Op.class)
public class BlurCLIJCClearCLImageClearCLBuffer
        extends
        AbstractUnaryComputerOp<ClearCLImage,ClearCLBuffer>
        implements BlurCLIJ, Contingent
{

    @Parameter
    private float blurSigmaX;

    @Parameter
    private float blurSigmaY;

    @Parameter
    private float blurSigmaZ;

    @Override
    public void compute(final ClearCLImage input, final ClearCLBuffer output)
    {
        final CLIJ clij = CLIJ.getInstance();

            Kernels.blur(clij, input, output, blurSigmaX, blurSigmaY, blurSigmaZ);
    }

    @Override
    public boolean conforms() {
        return true;
    }

}

package net.haesleinhuepf.clij.ops.generated.blurCLIJ;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.AbstractBinaryComputerOp;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.type.numeric.RealType;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import ij.IJ;
import ij.ImagePlus;
import ij.process.AutoThresholder;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.utilities.AffineTransform;
import net.haesleinhuepf.clij.utilities.CLKernelExecutor;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import java.nio.FloatBuffer;
import java.util.HashMap;
import static net.haesleinhuepf.clij.utilities.CLIJUtilities.radiusToKernelSize;
import static net.haesleinhuepf.clij.utilities.CLIJUtilities.sigmaToKernelSize;
// This is generated code. See src/test/java/net/haesleinhuepf/clij/opsgenerator for details

@Plugin(type = Op.class)
public class BlurCLIJCClearCLImageClearCLImage
        extends
        AbstractUnaryComputerOp<ClearCLImage,ClearCLImage>
        implements BlurCLIJ, Contingent
{

    @Parameter
    private Integer kernelSizeX;

    @Parameter
    private Integer kernelSizeY;

    @Parameter
    private Integer kernelSizeZ;

    @Parameter
    private Float sigmaX;

    @Parameter
    private Float sigmaY;

    @Parameter
    private Float sigmaZ;

    @Override
    public void compute(final ClearCLImage input, final ClearCLImage output)
    {
        final CLIJ clij = CLIJ.getInstance();

            Kernels.blur(clij, input, output, kernelSizeX, kernelSizeY, kernelSizeZ, sigmaX, sigmaY, sigmaZ);
    }

    @Override
    public boolean conforms() {
        return true;
    }

}

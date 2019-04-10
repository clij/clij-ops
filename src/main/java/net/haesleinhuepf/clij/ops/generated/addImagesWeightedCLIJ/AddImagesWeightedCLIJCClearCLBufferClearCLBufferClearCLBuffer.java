package net.haesleinhuepf.clij.ops.generated.addImagesWeightedCLIJ;
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
public class AddImagesWeightedCLIJCClearCLBufferClearCLBufferClearCLBuffer
        extends
        AbstractBinaryComputerOp<ClearCLBuffer, ClearCLBuffer, ClearCLBuffer>
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

}

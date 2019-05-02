package net.haesleinhuepf.clij.ops.reviewed.copySliceCLIJ;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imagej.ops.special.hybrid.AbstractBinaryHybridCF;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.type.numeric.RealType;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import ij.IJ;
import ij.ImagePlus;
import ij.process.AutoThresholder;
import net.haesleinhuepf.clij.clearcl.ClearCL;
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


@Plugin(type = CopySliceCLIJ.class)
public class CopySliceCLIJ_HCFClearCLImageClearCLImage
        extends
        AbstractUnaryHybridCF<ClearCLImage,ClearCLImage>
        implements CopySliceCLIJ, Contingent
{

    @Parameter
    private Integer planeIndex;

    @Override
    public void compute(final ClearCLImage input, final ClearCLImage output)
    {
        final CLIJ clij = CLIJ.getInstance();

            Kernels.copySlice(clij, input, output, planeIndex);
    }

    @Override
    public boolean conforms() {
        return true;
    }

    @Override
    public ClearCLImage createOutput(ClearCLImage input) {
        CLIJ clij = CLIJ.getInstance();
        if (input.getDimension() == 2) {
            return clij.createCLImage(new long[]{ input.getWidth(), input.getHeight(), planeIndex}, input.getChannelDataType());
        } else  {
            return clij.createCLImage(new long[]{ input.getWidth(), input.getHeight()}, input.getChannelDataType());
        }
    }

}

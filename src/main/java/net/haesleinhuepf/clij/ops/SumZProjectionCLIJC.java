package net.haesleinhuepf.clij.ops;
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
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.utilities.CLKernelExecutor;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import java.nio.FloatBuffer;
import java.util.HashMap;
import static net.haesleinhuepf.clij.utilities.CLIJUtilities.radiusToKernelSize;
import static net.haesleinhuepf.clij.utilities.CLIJUtilities.sigmaToKernelSize;
// This is generated code. See src/test/java/net/haesleinhuepf/clij/opsgenerator for details

@Plugin(type = Op.class)
public class SumZProjectionCLIJC
        extends
        AbstractUnaryComputerOp<ClearCLBuffer,ClearCLBuffer>
        implements Op, Contingent
{

    @Override
    public void compute(final ClearCLBuffer input, final ClearCLBuffer output)
    {
        final CLIJ clij = CLIJ.getInstance();

            Kernels.sumZProjection(clij, input, output);
    }

    @Override
    public boolean conforms() {
        return true;
    }

}

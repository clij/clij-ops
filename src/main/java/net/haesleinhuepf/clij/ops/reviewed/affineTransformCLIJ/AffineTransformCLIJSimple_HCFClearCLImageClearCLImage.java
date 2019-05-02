package net.haesleinhuepf.clij.ops.reviewed.affineTransformCLIJ;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;


@Plugin(type = AffineTransformCLIJ.class)
public class AffineTransformCLIJSimple_HCFClearCLImageClearCLImage
        extends
        AbstractUnaryHybridCF<ClearCLImage,ClearCLImage>
        implements AffineTransformCLIJ, Contingent
{

    @Parameter
    private String transform;

    @Override
    public void compute(final ClearCLImage input, final ClearCLImage output)
    {
        final CLIJ clij = CLIJ.getInstance();

        Kernels.affineTransform(clij, input, output,
                AffineTransformHelper.transformStringToMatrix(transform, input.getWidth(), input.getHeight(), input.getDepth()));
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

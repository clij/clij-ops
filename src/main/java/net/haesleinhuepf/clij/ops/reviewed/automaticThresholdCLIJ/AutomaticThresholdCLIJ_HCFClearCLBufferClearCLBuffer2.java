package net.haesleinhuepf.clij.ops.reviewed.automaticThresholdCLIJ;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ops.Contingent;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
// This is generated code. See src/test/java/net/haesleinhuepf/clij/opsgenerator for details


@Plugin(type = AutomaticThresholdCLIJ.class)
public class AutomaticThresholdCLIJ_HCFClearCLBufferClearCLBuffer2
        extends
        AbstractUnaryHybridCF<ClearCLBuffer,ClearCLBuffer>
        implements AutomaticThresholdCLIJ, Contingent
{

    @Parameter
    private String userSelectedMethod;

    @Parameter
    private Float minimumGreyValue;

    @Parameter
    private Float maximumGreyValue;

    @Parameter
    private Integer numberOfBins;

    @Override
    public void compute(final ClearCLBuffer input, final ClearCLBuffer output)
    {
        final CLIJ clij = CLIJ.getInstance();

            Kernels.automaticThreshold(clij, input, output, userSelectedMethod, minimumGreyValue, maximumGreyValue, numberOfBins);
    }

    @Override
    public boolean conforms() {
        return true;
    }

    @Override
    public ClearCLBuffer createOutput(ClearCLBuffer input) {
        CLIJ clij = CLIJ.getInstance();
        return clij.create(input);
    }

}

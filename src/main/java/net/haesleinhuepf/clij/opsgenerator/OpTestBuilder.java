
package net.haesleinhuepf.clij.opsgenerator;

public class OpTestBuilder {

	private static String buildTestHeader() {
		StringBuilder builder = new StringBuilder();
		builder.append("package net.haesleinhuepf.clij.ops\n\n");
		builder.append("import net.haesleinhuepf.clij.CLIJ\n");
		builder.append("import net.haesleinhuepf.clij.clearcl.ClearCLBuffer\n");
		builder.append("import net.imagej.ImageJ\n");
		builder.append("import net.imglib2.FinalDimensions\n");
		builder.append("import net.imglib2.img.Img\n");
		builder.append("import net.imglib2.type.numeric.real.FloatType\n");
		builder.append("import org.junit.Test\n\n");
		return builder.toString();
	}

}

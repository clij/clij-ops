
package net.haesleinhuepf.clij.converter;

import org.scijava.convert.AbstractConverter;
import org.scijava.convert.Converter;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.ops.CLIJ_pull.CLIJ_pull;
import net.imagej.ops.OpService;
import net.imglib2.RandomAccessibleInterval;

/**
 * @author Deborah Schmidt
 */
@Plugin(type = Converter.class)
public class ClearCLBufferRandomAccessibleIntervalConverter extends
	AbstractConverter<ClearCLBuffer, RandomAccessibleInterval>
{

	@Parameter
	private OpService ops;

	@Override
	public <T> T convert(Object src, Class<T> dest) {
		return (T) ops.run(CLIJ_pull.class, src);
	}

	@Override
	public Class<RandomAccessibleInterval> getOutputType() {
		return RandomAccessibleInterval.class;
	}

	@Override
	public Class<ClearCLBuffer> getInputType() {
		return ClearCLBuffer.class;
	}
}


package net.haesleinhuepf.clij.converter;

import org.scijava.convert.AbstractConverter;
import org.scijava.convert.Converter;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.ops.CLIJ_push.CLIJ_push;
import net.imagej.ops.OpService;
import net.imglib2.RandomAccessibleInterval;

/**
 * @author Deborah Schmidt
 */
@Plugin(type = Converter.class)
public class RandomAccessibleIntervalClearCLBufferConverter extends
	AbstractConverter<RandomAccessibleInterval, ClearCLBuffer>
{

	@Parameter
	private OpService ops;

	@Override
	public <T> T convert(Object src, Class<T> dest) {
		return (T) ops.run(CLIJ_push.class, src);
	}

	@Override
	public Class<ClearCLBuffer> getOutputType() {
		return ClearCLBuffer.class;
	}

	@Override
	public Class<RandomAccessibleInterval> getInputType() {
		return RandomAccessibleInterval.class;
	}
}

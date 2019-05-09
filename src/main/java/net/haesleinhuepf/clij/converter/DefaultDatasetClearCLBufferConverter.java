
package net.haesleinhuepf.clij.converter;

import org.scijava.Priority;
import org.scijava.convert.AbstractConverter;
import org.scijava.convert.Converter;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.ops.CLIJ_push.CLIJ_push;
import net.imagej.DefaultDataset;
import net.imagej.ops.OpService;

/**
 * @author Deborah Schmidt
 */
@Plugin(type = Converter.class, priority = Priority.HIGH)
public class DefaultDatasetClearCLBufferConverter extends
	AbstractConverter<DefaultDataset, ClearCLBuffer>
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
	public Class<DefaultDataset> getInputType() {
		return DefaultDataset.class;
	}
}

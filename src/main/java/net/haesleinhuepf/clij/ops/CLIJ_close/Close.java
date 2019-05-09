
package net.haesleinhuepf.clij.ops.CLIJ_close;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.haesleinhuepf.clij.CLIJService;
import net.imagej.ops.special.AbstractNullaryOp;

/**
 * @author Deborah Schmidt
 */

@Plugin(type = CLIJ_close.class)
public class Close extends AbstractNullaryOp implements CLIJ_close {

	@Parameter
	CLIJService clij;

	@Override
	public Object run(Object output) {
		return clij.get().close();
	}

	@Override
	public Object out() {
		return null;
	}
}

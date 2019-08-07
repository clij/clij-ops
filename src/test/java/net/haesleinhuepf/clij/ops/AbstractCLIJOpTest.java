package net.haesleinhuepf.clij.ops;

import net.haesleinhuepf.clij.CLIJService;
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpMatchingService;
import net.imagej.ops.OpService;
import org.scijava.Context;
import org.scijava.cache.CacheService;

public class AbstractCLIJOpTest extends AbstractOpTest {

	@Override
	protected Context createContext() {
		return new Context(OpService.class, OpMatchingService.class,
				CacheService.class, CLIJService.class);
	}

}

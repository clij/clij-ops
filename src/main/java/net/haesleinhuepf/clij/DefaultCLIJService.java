
package net.haesleinhuepf.clij;

import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;
import org.scijava.service.Service;

@Plugin(type = Service.class)
public class DefaultCLIJService extends AbstractService implements CLIJService {

	CLIJ clij;

	@Override
	public CLIJ get() {
		if (clij == null) {
			clij = CLIJ.getInstance();
		}
		return clij;
	}

	@Override
	public CLIJ get(String deviceNameMustContain) {
		if (clij != null && clij.getGPUName().contains(deviceNameMustContain)) {
			return clij;
		}
		if (clij != null) {
			clij.close();
		}
		clij = CLIJ.getInstance(deviceNameMustContain);
		return null;
	}
}


package net.haesleinhuepf.clij;

import net.imagej.ImageJService;

public interface CLIJService extends ImageJService {

	CLIJ get();

	CLIJ get(String deviceNameMustContain);

}

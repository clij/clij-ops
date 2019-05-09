
package net.haesleinhuepf.clij.opsgenerator.properties;

public abstract class ComputerOpProperties extends OpProperties {

	public String dstParameter;

	public ComputerOpProperties(String line, String namespace, String header) {
		super(line, namespace, header);
	}

	public String getClassName() {
		return super.getClassName() + "C";
	}
}

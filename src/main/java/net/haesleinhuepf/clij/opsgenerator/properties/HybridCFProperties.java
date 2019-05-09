
package net.haesleinhuepf.clij.opsgenerator.properties;

public abstract class HybridCFProperties extends OpProperties {

	public String dstParameter;

	public HybridCFProperties(String line, String namespace, String header) {
		super(line, namespace, header);
	}

	public String getClassName() {
		return super.getClassName() + "HCF";
	}
}

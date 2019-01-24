package net.haesleinhuepf.clij.opsgenerator;

public class OpProperties {
	public String methodName;
	public String className;
	public String namespace;
	public String[] parameters;
	public String returnType; // only for functions
	public String header;
	public String dstParameter; // only for computers
	public String src1Parameter;
	public String src2Parameter; // only for binary ops
}

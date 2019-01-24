package net.haesleinhuepf.clij.opsgenerator;

import static net.haesleinhuepf.clij.opsgenerator.OpGenerator.*;

public class BinaryComputerOpBuilder {

	static String buildBinaryComputerOp(OpProperties props) {
		String className = getBinaryClassName(getComputerClassName(props.methodName), props);
		String srcClass = getParameterClass(getParameter(props.parameters, props.src1Parameter));
		String src1Class = getParameterClass(getParameter(props.parameters, props.src2Parameter));
		String dstClass = getParameterClass(getParameter(props.parameters, props.dstParameter));
		String srcName = getParameterName(getParameter(props.parameters, props.src1Parameter));
		String src1Name = getParameterName(getParameter(props.parameters, props.src2Parameter));
		String dstName = getParameterName(getParameter(props.parameters, props.dstParameter));
		String namespaceClass = getClassName(props.namespace);

		StringBuilder builder = new StringBuilder();
		builder.append("package net.haesleinhuepf.clij.ops.generated" + (namespaceClass.isEmpty() ? "" : "." + props.namespace) + ";\n");
		builder.append(props.header);
		builder.append("\n");
		builder.append("@Plugin(type = Op.class)\n");
		builder.append("public class " + className + "\n");
		builder.append("        extends\n");
		builder.append("        AbstractBinaryComputerOp<" + srcClass + ", " + src1Class + ", " + dstClass + ">\n");
		builder.append("        implements " + getOpDep(namespaceClass) + ", Contingent\n");
		builder.append("{\n\n");
		for (String parameter : props.parameters) {
			builder.append(buildParameter(parameter, props.src1Parameter, props.src2Parameter, props.dstParameter));
		}
		builder.append("    @Override\n");
		builder.append("    public void compute(final " + srcClass + " input1, final " + src1Class + " input2, final " + dstClass + " output)\n");
		builder.append("    {\n");
		builder.append("        final CLIJ clij = CLIJ.getInstance();\n\n");

		StringBuilder parametersForCall = new StringBuilder();
		parametersForCall.append("clij");

		int count = 0;
		for (String parameter : props.parameters) {
			String parameterName = getParameterName(parameter);
			if(parameterName.equals(srcName)) parameterName = "input1";
			if(parameterName.equals(src1Name)) parameterName = "input2";
			if(parameterName.equals(dstName)) parameterName = "output";
			if (count > 0) {
				parametersForCall.append(", ");
				parametersForCall.append(parameterName);
			}
			count++;
		}
		builder.append("        Kernels." + props.methodName + "(");
		builder.append(parametersForCall.toString());
		builder.append(");\n");
		builder.append("    }\n\n");

		builder.append(buildConformFunction());
		builder.append("}\n");
		return builder.toString();
	}

	private static String buildBinaryComputerTest(String className, String methodName, String param1, String param2) {
		StringBuilder builder = new StringBuilder();
		builder.append("public class " + className + "Test {\n\n");
		builder.append("    @Test\n");
		builder.append("    public void " + methodName + "Test () {\n");
		builder.append("        ImageJ ij = new ImageJ();\n");
		builder.append("        Img img1 = ij.op().create().img(new FinalDimensions(10, 10, 10), new FloatType());\n");
		builder.append("        Img img2 = img1.copy();\n");
		builder.append("        CLIJ clij = CLIJ.getInstance();\n");
		builder.append("        ClearCLBuffer input = clij.convert(img1, ClearCLBuffer.class);\n");
		builder.append("        ClearCLBuffer output = clij.convert(img2, ClearCLBuffer.class);\n");
		builder.append("        ij.op().run(" + className + ".class, input, output);\n");
		builder.append("    }\n\n");
		builder.append("}\n");
		return builder.toString();
	}
}

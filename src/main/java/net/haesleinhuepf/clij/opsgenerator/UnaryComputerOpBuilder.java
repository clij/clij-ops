package net.haesleinhuepf.clij.opsgenerator;

import static net.haesleinhuepf.clij.opsgenerator.OpGenerator.*;

public class UnaryComputerOpBuilder {

	static String buildUnaryComputerOp(OpProperties props) {

		String className = getUnaryClassName(getComputerClassName(props.methodName), props);
		String srcClass = getParameterClass(getParameter(props.parameters, props.src1Parameter));
		String dstClass = getParameterClass(getParameter(props.parameters, props.dstParameter));
		String srcName = getParameterName(getParameter(props.parameters, props.src1Parameter));
		String dstName = getParameterName(getParameter(props.parameters, props.dstParameter));
		String namespaceClass = getClassName(props.namespace);

		StringBuilder builder = new StringBuilder();
		builder.append("package net.haesleinhuepf.clij.ops.generated" + (namespaceClass.isEmpty() ? "" : "." + props.namespace) + ";\n");
		builder.append(props.header);
		builder.append("\n");
		builder.append("@Plugin(type = Op.class)\n");
		builder.append("public class " + className + "\n");
		builder.append("        extends\n");
		builder.append("        AbstractUnaryComputerOp<" + srcClass + "," + dstClass + ">\n");
		builder.append("        implements " + getOpDep(namespaceClass) + ", Contingent\n");
		builder.append("{\n\n");
		for (String parameter : props.parameters) {
			builder.append(buildParameter(parameter, props.src1Parameter, props.dstParameter));
		}
		builder.append("    @Override\n");
		builder.append("    public void compute(final " + srcClass + " input, final " + dstClass + " output)\n");
		builder.append("    {\n");
		builder.append("        final CLIJ clij = CLIJ.getInstance();\n\n");

		StringBuilder parametersForCall = new StringBuilder();
		parametersForCall.append("clij");

		int count = 0;
		for (String parameter : props.parameters) {
			String parameterName = getParameterName(parameter);
			if(parameterName.equals(srcName)) parameterName = "input";
			if(parameterName.equals(dstName)) parameterName = "output";
			if (count > 0) {
				parametersForCall.append(", ");
				parametersForCall.append(parameterName);
			}
			count++;
		}
		builder.append("            Kernels." + props.methodName + "(");
		builder.append(parametersForCall.toString());
		builder.append(");\n");
		builder.append("    }\n\n");

		builder.append(buildConformFunction());
		builder.append("}\n");
		return builder.toString();
	}

}

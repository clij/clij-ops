package net.haesleinhuepf.clij.opsgenerator;

import static net.haesleinhuepf.clij.opsgenerator.OpGenerator.*;

public class UnaryFunctionOpBuilder {

	static String buildUnaryFunctionOp(OpProperties props) {

		String className = getUnaryFunctionClassName(getFunctionClassName(props.methodName), props);
		String srcClass = getParameterClass(getParameter(props.parameters, props.src1Parameter));
		String srcName = getParameterName(getParameter(props.parameters, props.src1Parameter));
		String namespaceClass = getClassName(props.namespace);

		StringBuilder builder = new StringBuilder();
		builder.append("package net.haesleinhuepf.clij.ops.generated" + (namespaceClass.isEmpty() ? "" : "." + props.namespace) + ";\n");
		builder.append(props.header);
		builder.append("\n");
		builder.append("@Plugin(type = Op.class)\n");
		builder.append("public class " + className + "\n");
		builder.append("        extends\n");
		builder.append("        AbstractUnaryFunctionOp<" + srcClass + ", " + props.returnType + ">\n");
		builder.append("        implements " + getOpDep(namespaceClass) + ", Contingent\n");
		builder.append("{\n\n");
		for (String parameter : props.parameters) {
			builder.append(buildParameter(parameter, props.src1Parameter));
		}
		builder.append("    @Override\n");
		builder.append("    public " + props.returnType + " calculate(final " + srcClass + " input)\n");
		builder.append("    {\n");
		builder.append("        final CLIJ clij = CLIJ.getInstance();\n\n");

		StringBuilder parametersForCall = new StringBuilder();
		parametersForCall.append("clij");

		int count = 0;
		for (String parameter : props.parameters) {
			String parameterName = getParameterName(parameter);
			if(parameterName.equals(srcName)) parameterName = "input";
			if (count > 0) {
				parametersForCall.append(", ");
				parametersForCall.append(parameterName);
			}
			count++;
		}
		builder.append("            return Kernels." + props.methodName + "(");
		builder.append(parametersForCall.toString());
		builder.append(");\n");
		builder.append("    }\n\n");

		builder.append(buildConformFunction());
		builder.append("}\n");
		return builder.toString();
	}


}

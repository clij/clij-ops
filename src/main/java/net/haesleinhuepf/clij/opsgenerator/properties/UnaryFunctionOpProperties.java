
package net.haesleinhuepf.clij.opsgenerator.properties;

public class UnaryFunctionOpProperties extends FunctionOpProperties {

	public UnaryFunctionOpProperties(String line, String namespace,
		String header)
	{
		super(line, namespace, header);
	}

	@Override
	public String getClassName() {
		String srcClass = getParameterClass(getParameter(parameters,
			src1Parameter));
		return super.getClassName() + srcClass;
	}

	@Override
	public String build() {
		className = getClassName();
		String srcClass = getParameterClass(getParameter(parameters,
			src1Parameter));
		String srcName = getParameterName(getParameter(parameters, src1Parameter));
		String namespaceClass = getClassName(namespace);

		StringBuilder builder = new StringBuilder();
		builder.append(buildHeader());
		builder.append(buildAnnotation());
		builder.append("public class " + className + "\n");
		builder.append("        extends\n");
		builder.append("        AbstractUnaryFunctionOp<" + srcClass + ", " +
			returnType + ">\n");
		builder.append("        implements " + getOpDep(namespaceClass) +
			", Contingent\n");
		builder.append("{\n\n");
		for (String parameter : parameters) {
			builder.append(buildParameter(parameter, src1Parameter));
		}
		builder.append("    @Override\n");
		builder.append("    public " + returnType + " calculate(final " + srcClass +
			" input)\n");
		builder.append("    {\n");
		builder.append("        final CLIJ clij = CLIJ.getInstance();\n\n");

		StringBuilder parametersForCall = new StringBuilder();
		parametersForCall.append("clij");

		int count = 0;
		for (String parameter : parameters) {
			String parameterName = getParameterName(parameter);
			if (parameterName.equals(srcName)) parameterName = "input";
			if (count > 0) {
				parametersForCall.append(", ");
				parametersForCall.append(parameterName);
			}
			count++;
		}
		builder.append("            return Kernels." + methodName + "(");
		builder.append(parametersForCall.toString());
		builder.append(");\n");
		builder.append("    }\n\n");

		builder.append(buildConformMethod());
		builder.append("}\n");
		return builder.toString();
	}
}

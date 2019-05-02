package net.haesleinhuepf.clij.opsgenerator.properties;

public class BinaryHybridCFProperties extends HybridCFProperties {
	public String src2Parameter;

	public BinaryHybridCFProperties(String line, String namespace, String header) {
		super(line, namespace, header);
	}

	@Override
	public String getClassName() {
		String src1Class = getParameterClass(getParameter(parameters, src1Parameter));
        String src2Class = getParameterClass(getParameter(parameters, src2Parameter));
        String dstClass = getParameterClass(getParameter(parameters, dstParameter));
        return super.getClassName() + src1Class + src2Class + dstClass;
	}

	@Override
	public String build() {
		className = getClassName();
		String srcClass = getParameterClass(getParameter(parameters, src1Parameter));
		String src1Class = getParameterClass(getParameter(parameters, src2Parameter));
		String dstClass = getParameterClass(getParameter(parameters, dstParameter));
		String srcName = getParameterName(getParameter(parameters, src1Parameter));
		String src1Name = getParameterName(getParameter(parameters, src2Parameter));
		String dstName = getParameterName(getParameter(parameters, dstParameter));
		String namespaceClass = getClassName(namespace);

		StringBuilder builder = new StringBuilder();
		builder.append(buildHeader());
		builder.append(buildAnnotation());
		builder.append("public class " + className + "\n");
		builder.append("        extends\n");
		builder.append("        AbstractBinaryHybridCF<" + srcClass + ", " + src1Class + ", " + dstClass + ">\n");
		builder.append("        implements " + getOpDep(namespaceClass) + ", Contingent\n");
		builder.append("{\n\n");
		for (String parameter : parameters) {
			builder.append(buildParameter(parameter, src1Parameter, src2Parameter, dstParameter));
		}
		builder.append("    @Override\n");
		builder.append("    public void compute(final " + srcClass + " input1, final " + src1Class + " input2, final " + dstClass + " output)\n");
		builder.append("    {\n");
		builder.append("        final CLIJ clij = CLIJ.getInstance();\n\n");

		StringBuilder parametersForCall = new StringBuilder();
		parametersForCall.append("clij");

		int count = 0;
		for (String parameter : parameters) {
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
		builder.append("        Kernels." + methodName + "(");
		builder.append(parametersForCall.toString());
		builder.append(");\n");
		builder.append("    }\n\n");

		builder.append(buildConformMethod());
		builder.append(buildCreateOutputMethod());
		builder.append("}\n");
		return builder.toString();
	}

	protected String buildCreateOutputMethod() {
		String srcClass = getParameterClass(getParameter(parameters, src1Parameter));
		String src1Class = getParameterClass(getParameter(parameters, src2Parameter));
		String dstClass = getParameterClass(getParameter(parameters, dstParameter));
		StringBuilder builder = new StringBuilder();
		builder.append("    @Override\n");
		builder.append("    public " + dstClass + " createOutput(" + srcClass + " input1, " + src1Class + " input2) {\n");
		builder.append("        CLIJ clij = CLIJ.getInstance();\n");
		builder.append("        return clij.create(input1);\n");
		builder.append("    }\n\n");
		return builder.toString();
	}

}

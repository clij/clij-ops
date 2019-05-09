
package net.haesleinhuepf.clij.opsgenerator.properties;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class OpProperties {

	public String methodName;
	public String className;
	public String namespace;
	public String[] parameters;
	public String header;
	public String src1Parameter;

	public final static Map<String, String> primitiveMap = new HashMap<>();
	static {
		primitiveMap.put("boolean", "Boolean");
		primitiveMap.put("byte", "Byte");
		primitiveMap.put("short", "Short");
		primitiveMap.put("char", "Character");
		primitiveMap.put("int", "Integer");
		primitiveMap.put("long", "Long");
		primitiveMap.put("float", "Float");
		primitiveMap.put("double", "Double");
	}

	public OpProperties(String line, String namespace, String header) {
		methodName = getMethodName(line);
		parameters = getParameters(line);
		this.namespace = namespace;
		this.header = header;
	}

	public static String getMethodName(String line) {
		String[] temp = line.split("\\(");
		temp = temp[0].trim().split(" ");
		return temp[temp.length - 1];
	}

	static String[] getParameters(String line) {
		String[] temp = line.split("\\(");
		String parameters = temp[1].trim();
		parameters = parameters.replace(")", "");
		return parameters.replace("{", "").split(",");
	}

	public static String[] getParameterNames(String line) {
		return getParameterNames(getParameters(line));
	}

	static String[] getParameterNames(String[] parameters) {
		String[] res = new String[parameters.length];
		for (int i = 0; i < res.length; i++) {
			res[i] = getParameterName(parameters[i]);
		}
		return res;
	}

	static String getParameterName(String parameter) {
		return parameter.trim().split(" ")[1];
	}

	static String getParameter(String[] parameters, String srcParameter) {
		List<String> parameterNames = Arrays.asList(getParameterNames(parameters));
		return parameters[parameterNames.indexOf(srcParameter)];
	}

	static String getParameterClass(String parameter) {
		String className = parameter.trim().split(" ")[0];
		if (primitiveMap.containsKey(className)) return primitiveMap.get(className);
		else return className;
	}

	public String getClassName() {
		return getClassName(methodName);
	}

	public static String getClassName(String name) {
		if (name.isEmpty()) return "";
		return "CLIJ_" + name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	public static String getNamespaceName(String methodName) {
		return "CLIJ_" + methodName;
	}

	private String getNamespaceName() {
		return getNamespaceName(methodName);
	}

	static String getOpDep(String namespaceClass) {
		if (namespaceClass.isEmpty()) return "Op";
		return namespaceClass;
	}

	public abstract String build();

	static String buildConformMethod() {
		StringBuilder builder = new StringBuilder();
		builder.append("    @Override\n");
		builder.append("    public boolean conforms() {\n");
		builder.append("        return true;\n");
		builder.append("    }\n\n");
		return builder.toString();
	}

	protected String buildHeader() {
		return "package net.haesleinhuepf.clij.ops.generated" + (getClassName(
			namespace).isEmpty() ? "" : "." + namespace) + ";\n" + header;
	}

	protected String buildAnnotation() {
		return "\n\n@Plugin(type = " + getOpDep(getClassName(namespace)) +
			".class)\n";
	}

	static String buildParameter(String parameter, String... ignore) {
		StringBuilder builder = new StringBuilder();
		String name = getParameterName(parameter);
		if (Arrays.asList(ignore).contains(name) || name.equals("clij")) return "";
		builder.append("    @Parameter\n");
		builder.append("    private " + parameter.trim() + ";\n\n");
		return builder.toString();
	}
}

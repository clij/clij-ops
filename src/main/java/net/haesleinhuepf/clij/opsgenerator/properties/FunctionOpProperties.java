package net.haesleinhuepf.clij.opsgenerator.properties;

public abstract class FunctionOpProperties extends OpProperties {
	public String returnType;

	public FunctionOpProperties(String line, String namespace, String header) {
		super(line, namespace, header);
		returnType = getReturnType(line);
	}

	static String getReturnType(String line) {
		String[] temp = line.split("\\(");
		temp = temp[0].trim().split(" ");
		String className = temp[temp.length - 2];
		if(primitiveMap.containsKey(className)) return primitiveMap.get(className);
		else return className;
	}

	@Override
	public String getClassName() {
		return super.getClassName() + "CLIJF";
	}

}

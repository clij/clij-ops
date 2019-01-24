package net.haesleinhuepf.clij.opsgenerator;

import net.imagej.ops.Op;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static net.haesleinhuepf.clij.opsgenerator.OpGenerator.getClassName;

public class OpInterfaceBuilder {

	static void writeInterfaceClass(String namespace) throws IOException {

		String namespaceClass = getClassName(namespace);

		System.out.println("creating namespace " + namespace);

		File namespaceFolder = new File("/home/random/Development/imagej/project/"
				+ "clij/clij-ops/src/main/java/net/haesleinhuepf/clij/ops/generated/"
				+ namespace + "/");
		namespaceFolder.mkdirs();
		File outputTarget = new File(namespaceFolder.getAbsolutePath() + "/" + namespaceClass + ".java");
		outputTarget.createNewFile();

		FileWriter writer = new FileWriter(outputTarget);
		writer.write(generateNamespace(namespaceClass, namespace));
		writer.close();
	}

	static String generateNamespace(String className, String packageName) {
		StringBuilder builder = new StringBuilder();
		builder.append("package net.haesleinhuepf.clij.ops.generated." + packageName + ";\n\n");
		builder.append("import net.imagej.ops.Op;\n\n");
		builder.append("public interface " + className + " extends Op {\n");
		builder.append("	String NAME = \"" + packageName + "\";\n");
		builder.append("}\n");
		return builder.toString();
	}

}

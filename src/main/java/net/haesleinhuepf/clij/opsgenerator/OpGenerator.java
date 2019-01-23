package net.haesleinhuepf.clij.opsgenerator;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OpGenerator
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 01 2019
 */
public class OpGenerator {


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

    public static void main(String ... args) throws IOException {
//        Kernels.class.get
        File inputFile = new File("/home/random/Development/imagej/project/clij/clij-core/src/main/java/net/haesleinhuepf/clij/kernels/Kernels.java");

        BufferedReader br = new BufferedReader(new FileReader(inputFile));

        StringBuilder header = new StringBuilder();
        header.append("package net.haesleinhuepf.clij.ops;\n");
        header.append("import net.haesleinhuepf.clij.CLIJ;\n");
        header.append("import net.haesleinhuepf.clij.kernels.Kernels;\n");
        header.append("import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;\n");
        header.append("import net.imagej.ops.Contingent;\n");
        header.append("import net.imagej.ops.Op;\n");
        header.append("import net.imagej.ops.special.computer.AbstractUnaryComputerOp;\n");
        header.append("import net.imagej.ops.special.computer.AbstractBinaryComputerOp;\n");
        header.append("import net.imagej.ops.special.function.AbstractUnaryFunctionOp;\n");
        header.append("import net.imglib2.type.numeric.RealType;\n");
        header.append("import org.scijava.plugin.Parameter;\n");
        header.append("import org.scijava.plugin.Plugin;\n");

        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("import")) {
                header.append(line);
                header.append("\n");
            }
            if (line.startsWith("public class")) {
                break;
            }
        }

        header.append("// This is generated code. See src/test/java/net/haesleinhuepf/clij/opsgenerator for details\n");

        int opCreated = 0;
        int opNotCreated = 0;

        while ((line = br.readLine()) != null) {
            line = line.trim();

            if (line.startsWith("public")) {
                if(processKernelFunction(header.toString(), line)) opCreated++;
                else opNotCreated++;
            }
        }

        System.out.println(opCreated + " ops created, " + opNotCreated + " kernel functions ignored");

    }

    private static boolean processKernelFunction(String header, String line) throws IOException {

        String methodName = getMethodName(line);
        String[] parameters = getParameters(line);
        String returnType = getReturnType(line);

        List<String> params = Arrays.asList(getParameterNames(parameters));

        String classContent = "";
        String className = getComputerClassName(methodName);

        if(params.contains("src") && params.contains("dst")) {
            if(params.contains("src1")) {
                classContent = buildBinaryComputerOp(header, methodName, parameters, "src", "src1", "dst");
            }else {
                classContent = buildUnaryComputerOp(header, methodName, parameters, "src", "dst");

            }
        } else if(!params.contains("src") && params.contains("src1") && params.contains("dst")) {
            if(params.contains("src2")) {
                classContent = buildBinaryComputerOp(header, methodName, parameters, "src1", "src2", "dst");
            }else {
                classContent = buildUnaryComputerOp(header, methodName, parameters, "src1", "dst");

            }
        } else if(params.contains("clImage") && params.contains("clReducedImage")) {
            classContent = buildUnaryComputerOp(header, methodName, parameters, "clImage", "clReducedImage");
        } else if(params.contains("src") && params.contains("dst_max")) {
            classContent = buildUnaryComputerOp(header, methodName, parameters, "src", "dst_max");
        } else if(params.contains("input3d") && params.contains("output3d")) {
            classContent = buildUnaryComputerOp(header, methodName, parameters, "input3d", "output3d");
        } else if(params.contains("source1") && params.contains("source2") && params.contains("destination")) {
            classContent = buildBinaryComputerOp(header, methodName, parameters, "source1", "source2", "destination");
        } else if(params.contains("clImage")) {
            classContent = buildUnaryFunctionOp(header, methodName, parameters, "clImage", returnType);
            className = getFunctionClassName(methodName);
        } else if(params.contains("input")) {
            classContent = buildUnaryFunctionOp(header, methodName, parameters, "input", returnType);
            className = getFunctionClassName(methodName);
        }
        boolean success = true;
        if(classContent.isEmpty()) {
            success = false;
           classContent = "// Could not parse kernel function.\n";
            System.out.println("Could not parse kernel function " + methodName);
        }

        File outputTarget = new File("/home/random/Development/imagej/project/clij/clij-ops/src/main/java/net/haesleinhuepf/clij/ops/" + className + ".java");
        outputTarget.createNewFile();

        FileWriter writer = new FileWriter(outputTarget);
        writer.write(classContent);
        writer.close();
        return success;
    }

	private static String buildUnaryFunctionOp(String header, String methodName, String[] parameters, String srcParameter, String outputType) {

		String className = getFunctionClassName(methodName);
		String srcClass = getParameterClass(getParameter(parameters, srcParameter));
		String srcName = getParameterName(getParameter(parameters, srcParameter));

		StringBuilder builder = new StringBuilder();
		builder.append(header);
		builder.append("\n");
		builder.append("@Plugin(type = Op.class)\n");
		builder.append("public class " + className + "\n");
		builder.append("        extends\n");
		builder.append("        AbstractUnaryFunctionOp<" + srcClass + ", " + outputType + ">\n");
		builder.append("        implements Op, Contingent\n");
		builder.append("{\n\n");
		for (String parameter : parameters) {
			builder.append(buildParameter(parameter, srcParameter));
		}
		builder.append("    @Override\n");
		builder.append("    public " + outputType + " calculate(final " + srcClass + " input)\n");
		builder.append("    {\n");
		builder.append("        final CLIJ clij = CLIJ.getInstance();\n\n");

		StringBuilder parametersForCall = new StringBuilder();
		parametersForCall.append("clij");

		int count = 0;
		for (String parameter : parameters) {
			String parameterName = getParameterName(parameter);
			if(parameterName.equals(srcName)) parameterName = "input";
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

		builder.append(buildConformFunction());
		builder.append("}\n");
		return builder.toString();
	}

    private static String buildUnaryComputerOp(String header, String methodName, String[] parameters, String srcParameter, String dstParameter) {

        String className = getComputerClassName(methodName);
        String srcClass = getParameterClass(getParameter(parameters, srcParameter));
        String dstClass = getParameterClass(getParameter(parameters, dstParameter));
        String srcName = getParameterName(getParameter(parameters, srcParameter));
        String dstName = getParameterName(getParameter(parameters, dstParameter));

        StringBuilder builder = new StringBuilder();
        builder.append(header);
        builder.append("\n");
        builder.append("@Plugin(type = Op.class)\n");
        builder.append("public class " + className + "\n");
        builder.append("        extends\n");
        builder.append("        AbstractUnaryComputerOp<" + srcClass + "," + dstClass + ">\n");
        builder.append("        implements Op, Contingent\n");
        builder.append("{\n\n");
        for (String parameter : parameters) {
            builder.append(buildParameter(parameter, srcParameter, dstParameter));
        }
        builder.append("    @Override\n");
        builder.append("    public void compute(final " + srcClass + " input, final " + dstClass + " output)\n");
        builder.append("    {\n");
        builder.append("        final CLIJ clij = CLIJ.getInstance();\n\n");

        StringBuilder parametersForCall = new StringBuilder();
        parametersForCall.append("clij");

        int count = 0;
        for (String parameter : parameters) {
            String parameterName = getParameterName(parameter);
            if(parameterName.equals(srcName)) parameterName = "input";
            if(parameterName.equals(dstName)) parameterName = "output";
            if (count > 0) {
                parametersForCall.append(", ");
                parametersForCall.append(parameterName);
            }
            count++;
        }
        builder.append("            Kernels." + methodName + "(");
        builder.append(parametersForCall.toString());
        builder.append(");\n");
        builder.append("    }\n\n");

        builder.append(buildConformFunction());
        builder.append("}\n");
        return builder.toString();
    }

    private static String getParameter(String[] parameters, String srcParameter) {
        List<String> parameterNames = Arrays.asList(getParameterNames(parameters));
        return parameters[parameterNames.indexOf(srcParameter)];
    }

    private static String buildBinaryComputerOp(String header, String methodName, String[] parameters, String srcParameter, String src1Parameter, String dstParameter) {
        String className = getComputerClassName(methodName);
        String srcClass = getParameterClass(getParameter(parameters, srcParameter));
        String src1Class = getParameterClass(getParameter(parameters, src1Parameter));
        String dstClass = getParameterClass(getParameter(parameters, dstParameter));
        String srcName = getParameterName(getParameter(parameters, srcParameter));
        String src1Name = getParameterName(getParameter(parameters, src1Parameter));
        String dstName = getParameterName(getParameter(parameters, dstParameter));

        StringBuilder builder = new StringBuilder();
        builder.append(header);
        builder.append("\n");
        builder.append("@Plugin(type = Op.class)\n");
        builder.append("public class " + className + "\n");
        builder.append("        extends\n");
        builder.append("        AbstractBinaryComputerOp<" + srcClass + ", " + src1Class + ", " + dstClass + ">\n");
        builder.append("        implements Op, Contingent\n");
        builder.append("{\n\n");
        for (String parameter : parameters) {
            builder.append(buildParameter(parameter, srcParameter, src1Parameter, dstParameter));
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

        builder.append(buildConformFunction());
        builder.append("}\n");
        return builder.toString();
    }

    private static String buildParameter(String parameter, String... ignore) {
        StringBuilder builder = new StringBuilder();
        String name = getParameterName(parameter);
        if(Arrays.asList(ignore).contains(name) || name.equals("clij")) return "";
        builder.append("    @Parameter\n");
        builder.append("    private " + parameter.trim() + ";\n\n");
        return builder.toString();
    }

    private static String buildConformFunction() {
        StringBuilder builder = new StringBuilder();
        builder.append("    @Override\n");
        builder.append("    public boolean conforms() {\n");
        builder.append("        return true;\n");
        builder.append("    }\n\n");
        return builder.toString();
    }

    private static String[] getParameterNames(String[] parameters) {
        String[] res = new String[parameters.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = getParameterName(parameters[i]);
        }
        return res;
    }

    private static String getParameterName(String parameter) {
        return parameter.trim().split(" ")[1];
    }

    private static String getParameterClass(String parameter) {
        String className = parameter.trim().split(" ")[0];
        if(primitiveMap.containsKey(className)) return primitiveMap.get(className);
        else return className;
    }

    private static String getReturnType(String line) {
        String[] temp = line.split("\\(");
        temp = temp[0].trim().split(" ");
        String className = temp[temp.length - 2];
        if(primitiveMap.containsKey(className)) return primitiveMap.get(className);
        else return className;
    }

    private static String getComputerClassName(String methodName) {
        return methodName.substring(0,1).toUpperCase() + methodName.substring(1) + "CLIJC";
    }

    private static String getFunctionClassName(String methodName) {
        return methodName.substring(0,1).toUpperCase() + methodName.substring(1) + "CLIJF";
    }

    private static String getMethodName(String line) {
        String[] temp = line.split("\\(");
        temp = temp[0].trim().split(" ");
        return temp[temp.length - 1];
    }

    private static String[] getParameters(String line) {
        String[] temp = line.split("\\(");
        String parameters = temp[1].trim();
        parameters = parameters.replace(")", "");
        return parameters.replace("{", "").split(",");
    }
}

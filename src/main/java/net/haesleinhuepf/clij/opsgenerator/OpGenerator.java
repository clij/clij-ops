package net.haesleinhuepf.clij.opsgenerator;

import java.io.*;
import java.util.*;

import static net.haesleinhuepf.clij.opsgenerator.BinaryComputerOpBuilder.buildBinaryComputerOp;
import static net.haesleinhuepf.clij.opsgenerator.OpInterfaceBuilder.writeInterfaceClass;
import static net.haesleinhuepf.clij.opsgenerator.UnaryComputerOpBuilder.buildUnaryComputerOp;
import static net.haesleinhuepf.clij.opsgenerator.UnaryFunctionOpBuilder.buildUnaryFunctionOp;

/**
 * OpGenerator
 * <p>
 * <p>
 * <p>
 * Author: @frauzufall
 * 01 2019
 */
public class OpGenerator {

    static String root = "/home/random/Development/imagej/project/clij/clij-ops/src/main/java/net/haesleinhuepf/clij/ops/generated/";

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

        File inputFile = new File("/home/random/Development/imagej/project/clij/clij-core/src/main/java/net/haesleinhuepf/clij/kernels/Kernels.java");

        BufferedReader br = new BufferedReader(new FileReader(inputFile));

        String header = processImports(br);

        int opCreated = 0;
        int opNotCreated = 0;

        List<String> methods = locateMethods(br);
        List<String> methodNames = getMethodNames(methods);
        for (int i = 0; i < methods.size(); i++) {
            String methodName = methodNames.get(i);
            int methodCount = Collections.frequency(methodNames, methodName);
            String namespace = getNamespaceName(methodName);
            writeInterfaceClass(namespace);
            for (int j = 0; j < methodCount; j++) {
                if(processKernelFunction(header, namespace, methods.get(i+j))) opCreated++;
                else opNotCreated++;
            }
            i += methodCount-1;
        }

        System.out.println(methods.size() + " kernel functions available, " + opCreated + " ops created, " + opNotCreated + " kernel functions failed");

    }

    private static List<String> getMethodNames(List<String> functions) {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < functions.size(); i++) {
            res.add(getMethodName(functions.get(i)));
        }
        return res;
    }

    private static String processImports(BufferedReader br) throws IOException {
        StringBuilder header = new StringBuilder();
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
        return header.toString();
    }

    private static List<String> locateMethods(BufferedReader br) throws IOException {
        List<String> res = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("public")) {
                res.add(line);
            }
        }
        res.sort(String::compareTo);
        return res;
    }

    private static boolean processKernelFunction(String header, String namespace, String line) throws IOException {

        OpProperties props = new OpProperties();

        props.methodName = getMethodName(line);
        props.parameters = getParameters(line);
        props.returnType = getReturnType(line);
        props.namespace = namespace;
        props.header = header;

        List<String> params = Arrays.asList(getParameterNames(props.parameters));

        String classContent = "";

        if(params.contains("src") && params.contains("dst")) {
            props.src1Parameter = "src";
            props.dstParameter = "dst";
            if(params.contains("src1")) {
                props.src2Parameter = "src1";
                props.className = getBinaryClassName(getComputerClassName(props.methodName), props);
                classContent = buildBinaryComputerOp(props);
            }else {
                props.className = getUnaryClassName(getComputerClassName(props.methodName), props);
                classContent = buildUnaryComputerOp(props);
            }
        } else if(!params.contains("src") && params.contains("src1") && params.contains("dst")) {
            props.src1Parameter = "src1";
            props.dstParameter = "dst";
            if(params.contains("src2")) {
                props.src2Parameter = "src2";
                props.className = getBinaryClassName(getComputerClassName(props.methodName), props);
                classContent = buildBinaryComputerOp(props);
            }else {
                props.className = getUnaryClassName(getComputerClassName(props.methodName), props);
                classContent = buildUnaryComputerOp(props);
            }
        } else if(params.contains("clImage") && params.contains("clReducedImage")) {
            props.src1Parameter = "clImage";
            props.dstParameter = "clReducedImage";
            props.className = getUnaryClassName(getComputerClassName(props.methodName), props);
            classContent = buildUnaryComputerOp(props);
        } else if(params.contains("src") && params.contains("dst_max")) {
            props.src1Parameter = "src";
            props.dstParameter = "dst_max";
            props.className = getUnaryClassName(getComputerClassName(props.methodName), props);
            classContent = buildUnaryComputerOp(props);
        } else if(params.contains("input3d") && params.contains("output3d")) {
            props.src1Parameter = "input3d";
            props.dstParameter = "output3d";
            props.className = getUnaryClassName(getComputerClassName(props.methodName), props);
            classContent = buildUnaryComputerOp(props);
        } else if(params.contains("source1") && params.contains("source2") && params.contains("destination")) {
            props.src1Parameter = "source1";
            props.src2Parameter = "source2";
            props.dstParameter = "destination";
            props.className = getBinaryClassName(getComputerClassName(props.methodName), props);
            classContent = buildBinaryComputerOp(props);
        } else if(params.contains("clImage")) {
            props.src1Parameter = "clImage";
            props.className = getUnaryFunctionClassName(getFunctionClassName(props.methodName), props);
            classContent = buildUnaryFunctionOp(props);
        } else if(params.contains("input")) {
            props.src1Parameter = "input";
            props.className = getUnaryFunctionClassName(getFunctionClassName(props.methodName), props);
            classContent = buildUnaryFunctionOp(props);
        }
        boolean success = true;
        if(classContent.isEmpty()) {
            success = false;
           classContent = "// Could not parse kernel function.\n";
           props.className = "Failed" + getClassName(props.methodName);
            System.out.println("Could not parse kernel function " + props.methodName);
        }

        File outputDir = new File(root + (namespace.isEmpty() ? "" : namespace + "/"));
        outputDir.mkdirs();
        File outputTarget = new File(outputDir.getAbsolutePath() + "/"
                + props.className + ".java");
        outputTarget.createNewFile();

        FileWriter writer = new FileWriter(outputTarget);
        writer.write(classContent);
        writer.close();
        return success;
    }

    static String getParameter(String[] parameters, String srcParameter) {
        List<String> parameterNames = Arrays.asList(getParameterNames(parameters));
        return parameters[parameterNames.indexOf(srcParameter)];
    }

    static String buildParameter(String parameter, String... ignore) {
        StringBuilder builder = new StringBuilder();
        String name = getParameterName(parameter);
        if(Arrays.asList(ignore).contains(name) || name.equals("clij")) return "";
        builder.append("    @Parameter\n");
        builder.append("    private " + parameter.trim() + ";\n\n");
        return builder.toString();
    }

    static String buildConformFunction() {
        StringBuilder builder = new StringBuilder();
        builder.append("    @Override\n");
        builder.append("    public boolean conforms() {\n");
        builder.append("        return true;\n");
        builder.append("    }\n\n");
        return builder.toString();
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

    static String getParameterClass(String parameter) {
        String className = parameter.trim().split(" ")[0];
        if(primitiveMap.containsKey(className)) return primitiveMap.get(className);
        else return className;
    }

    static String getReturnType(String line) {
        String[] temp = line.split("\\(");
        temp = temp[0].trim().split(" ");
        String className = temp[temp.length - 2];
        if(primitiveMap.containsKey(className)) return primitiveMap.get(className);
        else return className;
    }

    static String getClassName(String methodName) {
        if(methodName.isEmpty()) return "";
        return methodName.substring(0,1).toUpperCase() + methodName.substring(1);
    }

    static String getNamespaceName(String methodName) {
        return methodName + "CLIJ";
    }

    static String getComputerClassName(String methodName) {
        return getClassName(methodName) + "CLIJC";
    }

    static String getFunctionClassName(String methodName) {
        return getClassName(methodName) + "CLIJF";
    }

    static String getUnaryClassName(String className, OpProperties props) {
        String srcClass = getParameterClass(getParameter(props.parameters, props.src1Parameter));
        String dstClass = getParameterClass(getParameter(props.parameters, props.dstParameter));
        return className + srcClass + dstClass;
    }

    static String getUnaryFunctionClassName(String className, OpProperties props) {
        String srcClass = getParameterClass(getParameter(props.parameters, props.src1Parameter));
        return className + srcClass;
    }

    static String getBinaryClassName(String className, OpProperties props) {
        String src1Class = getParameterClass(getParameter(props.parameters, props.src1Parameter));
        String src2Class = getParameterClass(getParameter(props.parameters, props.src2Parameter));
        String dstClass = getParameterClass(getParameter(props.parameters, props.dstParameter));
        return className + src1Class + src2Class + dstClass;
    }

    static String getMethodName(String line) {
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

    static String getOpDep(String namespaceClass) {
        if(namespaceClass.isEmpty()) return "Op";
        return namespaceClass;
    }

}

package net.haesleinhuepf.clij.opsgenerator;

import net.haesleinhuepf.clij.opsgenerator.properties.BinaryHybridCFProperties;
import net.haesleinhuepf.clij.opsgenerator.properties.OpProperties;
import net.haesleinhuepf.clij.opsgenerator.properties.UnaryHybridCFProperties;
import net.haesleinhuepf.clij.opsgenerator.properties.UnaryFunctionOpProperties;
import net.haesleinhuepf.clij.opsgenerator.properties.UnaryHybridCFProperties;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.haesleinhuepf.clij.opsgenerator.OpInterfaceBuilder.writeInterfaceClass;
import static net.haesleinhuepf.clij.opsgenerator.properties.OpProperties.getMethodName;

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

//    public final static Map<String, String> primitiveMap = new HashMap<>();
//    static {
//        primitiveMap.put("boolean", "Boolean");
//        primitiveMap.put("byte", "Byte");
//        primitiveMap.put("short", "Short");
//        primitiveMap.put("char", "Character");
//        primitiveMap.put("int", "Integer");
//        primitiveMap.put("long", "Long");
//        primitiveMap.put("float", "Float");
//        primitiveMap.put("double", "Double");
//    }

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
            String namespace = OpProperties.getNamespaceName(methodName);
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
        header.append("import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;\n");
        header.append("import net.imagej.ops.special.hybrid.AbstractBinaryHybridCF;\n");
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

        List<String> params = Arrays.asList(OpProperties.getParameterNames(line));

        OpProperties props = null;

        if(params.contains("src") && params.contains("dst")) {
            if(params.contains("src1")) {
                props =  new BinaryHybridCFProperties(line, namespace, header);
                props.src1Parameter = "src";
                ((BinaryHybridCFProperties)props).src2Parameter = "src1";
                ((BinaryHybridCFProperties)props).dstParameter = "dst";

            }else {
                props =  new UnaryHybridCFProperties(line, namespace, header);
                props.src1Parameter = "src";
                ((UnaryHybridCFProperties)props).dstParameter = "dst";
            }
        } else if(!params.contains("src") && params.contains("src1") && params.contains("dst")) {
            if(params.contains("src2")) {
                props =  new BinaryHybridCFProperties(line, namespace, header);
                props.src1Parameter = "src1";
                ((BinaryHybridCFProperties)props).src2Parameter = "src2";
                ((BinaryHybridCFProperties)props).dstParameter = "dst";
            }else {
                props =  new UnaryHybridCFProperties(line, namespace, header);
                props.src1Parameter = "src1";
                ((UnaryHybridCFProperties)props).dstParameter = "dst";
            }
        } else if(params.contains("clImage") && params.contains("clReducedImage")) {
            props =  new UnaryHybridCFProperties(line, namespace, header);
            props.src1Parameter = "clImage";
            ((UnaryHybridCFProperties)props).dstParameter = "clReducedImage";
        } else if(params.contains("src") && params.contains("dst_max")) {
            props =  new UnaryHybridCFProperties(line, namespace, header);
            props.src1Parameter = "src";
            ((UnaryHybridCFProperties)props).dstParameter = "dst_max";
        } else if(params.contains("src") && params.contains("dstHistogram")) {
	        props =  new UnaryHybridCFProperties(line, namespace, header);
	        props.src1Parameter = "src";
	        ((UnaryHybridCFProperties)props).dstParameter = "dstHistogram";
        } else if(params.contains("src") && params.contains("dst_min")) {
	        props =  new UnaryHybridCFProperties(line, namespace, header);
	        props.src1Parameter = "src";
	        ((UnaryHybridCFProperties)props).dstParameter = "dst_min";
        } else if(params.contains("input3d") && params.contains("output3d")) {
            props =  new UnaryHybridCFProperties(line, namespace, header);
            props.src1Parameter = "input3d";
            ((UnaryHybridCFProperties)props).dstParameter = "output3d";
        } else if(params.contains("subtrahend") && params.contains("minuend") && params.contains("destination")) {
            props =  new BinaryHybridCFProperties(line, namespace, header);
            props.src1Parameter = "subtrahend";
            ((BinaryHybridCFProperties)props).src2Parameter = "minuend";
            ((BinaryHybridCFProperties)props).dstParameter = "destination";
        } else if(params.contains("source1") && params.contains("source2") && params.contains("destination")) {
            props =  new BinaryHybridCFProperties(line, namespace, header);
            props.src1Parameter = "source1";
            ((BinaryHybridCFProperties)props).src2Parameter = "source2";
            ((BinaryHybridCFProperties)props).dstParameter = "destination";
        } else if(params.contains("clImage")) {
            props =  new UnaryFunctionOpProperties(line, namespace, header);
            props.src1Parameter = "clImage";
        } else if(params.contains("input")) {
            props =  new UnaryFunctionOpProperties(line, namespace, header);
            props.src1Parameter = "input";
        } else if(params.contains("image")) {
            props =  new UnaryFunctionOpProperties(line, namespace, header);
            props.src1Parameter = "image";
        }

        boolean success = true;
        if(props == null) {
            success = false;
            String method = getMethodName(line);
            System.out.println("Could not parse kernel function " + method);
        } else {
            String classContent = props.build();
            File outputDir = new File(root + (namespace.isEmpty() ? "" : namespace + "/"));
            outputDir.mkdirs();
            File outputTarget = new File(outputDir.getAbsolutePath() + "/"
                    + props.className + ".java");
            outputTarget.createNewFile();

            FileWriter writer = new FileWriter(outputTarget);
            writer.write(classContent);
            writer.close();
        }

        return success;
    }


}

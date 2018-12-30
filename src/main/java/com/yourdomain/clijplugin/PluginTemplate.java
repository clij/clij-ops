package com.yourdomain.clijplugin;

import clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * The PluginTemplate serves as starting point for making your own plugin.
 *
 * The name of the class and the name of your plugin method call must be as similar as shown here.
 * Assuming the class is called "PluginTemplate", the method name must be "CLIJ_pluginTemplate". Otherwise, it may not
 * be found be the macro interpreter.
 *
 * Enter the correct name of your method here: -------------
 *                                                         |
 *                                                         |
 * Author: @haesleinhuepf                                  |
 * 12 2018                                                 V
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_pluginTemplate")
public class PluginTemplate extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        // -------------------------------------------------------------------------------------------------------------
        // todo: Build in your own code here. The variable args contains all parameters handed over in the order as
        //      entered in ImageJ macro or in the dialog. Images have type ClearCLBuffer, numbers come as Double.
        //      Use the methods asFloat(args[n]), asInteger(args[n]), asBoolean(args[n]) to convert them properly.

        boolean result = addScalar((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[2]));

        // -------------------------------------------------------------------------------------------------------------
        releaseBuffers(args);
        return result;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // todo: enter your corde here. The argument order may be changed. However, it is recommended to use the same order
    //       as defined in the getParameterHelpText() method to prevent confusion.
    private boolean addScalar(ClearCLBuffer src, ClearCLBuffer dst, Float scalar) {
        HashMap<String, Object> lParameters = new HashMap<>();
        lParameters.put("src", src);
        lParameters.put("scalar", scalar);
        lParameters.put("dst", dst);

        // todo: The location of the .cl file must be relative to the class specified below:
        return clij.execute(PluginTemplate.class, "template.cl", "addScalar_" + src.getDimension() + "d", lParameters);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // todo: enter the list of parameters you need in order to make your algorithm run properly. Use the words
    //       Image, Number, String, Boolean to specify the right types.
    //       Name your output images with a name containing "destination" to make CLIJ generate them automatically and
    //       not prompt the user for it.
    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number scalar";
    }


    // -----------------------------------------------------------------------------------------------------------------
    // todo: enter the documentation for your plugin here:
    @Override
    public String getDescription() {
        return "Detailed description of your plugin.\n" +
                "What happens to the image?" +
                "What do the parameters mean?";
    }

    // -----------------------------------------------------------------------------------------------------------------
    // todo: enter the image dimensionality for which your algorithm might be applied:
    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
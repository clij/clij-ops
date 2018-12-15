package com.yourdomain.clijplugin;

import clearcl.ClearCLBuffer;
import net.haesleinhuepf.imagej.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.imagej.macro.CLIJMacroPlugin;
import net.haesleinhuepf.imagej.macro.CLIJOpenCLProcessor;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * The PluginTemplate serves as starting point for making your own plugin.
 *
 * Enter the correct name of your method here: -------------
 *                                                         |
 *                                                         |
 * Author: @haesleinhuepf                                  |
 * 12 2018                                                 V
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_pluginTemplate")
public class PluginTemplate extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = addScalar((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[2]));
        releaseBuffers(args);
        return result;
    }

    private boolean addScalar(ClearCLBuffer src, ClearCLBuffer dst, Float scalar) {
        HashMap<String, Object> lParameters = new HashMap<>();
        lParameters.put("src", src);
        lParameters.put("scalar", scalar);
        lParameters.put("dst", dst);

        return clij.execute(PluginTemplate.class,
                "template.cl",
                "addScalar_" + src.getDimension() + "d",
                lParameters);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number scalar";
    }
}
# CLIJ example ImageJ Ops jython: blur.py
#
# This script shows how to blur an image in the GPU.
#
# Author: Deborah Schmidt, frauzufall@mpi-cbg.de
# May 2019
# ---------------------------------------------

run("Close All");

// Get test data
run("T1 Head (2.4M, 16-bits)");
input = getTitle();
getDimensions(width, height, channels, slices, frames);

// create an emtpy image to put the blurred pixels in
blurred = "Blurred";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ_clear();

// push images to GPU
Ext.CLIJ_push(input);

// cleanup ImageJ
run("Close All");

// Blur in GPU
Ext.CLIJ_blur3D(input, blurred, 5, 5, 1);

// Get results back from GPU
Ext.CLIJ_pull(blurred);

// Cleanup by the end
Ext.CLIJ_clear();

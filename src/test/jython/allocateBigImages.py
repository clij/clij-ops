# CLIJ example ImageJ Ops jython: backgroundSubtraction.py
#
# This script shows how background subtraction can be done in the GPU.
#
# Author: Deborah Schmidt, frauzufall@mpi-cbg.de
# May 2019
# ---------------------------------------------

#@ IOService io
#@ UIService ui
#@ OpService ops

from net.haesleinhuepf.clij import CLIJ;

# Get test data
input = io.open("https://imagej.nih.gov/ij/images/blobs.gif");
singleChannelInput = ops.transform().hyperSliceView(input, 2, 0);

# Init GPU
clij = CLIJ.getInstance("Intel(R) HD Graphics Kabylake Desktop GT1.5");

# push images to GPU
inputGPU = clij.push(singleChannelInput);

# create an 800 MB image in GPU memory
bigStack = clij.create([2048, 2048, 100], inputGPU.getNativeType());


for i in range(0,10) :
	# fill the image with content
	ops.run("copySliceCLIJ", bigStack, inputGPU, i);

crop = ops.run("cropCLIJ", bigStack, 0, 0, 0, 150, 150, 10);

# Get results back from GPU
result = clij.pull(crop);

ui.show(result);

# report about what's allocated in the GPU memory
#clij.reportMemory();

# Cleanup by the end
clij.close();

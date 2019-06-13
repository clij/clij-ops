# CLIJ example ImageJ Ops jython: backgroundSubtraction.py
#
# This script shows how background subtraction can be done in the GPU.
#
# Author: Robert Haase, rhaase@mpi-cbg.de
# Author: Deborah Schmidt, frauzufall@mpi-cbg.de
# May 2019
# ---------------------------------------------

#@ IOService io
#@ UIService ui
#@ OpService ops

# Get test data
input = io.open("https://bds.mpi-cbg.de/samples/blobs.png")
ui.show("input", input)

# push images to GPU
inputGPU = ops.run("CLIJ_push", input)

# create an 800 MB image in GPU memory
bigStack = ops.run("CLIJ_create", [2048, 2048, 100], inputGPU.getNativeType())


for i in range(0,10) :
	# fill the image with content
	ops.run("CLIJ_copySlice", bigStack, inputGPU, i)

crop = ops.run("CLIJ_crop", bigStack, 0, 0, 0, 150, 150, 10)

# Get results back from GPU
result = ops.run("CLIJ_pull", crop)

ui.show("crop of big image", result)

# report about what's allocated in the GPU memory
#clij.reportMemory();

# Cleanup by the end
ops.run("CLIJ_close", inputGPU)
ops.run("CLIJ_close",bigStack)
ops.run("CLIJ_close",crop)


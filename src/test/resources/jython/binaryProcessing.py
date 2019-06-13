# CLIJ example ImageJ Ops jython: binaryProcessing.py
#
# This script shows how to deal with binary images, e.g. thresholding, dilation, erosion, in the GPU.
#
# Author: Robert Haase, rhaase@mpi-cbg.de
# Author: Deborah Schmidt, frauzufall@mpi-cbg.de
# May 2019
# ---------------------------------------------

#@ IOService io
#@ UIService ui
#@ OpService ops

input = io.open("https://bds.mpi-cbg.de/samples/blobs.png")
ui.show("input", input)
# push image to GPU
inputGPU = ops.run("CLIJ_push", input)

threshold = 128;
mask = ops.run("CLIJ_threshold", inputGPU, threshold)
temp = ops.run("CLIJ_erodeBox", mask)
mask = ops.run("CLIJ_erodeBox", temp)
temp = ops.run("CLIJ_dilateBox", mask)
mask = ops.run("CLIJ_dilateBox", temp)
# show result
result = ops.run("CLIJ_pull", mask)
ui.show("binary processing", result)

#cleanup
inputGPU.close()
mask.close()
temp.close()


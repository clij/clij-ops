# CLIJ example ImageJ Ops jython: binaryProcessing.py
#
# This script shows how to deal with binary images, e.g. thresholding, dilation, erosion, in the GPU.
#
# Author: Deborah Schmidt, frauzufall@mpi-cbg.de
# May 2019
# ---------------------------------------------

#@ IOService io
#@ UIService ui
#@ OpService ops

from net.haesleinhuepf.clij import CLIJ

input = io.open("https://imagej.nih.gov/ij/images/blobs.gif")
# init GPU
clij = CLIJ.getInstance()
# push image to GPU
inputGPU = clij.push(input)

threshold = 128;
mask = ops.run("thresholdCLIJ", inputGPU, threshold)
temp = ops.run("erodeBoxCLIJ", mask)
mask = ops.run("erodeBoxCLIJ", temp)
temp = ops.run("dilateBoxCLIJ", mask)
mask = ops.run("dilateBoxCLIJ", temp)
# show result
result = clij.pull(mask)
ui.show(result)
clij.close()

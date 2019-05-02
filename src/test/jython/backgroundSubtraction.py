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

from net.haesleinhuepf.clij import CLIJ

input = io.open("https://imagej.nih.gov/ij/images/t1-head.gif")
singleChannelInput = ops.transform().hyperSliceView(input, 2, 0)
# init GPU
clij = CLIJ.getInstance()
# push image to GPU
inputGPU = clij.push(singleChannelInput)
# Blur in GPU
background = ops.run("blurCLIJ", inputGPU, 10, 10, 1)
background_subtracted = ops.run("addImagesWeightedCLIJ", inputGPU, background, 1, -1)
maximum_projected = ops.run("maximumZProjectionCLIJ", background_subtracted)

# show result
result = clij.pull(maximum_projected)
ui.show(result)
clij.close()

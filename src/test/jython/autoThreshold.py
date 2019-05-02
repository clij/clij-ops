# CLIJ example ImageJ Ops jython: thresholding.py
#
# This script shows how to apply an automatic threshold method to an image in the GPU.
#
# Author: Deborah Schmidt, frauzufall@mpi-cbg.de
# May 2019
# ---------------------------------------------

#@ IOService io
#@ UIService ui
#@ OpService ops

from net.haesleinhuepf.clij import CLIJ

blobs = io.open("https://imagej.nih.gov/ij/images/blobs.gif")
# init GPU
clij = CLIJ.getInstance()
# push image to GPU
blobsGPU = clij.push(blobs)
# run threshold
maskGPU = ops.run("automaticThresholdCLIJ", blobsGPU, "Otsu")
# show result
result = clij.pull(maskGPU)
ui.show(result)

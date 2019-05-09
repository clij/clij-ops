# CLIJ example ImageJ Ops jython: thresholding.py
#
# This script shows how to apply an automatic threshold method to an image in the GPU.
#
# Author: Robert Haase, rhaase@mpi-cbg.de
# Author: Deborah Schmidt, frauzufall@mpi-cbg.de
# May 2019
# ---------------------------------------------

#@ IOService io
#@ UIService ui
#@ OpService ops

blobs = io.open("https://bds.mpi-cbg.de/samples/blobs.png")
ui.show("input", blobs)
# push image to GPU
blobsGPU = ops.run("CLIJ_push", blobs)
# run threshold
maskGPU = ops.run("CLIJ_automaticThreshold", blobsGPU, "Otsu")
# show result
result = ops.run("CLIJ_pull", maskGPU)
ui.show("auto threshold", result)

#cleanup
ops.run("CLIJ_close", blobsGPU)
ops.run("CLIJ_close", maskGPU)
ops.run("CLIJ_close")
# CLIJ example ImageJ Ops jython: create_object_outlines.py
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

blobs = io.open("https://bds.mpi-cbg.de/samples/blobs.png")
ui.show("input", blobs)
# push images to GPU
blobsGPU = ops.run("CLIJ_push", blobs)
mean2DBox = ops.run("CLIJ_meanBox", blobsGPU, 2.0, 2.0)
threshold = ops.run("CLIJ_threshold", mean2DBox, 127.0)
erodeBox = ops.run("CLIJ_erodeBox", threshold)
binaryXOr = ops.run("CLIJ_binaryXOr", threshold, erodeBox)

# show result
result = ops.run("CLIJ_pull", binaryXOr)
ui.show("object outlines", result)

#cleanup
ops.run("CLIJ_close", blobsGPU)
ops.run("CLIJ_close", mean2DBox)
ops.run("CLIJ_close", threshold)
ops.run("CLIJ_close", erodeBox)
ops.run("CLIJ_close", binaryXOr)
ops.run("CLIJ_close")

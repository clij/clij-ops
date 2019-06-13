# CLIJ example ImageJ Ops jython: convert.py
#
# This script shows how to convert an image in the GPU.
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

blobsFloat = ops.run("CLIJ_convertFloat", blobsGPU)
blobsUInt8 = ops.run("CLIJ_convertUInt16", blobsFloat)
blobsUInt16 = ops.run("CLIJ_convertUInt16", blobsUInt8)

# show result
result = ops.run("CLIJ_pull", blobsUInt16)
ui.show("UInt16", result)

# cleanup
ops.run("CLIJ_close", blobsGPU)
ops.run("CLIJ_close", blobsFloat)
ops.run("CLIJ_close", blobsUInt8)
ops.run("CLIJ_close", blobsUInt16)


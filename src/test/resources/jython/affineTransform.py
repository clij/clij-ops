# CLIJ example ImageJ Ops jython: affineTransform.py
#
# This script shows how to apply an affine transform in the GPU.
#
# Author: Robert Haase, rhaase@mpi-cbg.de
# Author: Deborah Schmidt, frauzufall@mpi-cbg.de
# May 2019
# ---------------------------------------------

#@ IOService io
#@ UIService ui
#@ OpService ops

# Get test data
blobs = io.open("https://bds.mpi-cbg.de/samples/blobs.png")
ui.show("input", blobs)
blobs32 = ops.convert().float32(blobs) # interplation works better with float images

ui.show("orig", blobs)
ui.show("32bit", blobs32)

# push images to GPU
blobsGPU = ops.run("CLIJ_push", blobs32)

transform = "center "
transform = transform + " rotate=45" # degrees
transform = transform + " scaleX=2" # relative zoom factor
transform = transform + " translateY=25" # pixels
transform = transform + " -center"

target = ops.run("CLIJ_affineTransform", blobsGPU, transform)

# show result
result = ops.run("CLIJ_pull", target)
ui.show("affine transform", result)
# cleanup
ops.run("CLIJ_close", blobsGPU)
ops.run("CLIJ_close", target)

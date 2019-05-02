# CLIJ example ImageJ Ops jython: affineTransform.py
#
# This script shows how to apply an affine transform in the GPU.
#
# Author: Deborah Schmidt, frauzufall@mpi-cbg.de
# May 2019
# ---------------------------------------------

#@ IOService io
#@ UIService ui
#@ OpService ops

from net.haesleinhuepf.clij import CLIJ

# Get test data
blobs = io.open("https://imagej.nih.gov/ij/images/blobs.gif")
blobs32 = ops.convert().float32(blobs) # interplation works better with float images

ui.show("orig", blobs)
ui.show("32bit", blobs32)

# init GPU
clij = CLIJ.getInstance()

# push images to GPU
blobsGPU = clij.push(blobs32)

transform = "center "
transform = transform + " rotate=45" # degrees
transform = transform + " scaleX=2" # relative zoom factor
transform = transform + " translateY=25" # pixels
transform = transform + " -center"

target = ops.run("affineTransformCLIJ", blobsGPU, transform)

# show result
result = clij.pull(target)
ui.show(result)
#run("Invert LUT");

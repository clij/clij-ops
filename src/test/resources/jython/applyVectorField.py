# CLIJ example ImageJ Ops jython: applyVectorField.py
#
# This script demonstrates how to apply a vector field to an image in order to transform it non-rigidly
#
# Author: Robert Haase, rhaase@mpi-cbg.de
# Author: Deborah Schmidt, frauzufall@mpi-cbg.de
# May 2019
# ---------------------------------------------

#@ IOService io
#@ UIService ui
#@ OpService ops

from net.imglib2.algorithm.region.hypersphere import HyperSphere

# get test image
blobs = io.open("https://bds.mpi-cbg.de/samples/blobs.png")
ui.show("input", blobs)
blobs32 = ops.convert().float32(blobs)

# create two images describing local shift
shiftX = ops.create().img([256, 254])
shiftX32 = ops.convert().float32(shiftX)
shiftY = ops.create().img([256, 254])
shiftY32 = ops.convert().float32(shiftY)

ra = shiftX32.randomAccess()
ra.setPosition([70, 98])

hyperSphere = HyperSphere(shiftX32, ra, 30)
for value in hyperSphere:
    value.set(25)

shiftX32 = ops.filter().gauss(shiftX32, 15.)

# push images to GPU
blobsGPU = ops.run("CLIJ_push", blobs32)
shiftXGPU = ops.run("CLIJ_push", shiftX32)
shiftYGPU = ops.run("CLIJ_push", shiftY32)
resultStackGPU = ops.run("CLIJ_create", [256, 254, 36], blobsGPU.getNativeType())

for i in range(0, 36) :
    # change the shift from slice to slice
    rotatedShiftX = ops.run("CLIJ_affineTransform", shiftXGPU, "center rotate=" + str(i * 10) + " -center")
    # apply transform
    transformed = ops.run("CLIJ_applyVectorfield", blobsGPU, rotatedShiftX, shiftYGPU)
    # put resulting 2D image in the right plane
    ops.run("CLIJ_copySlice", resultStackGPU, transformed, i)
    ops.run("CLIJ_close", rotatedShiftX)
    ops.run("CLIJ_close", transformed)

# show result
result = ops.run("CLIJ_pull", resultStackGPU)
ui.show("applied vector field", result)
# cleanup
ops.run("CLIJ_close", blobsGPU)
ops.run("CLIJ_close", shiftXGPU)
ops.run("CLIJ_close", shiftYGPU)
ops.run("CLIJ_close", resultStackGPU)


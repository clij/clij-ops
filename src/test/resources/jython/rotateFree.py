# CLIJ example ImageJ Ops jython: rotateFree.py
#
# This script shows how to rotate an image in the GPU.
#
# Author: Robert Haase, rhaase@mpi-cbg.de
# Author: Deborah Schmidt, frauzufall@mpi-cbg.de
# May 2019
# ---------------------------------------------

#@ IOService io
#@ UIService ui
#@ OpService ops

angle_step = 1

blobs = io.open("https://bds.mpi-cbg.de/samples/blobs.png")
ui.show("input", blobs)
blobs32 = ops.convert().float32(blobs)
# reserve the right amount of memory for the result image
target = ops.create().img([blobs.dimension(0), blobs.dimension(1), 360 / angle_step])
target32 = ops.convert().float32(target)
# init GPU
clij = CLIJ.getInstance()
# push images to GPU
blobsGPU = ops.run("CLIJ_push", blobs32)
targetGPU = ops.run("CLIJ_push", target32)
# scale multiple times, append to target
count = 0
for angle in range(0, 360, angle_step) :
    rotated = ops.run("CLIJ_rotate2D", blobsGPU, angle, True)
    # put the rotated image in the right place in the result stack
    ops.run("CLIJ_copySlice", targetGPU, rotated, count)
    ops.run("CLIJ_close", rotated)
    count += 1

# show result
result = ops.run("CLIJ_pull", targetGPU)
ui.show("freely rotated", result)

#cleanup
ops.run("CLIJ_close", blobsGPU)
ops.run("CLIJ_close", targetGPU)
ops.run("CLIJ_close", rotated)


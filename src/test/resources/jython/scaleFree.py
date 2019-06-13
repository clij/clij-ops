# CLIJ example ImageJ Ops jython: scaleFree.py
#
# This script shows how to scale an image in the GPU.
#
# Author: Robert Haase, rhaase@mpi-cbg.de
# Author: Deborah Schmidt, frauzufall@mpi-cbg.de
# May 2019
# ---------------------------------------------

#@ IOService io
#@ UIService ui
#@ OpService ops

zoom_step = 0.03

blobs = io.open("https://bds.mpi-cbg.de/samples/blobs.png")
ui.show("input", blobs)
# push image to GPU
blobsGPU = ops.run("CLIJ_push", blobs)
# scale multiple times, append to target
count = 0
dims = [blobs.dimension(0), blobs.dimension(1), long(1.0 / zoom_step)]
target = ops.run("CLIJ_create", dims, blobsGPU.getNativeType())
for zoom in range(100, 0, int(- zoom_step*100)) :
	zoomed = ops.run("CLIJ_scale", blobsGPU, zoom/100., True)
	# put the zoomed image in the right place in the result stack
	ops.run("CLIJ_copySlice", target, zoomed, count)
	ops.run("CLIJ_close", zoomed)
	count += 1

# show result
result = ops.run("CLIJ_pull", target)
ui.show("freely scaled", result)

#cleanup
ops.run("CLIJ_close", blobsGPU)
ops.run("CLIJ_close", target)


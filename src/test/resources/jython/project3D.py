# CLIJ example ImageJ Ops jython: project3D.py
#
# This macro shows how a 3D projection can be done in the GPU.
#
# Author: Robert Haase, rhaase@mpi-cbg.de
# Author: Deborah Schmidt, frauzufall@mpi-cbg.de
# May 2019
# ---------------------------------------------

#@ IOService io
#@ UIService ui
#@ OpService ops

import os
import inspect
def getResource(file):
    return os.path.dirname(os.path.abspath(inspect.getsourcefile(lambda:0))) + "/" + file

angle_step = 10

input = io.open(getResource("../samples/t1-head.tif"))
ui.show("input", input)
# reserve the right amount of memory for the result image
target = ops.create().img([input.dimension(0), input.dimension(1), 360 / angle_step])
target32 = ops.convert().float32(target)
# init GPU
clij = CLIJ.getInstance()
# push image to GPU
inputGPU = ops.run("CLIJ_push", input)
targetGPU = ops.run("CLIJ_push", target32)

# we need to translate the stack in Z to get some space for the shoulders
# when we rotate the head around the y-axis
translated = ops.run("CLIJ_translate3D", inputGPU, 0, 0, input.dimension(2) / 2)

count = 0
for angle in range(0, 360, angle_step) :
    rotated = ops.run("CLIJ_rotate3D", translated, 0, angle, 0.0, True)
    maxProjected = ops.run("CLIJ_maximumZProjection", rotated)
    # put the rotated image in the right place in the result stack
    ops.run("CLIJ_copySlice", targetGPU, maxProjected, count)
    ops.run("CLIJ_close", rotated)
    ops.run("CLIJ_close", maxProjected)
    count += 1

# show result
result = ops.run("CLIJ_pull", targetGPU)
ui.show("3D projected", result)

#cleanup
ops.run("CLIJ_close", inputGPU)
ops.run("CLIJ_close", targetGPU)
ops.run("CLIJ_close", translated)
ops.run("CLIJ_close")

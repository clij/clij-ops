# CLIJ example ImageJ Ops jython: topHat.py
#
# This script shows how a top-hat filter can be applied in the GPU.
#
# Author: Robert Haase, rhaase@mpi-cbg.de
# Author: Deborah Schmidt, frauzufall@mpi-cbg.de
# May 2019
# ---------------------------------------------

#@ IOService io
#@ UIService ui
#@ OpService ops

radiusXY = 10
radiusZ = 0

import os
import inspect
def getResource(file):
    return os.path.dirname(os.path.abspath(inspect.getsourcefile(lambda:0))) + "/" + file


input = io.open(getResource("../samples/t1-head.tif"))
ui.show("input", input)
# push image to GPU
inputGPU = ops.run("CLIJ_push", input)
median = ops.run("CLIJ_medianSliceBySliceSphere", inputGPU, 1, 1)
temp1 = ops.run("CLIJ_minimumBox", median, radiusXY, radiusXY, radiusZ)
temp2 = ops.run("CLIJ_maximumBox", temp1, radiusXY, radiusXY, radiusZ)
output = ops.run("CLIJ_subtractImages", median, temp2)
# show result
result = ops.run("CLIJ_pull", output)
ui.show("top hat", result)

#cleanup
ops.run("CLIJ_close", inputGPU)
ops.run("CLIJ_close", median)
ops.run("CLIJ_close", temp1)
ops.run("CLIJ_close", temp2)
ops.run("CLIJ_close", output)
ops.run("CLIJ_close")

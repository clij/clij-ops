# CLIJ example ImageJ Ops jython: maximumProjection.py
#
# This macro shows how maximum projection can be done in the GPU.
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


input = io.open(getResource("../samples/t1-head.tif"))
ui.show("input", input)
# push image to GPU
inputGPU = ops.run("CLIJ_push", input)

maximum_projected = ops.run("CLIJ_maximumZProjection", inputGPU)
# show result
result = ops.run("CLIJ_pull", maximum_projected)
ui.show("maximum projection", result)

#cleanup
ops.run("CLIJ_close", inputGPU)
ops.run("CLIJ_close", maximum_projected)


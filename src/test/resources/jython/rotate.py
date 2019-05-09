# CLIJ example ImageJ Ops jython: rotate.py
#
# This macro shows how stacks can be rotated in the GPU.
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

rotated = ops.run("CLIJ_rotateLeft", inputGPU)
# show result
result = ops.run("CLIJ_pull", rotated)
ui.show("rotated", result)

#cleanup
ops.run("CLIJ_close", inputGPU)
ops.run("CLIJ_close", rotated)
ops.run("CLIJ_close")

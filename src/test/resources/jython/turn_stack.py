# CLIJ example ImageJ Ops jython: turn_stack.py
#
# This script shows how to wrangle voxels in 3D in the GPU.
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

resliceLeft = ops.run("resliceLeftCLIJ", inputGPU)
rotateRight = ops.run("CLIJ_rotateRight", resliceLeft)
# show result
result = ops.run("CLIJ_pull", rotateRight)
ui.show("turned stack", result)

#cleanup
ops.run("CLIJ_close", inputGPU)
ops.run("CLIJ_close", resliceLeft)
ops.run("CLIJ_close", rotateRight)
ops.run("CLIJ_close")

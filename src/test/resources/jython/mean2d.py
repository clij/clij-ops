# CLIJ example ImageJ Ops jython: mean.py
#
# This script shows how the mean average filter works in the GPU.
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

mean = ops.run("CLIJ_mean2D", inputGPU, 3);
# show result
result = ops.run("CLIJ_pull", mean)
ui.show("mean2D", result)

#cleanup
ops.run("CLIJ_close", inputGPU)
ops.run("CLIJ_close", mean)
ops.run("CLIJ_close")

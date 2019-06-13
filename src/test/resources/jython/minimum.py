# CLIJ example ImageJ Ops jython: minimum.py
#
# This macro shows how apply a minimum filter in the GPU.
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

minimum = ops.run("CLIJ_minimumBox", inputGPU, 3, 3, 3);
# show result
result = ops.run("CLIJ_pull", minimum)
ui.show("minimum", result)

#cleanup
ops.run("CLIJ_close", inputGPU)
ops.run("CLIJ_close", minimum)


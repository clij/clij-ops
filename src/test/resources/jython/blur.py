# CLIJ example ImageJ Ops jython: blur.py
#
# This script shows how to blur an image in the GPU.
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

blurred = ops.run("CLIJ_blur", inputGPU, 5, 5, 1)
# show result
result = ops.run("CLIJ_pull", blurred)
ui.show("blurred", result)

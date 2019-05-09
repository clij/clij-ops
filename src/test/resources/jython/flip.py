# CLIJ example ImageJ Ops jython: flip.py
#
# This script shows how stacks can be rotated in the GPU.
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

flipped = ops.run("CLIJ_flip", inputGPU, True, False, False)
# show result
result = ops.run("CLIJ_pull", flipped)
ui.show("flipped", result)

#cleanup
ops.run("CLIJ_close", inputGPU)
ops.run("CLIJ_close", flipped)
ops.run("CLIJ_close")

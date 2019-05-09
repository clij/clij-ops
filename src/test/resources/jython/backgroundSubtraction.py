# CLIJ example ImageJ Ops jython: backgroundSubtraction.py
#
# This script shows how background subtraction can be done in the GPU.
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
# Blur in GPU
background = ops.run("CLIJ_blur", inputGPU, 10, 10, 1)
background_subtracted = ops.run("CLIJ_addImagesWeighted", inputGPU, background, 1, -1)
maximum_projected = ops.run("CLIJ_maximumZProjection", background_subtracted)

# show result
result = ops.run("CLIJ_pull", maximum_projected)
ui.show("background subtraction", result)

#cleanup
ops.run("CLIJ_close", inputGPU)
ops.run("CLIJ_close", background)
ops.run("CLIJ_close", background_subtracted)
ops.run("CLIJ_close", maximum_projected)
ops.run("CLIJ_close")

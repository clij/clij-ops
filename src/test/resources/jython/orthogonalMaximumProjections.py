# CLIJ example ImageJ Ops jython: orthogonalMaximumProjections.py
#
# This script shows how maximum-X, -Y and -Z projections can be created using the GPU.
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

downScalingFactorInXY = 0.666; # because the image has slice distance 1.5
downScalingFactorInZ = 1;

downscaled = ops.run("CLIJ_downsample", inputGPU, downScalingFactorInXY, downScalingFactorInXY, downScalingFactorInZ)

maximumProjectionX = ops.run("CLIJ_maximumXYZProjection", downscaled, 2, 1, 0)
maximumProjectionY = ops.run("CLIJ_maximumXYZProjection", downscaled, 2, 0, 1)
maximumProjectionZ = ops.run("CLIJ_maximumXYZProjection", downscaled, 0, 1, 2)

# show results
resultX = ops.run("CLIJ_pull", maximumProjectionX)
resultY = ops.run("CLIJ_pull", maximumProjectionY)
resultZ = ops.run("CLIJ_pull", maximumProjectionZ)
ui.show("Maximum projection in X", resultX)
ui.show("Maximum projection in Y", resultY)
ui.show("Maximum projection in Z", resultZ)

#cleanup
ops.run("CLIJ_close", inputGPU)
ops.run("CLIJ_close", downscaled)
ops.run("CLIJ_close", maximumProjectionX)
ops.run("CLIJ_close", maximumProjectionY)
ops.run("CLIJ_close", maximumProjectionZ)

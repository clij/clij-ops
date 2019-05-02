# CLIJ example ImageJ Ops jython: bitdepthConversion.py
#
# This script shows how change the bit-depth of images in the GPU.
#
# Author: Deborah Schmidt, frauzufall@mpi-cbg.de
# May 2019
# ---------------------------------------------

#@ IOService io
#@ UIService ui
#@ OpService ops

from net.haesleinhuepf.clij import CLIJ

image64 = ops.create().img([20, 20])
image32 = ops.convert().float32(image64)
# TODO how to do that in a script?
#LoopBuilder.setImages(image32).forEachPixel(pixel -> pixel.set(pixel.getIndex()))
# init GPU
clij = CLIJ.getInstance()
# push image to GPU
inputGPU = clij.push(image32)

intensityScalingFactor = 255
temp = ops.run("multiplyImageAndScalarCLIJ", inputGPU, intensityScalingFactor)
image16 = ops.run("convertUInt16CLIJ", temp)
# show result
result = clij.pull(image16)
ui.show(result)
clij.close()

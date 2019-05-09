# CLIJ example ImageJ Ops jython: workflow.py
##
# Author: Robert Haase, rhaase@mpi-cbg.de
# Author: Deborah Schmidt, frauzufall@mpi-cbg.de
# May 2019

#@ Dataset data
#@ UIService ui
#@ OpService ops

smallBlurSigmaInPixels = 2
blurSigmaInPixels = 6
sampleX = 0.52
sampleY = 0.52
sampleZ = 2.0

original = ops.run("CLIJ_push", data)

firstFiltered = ops.run("CLIJ_blur", original, smallBlurSigmaInPixels, smallBlurSigmaInPixels, 0)
secondFiltered = ops.run("CLIJ_blur", original, blurSigmaInPixels, blurSigmaInPixels, 0)
imageDoG = ops.run("CLIJ_addImagesWeighted", firstFiltered, secondFiltered, 1.0, -1.0)
positiveStack = ops.run("CLIJ_maximumImageAndScalar", imageDoG, 1.0)
scaled = ops.run("downsampleCLIJ", positiveStack, sampleX, sampleY, sampleZ)
reslicedFromTop = ops.run("resliceTopCLIJ", scaled)
radialResliced = ops.run("radialProjectionCLIJ", reslicedFromTop, 360, 1.0)
reslicedFromLeft = ops.run("resliceLeftCLIJ", radialResliced)
maxProjected = ops.run("maximumZProjectionCLIJ", reslicedFromLeft)

output = ops.run("CLIJ_pull", maxProjected)
ui.show("workflow result", output)

#cleanup
ops.run("CLIJ_close", original)
ops.run("CLIJ_close", secondFiltered)
ops.run("CLIJ_close", imageDoG)
ops.run("CLIJ_close", positiveStack)
ops.run("CLIJ_close", scaled)
ops.run("CLIJ_close", reslicedFromTop)
ops.run("CLIJ_close", reslicedFromLeft)
ops.run("CLIJ_close", radialResliced)
ops.run("CLIJ_close", maxProjected)
ops.run("CLIJ_close")

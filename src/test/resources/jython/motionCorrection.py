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

from org.scijava.table import DefaultGenericTable
import os
import inspect
def getResource(file):
	return os.path.dirname(os.path.abspath(inspect.getsourcefile(lambda:0))) + "/" + file

# define a threshold to differentiate object and background
threshold = 50

input = io.open(getResource("../samples/motion_correction_Drosophila_DSmanila1.tif"))
ui.show("input", input)
input32 = ops.convert().float32(input)
# push image to GPU
inputGPU = ops.run("CLIJ_push", input)

formerX = 0
formerY = 0

mass = DefaultGenericTable()

# process all slices; only the first stays where it is
for z in range(0, input.dimension(2)):

	slice = ops.run("CLIJ_copySlice", inputGPU, z)

	# determine center of mass
	binary = ops.run("CLIJ_threshold", slice, threshold)
	ops.run("CLIJ_centerOfMass", mass, binary)
	x = mass.get("MassX", mass.getRowCount()-1)
	y = mass.get("MassY", mass.getRowCount()-1)

	if z > 0:

		# determine shift
		deltaX = x - formerX
		deltaY = y - formerY

		# apply translation transformation
		shifted = ops.run("CLIJ_affineTransform", slice, "translatex=" + str(deltaX) + " translatey=" + str(deltaY))

		# copy result back
		ops.run("CLIJ_copySlice", inputGPU, shifted, z)

		ops.run("CLIJ_close", shifted)
	else:
		formerX = x
		formerY = y
	ops.run("CLIJ_close", slice)
	ops.run("CLIJ_close", binary)

ui.show("center of mass per slice", mass)

# show result
result = ops.run("CLIJ_pull", inputGPU)
ui.show("motion correction", result)

#cleanup
ops.run("CLIJ_close", inputGPU)
ops.run("CLIJ_close")



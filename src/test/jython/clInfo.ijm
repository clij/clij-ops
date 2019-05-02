# CLIJ example ImageJ Ops jython: clInfo.py
#
# Outputs information about OpenCL devices
#
# Author: Deborah Schmidt, frauzufall@mpi-cbg.de
# May 2019
# ---------------------------------------------

run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ_clInfo();
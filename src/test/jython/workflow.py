#@ Dataset data
#@ UIService ui
#@ OpService ops

from net.haesleinhuepf.clij import CLIJ;

smallBlurSigmaInPixels = 2;
blurSigmaInPixels = 6;
sampleX = 0.52;
sampleY = 0.52;
sampleZ = 2.0;

#clij = CLIJ.getInstance("Intel(R) HD Graphics Kabylake Desktop GT1.5");
clij = CLIJ.getInstance();

original = clij.push(data);

firstFiltered = ops.run("blurCLIJ", original, smallBlurSigmaInPixels, smallBlurSigmaInPixels, 0);
secondFiltered = ops.run("blurCLIJ", original, blurSigmaInPixels, blurSigmaInPixels, 0);
imageDoG = ops.run("addImagesWeightedCLIJ", firstFiltered, secondFiltered, 1.0, -1.0);
positiveStack = ops.run("maximumImageAndScalarCLIJ", imageDoG, 1.0);
scaled = ops.run("downsampleCLIJ", positiveStack, sampleX, sampleY, sampleZ);
reslicedFromTop = ops.run("resliceTopCLIJ", scaled);
radialResliced = ops.run("radialProjectionCLIJ", reslicedFromTop, 360, 1.0);
reslicedFromLeft = ops.run("resliceLeftCLIJ", radialResliced);
maxProjected = ops.run("maximumZProjectionCLIJ", reslicedFromLeft);

output = clij.pull(maxProjected);

ui.show(output);

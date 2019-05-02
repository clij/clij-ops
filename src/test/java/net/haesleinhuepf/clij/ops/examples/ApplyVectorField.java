package net.haesleinhuepf.clij.ops.examples;

import ij.ImagePlus;
import ij.gui.Overlay;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.imagej.ImageJ;
import net.imagej.roi.DefaultROITree;
import net.imagej.roi.ROITree;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.roi.MaskPredicate;
import net.imglib2.roi.geom.GeomMasks;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import org.python.bouncycastle.math.raw.Nat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static ij.IJ.makeOval;
import static ij.IJ.newImage;

public class ApplyVectorField {

	public void run() throws IOException {
		ImageJ ij = new ImageJ();
		Img blobs = (Img) ij.io().open("https://imagej.nih.gov/ij/images/blobs.gif");
		Img singleChannelInput = (Img) ij.op().transform().hyperSliceView(blobs, 2, 0);
		Img blobs32 = ij.op().convert().float32(singleChannelInput);
		// init GPU
		CLIJ clij = CLIJ.getInstance();
		// push images to GPU
		ClearCLBuffer blobsGPU = clij.convert(blobs32, ClearCLBuffer.class);

		// create two images describing local shift
		Img shiftX = ij.op().create().img(new long[]{256, 254, 1});
		Img shiftY = ij.op().create().img(new long[]{256, 254, 1});
// reserve memory for the result video
		Img resultStack = ij.op().create().img(new long[]{256, 254, 36});

		// create rois
		List<MaskPredicate<?>> rois = Arrays.asList(
				GeomMasks.closedEllipsoid(new double[]{20, 98}, new double[]{72, 68})
		);
		ROITree roiTree = new DefaultROITree();
		roiTree.addROIs( rois );
		Overlay overlay = ij.convert().convert( roiTree, Overlay.class );
		ImagePlus imagePlus = ImageJFunctions.wrap( shiftX, "bridge" );
		imagePlus.setOverlay( overlay );

		//TODO not finished
//		ij.op().math().add(imagePlus, imagePlus.getImage().);

//		run("Add...", "value=25");
//		run("Select None");
//		run("Gaussian Blur...", "sigma=15");
//		run("Enhance Contrast", "saturated=0.35");
//
//// init GPU
//		run("CLIJ Macro Extensions", "cl_device=");
//		Ext.CLIJ_push("blobs");
//		Ext.CLIJ_push("shiftX");
//		Ext.CLIJ_push("shiftY");
//		Ext.CLIJ_push("resultStack");
//
//		for (i = 0; i < 36; i++) {
//
//			// change the shift from slice to slice
//			Ext.CLIJ_affineTransform("shiftX", "rotatedShiftX", "center rotate=" + (i * 10) + " -center");
//
//			// apply transform
//			Ext.CLIJ_applyVectorField2D("blobs", "rotatedShiftX", "shiftY", "transformed");
//
//			// put resulting 2D image in the right plane
//			Ext.CLIJ_copySlice("transformed", "resultStack", i);
//		}
//
//
//// get result back from GPU
//		Ext.CLIJ_pull("resultStack");
//		run("Invert LUT");
//
//
//		// show result
//		RandomAccessibleInterval result = clij.convert(target, RandomAccessibleInterval.class);
//		ij.ui().show(result);
	}

	public static void main(String ... args) throws IOException {
		ApplyVectorField task = new ApplyVectorField();
		task.run();
	}
}

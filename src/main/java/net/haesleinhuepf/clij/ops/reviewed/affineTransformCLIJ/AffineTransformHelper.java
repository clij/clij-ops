package net.haesleinhuepf.clij.ops.reviewed.affineTransformCLIJ;

import net.imglib2.realtransform.AffineTransform3D;

public class AffineTransformHelper {

	public static float[] transformStringToMatrix(String transform, float inputWidth, float inputHeight, float inputDepth) {
		AffineTransform3D at = new AffineTransform3D();
		String[] transformCommands = transform.trim().toLowerCase().split(" ");

		for(String transformCommand : transformCommands) {
			String[] commandParts = transformCommand.split("=");
			//System.out.print("Command: " + commandParts[0]);
			if (commandParts[0].compareTo("center") == 0) {
				at.translate(-inputWidth / 2, -inputHeight / 2, -inputDepth / 2);
			} else if (commandParts[0].compareTo("-center") == 0) {
				at.translate(inputWidth / 2, inputHeight / 2, inputDepth / 2);
			} else if (commandParts[0].compareTo("scale") == 0) {
				at.scale(1.0 / Double.parseDouble(commandParts[1]));
			} else if (commandParts[0].compareTo("scalex") == 0) {
				AffineTransform3D scaleTransform = new AffineTransform3D();
				scaleTransform.set(1.0 / Double.parseDouble(commandParts[1]),0,0);
				scaleTransform.set(1.0 , 1, 1);
				scaleTransform.set(1, 2, 2);
				at.concatenate(scaleTransform);
			} else if (commandParts[0].compareTo("scaley") == 0) {
				AffineTransform3D scaleTransform = new AffineTransform3D();
				scaleTransform.set(1.0,0,0);
				scaleTransform.set(1.0  / Double.parseDouble(commandParts[1]) , 1, 1);
				scaleTransform.set(1, 2, 2);
				at.concatenate(scaleTransform);
			} else if (commandParts[0].compareTo("scalez") == 0) {
				AffineTransform3D scaleTransform = new AffineTransform3D();
				scaleTransform.set(1.0,0,0);
				scaleTransform.set(1.0 , 1, 1);
				scaleTransform.set(1.0  / Double.parseDouble(commandParts[1]) , 2, 2);
				at.concatenate(scaleTransform);
			} else if (commandParts[0].compareTo("rotatex") == 0) {
				float angle = (float)(-Float.valueOf(commandParts[1]) / 180.0f * Math.PI);
				at.rotate(0, angle);
			} else if (commandParts[0].compareTo("rotatey") == 0) {
				float angle = (float)(-Float.valueOf(commandParts[1]) / 180.0f * Math.PI);
				at.rotate(1, angle);
			} else if (commandParts[0].compareTo("rotatez") == 0 || commandParts[0].compareTo("rotate") == 0) {
				float angle = (float)(-Float.valueOf(commandParts[1]) / 180.0f * Math.PI);
				at.rotate(2, angle);
			} else if (commandParts[0].compareTo("translatex") == 0) {
				at.translate(Double.parseDouble(commandParts[1]), 0, 0);
			} else if (commandParts[0].compareTo("translatey") == 0) {
				at.translate(0,Double.parseDouble(commandParts[1]), 0);
			} else if (commandParts[0].compareTo("translatez") == 0) {
				at.translate(0, 0, Double.parseDouble(commandParts[1]));
			} else if (commandParts[0].compareTo("shearxy") == 0) {
				double shear = Double.parseDouble(commandParts[1]);
				AffineTransform3D shearTransform = new AffineTransform3D();
				shearTransform.set(1.0, 0, 0 );
				shearTransform.set(1.0, 1, 1 );
				shearTransform.set(1.0, 2, 2 );
				shearTransform.set(shear, 0, 1);
				//shearTransform.set(shear, 0, 2);
				at.concatenate(shearTransform);
			} else if (commandParts[0].compareTo("shearxz") == 0) {
				double shear = Double.parseDouble(commandParts[1]);
				AffineTransform3D shearTransform = new AffineTransform3D();
				shearTransform.set(1.0, 0, 0 );
				shearTransform.set(1.0, 1, 1 );
				shearTransform.set(1.0, 2, 2 );
				shearTransform.set(shear, 0, 2);
				at.concatenate(shearTransform);
			} else if (commandParts[0].compareTo("shearyx") == 0) {
				double shear = Double.parseDouble(commandParts[1]);
				AffineTransform3D shearTransform = new AffineTransform3D();
				shearTransform.set(1.0, 0, 0 );
				shearTransform.set(1.0, 1, 1 );
				shearTransform.set(1.0, 2, 2 );
				shearTransform.set(shear, 1, 0);
				at.concatenate(shearTransform);
			} else if (commandParts[0].compareTo("shearyz") == 0) {
				double shear = Double.parseDouble(commandParts[1]);
				AffineTransform3D shearTransform = new AffineTransform3D();
				shearTransform.set(1.0, 0, 0 );
				shearTransform.set(1.0, 1, 1 );
				shearTransform.set(1.0, 2, 2 );
				shearTransform.set(shear, 1, 2);
				at.concatenate(shearTransform);
			} else if (commandParts[0].compareTo("shearzx") == 0) {
				double shear = Double.parseDouble(commandParts[1]);
				AffineTransform3D shearTransform = new AffineTransform3D();
				shearTransform.set(1.0, 0, 0 );
				shearTransform.set(1.0, 1, 1 );
				shearTransform.set(1.0, 2, 2 );
				shearTransform.set(shear, 2, 0);
				at.concatenate(shearTransform);
			} else if (commandParts[0].compareTo("shearzy") == 0) {
				double shear = Double.parseDouble(commandParts[1]);
				AffineTransform3D shearTransform = new AffineTransform3D();
				shearTransform.set(1.0, 0, 0 );
				shearTransform.set(1.0, 1, 1 );
				shearTransform.set(1.0, 2, 2 );
				shearTransform.set(shear, 2, 1);
				at.concatenate(shearTransform);
			} else {
				System.out.print("Unknown transform: " + commandParts[0]);
			}
		}

		return net.haesleinhuepf.clij.utilities.AffineTransform.matrixToFloatArray(at);
	}

}

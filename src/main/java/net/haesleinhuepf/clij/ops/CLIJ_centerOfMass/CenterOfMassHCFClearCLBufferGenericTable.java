
package net.haesleinhuepf.clij.ops.CLIJ_centerOfMass;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.table.DefaultGenericTable;
import org.scijava.table.GenericTable;

import net.haesleinhuepf.clij.CLIJService;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;

/**
 * @author haesleinhuepf
 * @author frauzufall
 */

@Plugin(type = CLIJ_centerOfMass.class)
public class CenterOfMassHCFClearCLBufferGenericTable extends
	AbstractUnaryHybridCF<ClearCLBuffer, GenericTable> implements
	CLIJ_centerOfMass
{

	@Parameter
	CLIJService clij;

	@Override
	public void compute(ClearCLBuffer input, GenericTable output) {

		if (output.getColumnIndex("MassX") < 0) {
			output.appendColumn("MassX");
		}
		if (output.getColumnIndex("MassY") < 0) {
			output.appendColumn("MassY");
		}

		output.appendRow();

		int index = output.getRowCount() - 1;

		ClearCLBuffer multipliedWithCoordinate = clij.get().create(input
			.getDimensions(), NativeTypeEnum.Float);
		double sum = clij.get().op().sumPixels(input);

		// X:
		clij.get().op().multiplyImageAndCoordinate(input, multipliedWithCoordinate,
			0);
		double sumX = clij.get().op().sumPixels(multipliedWithCoordinate);
		output.set("MassX", index, sumX / sum);

		// Y:
		clij.get().op().multiplyImageAndCoordinate(input, multipliedWithCoordinate,
			1);
		double sumY = clij.get().op().sumPixels(multipliedWithCoordinate);
		output.set("MassY", index, sumY / sum);

		// Z:
		if (input.getDimension() > 2 && input.getDepth() > 1) {
			if (output.getColumnIndex("MassZ") < 0) {
				output.appendColumn("MassZ");
			}
			clij.get().op().multiplyImageAndCoordinate(input,
				multipliedWithCoordinate, 2);
			double sumZ = clij.get().op().sumPixels(multipliedWithCoordinate);
			output.set("MassZ", index, sumZ / sum);
		}

		multipliedWithCoordinate.close();

	}

	@Override
	public GenericTable createOutput(ClearCLBuffer input) {
		return new DefaultGenericTable();
	}
}

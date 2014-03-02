/*
 * Copyright 2007 Sun Microsystems, Inc.
 *
 * This file is part of jVoiceBridge.
 *
 * jVoiceBridge is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation and distributed hereunder
 * to you.
 *
 * jVoiceBridge is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Sun designates this particular file as subject to the "Classpath"
 * exception as provided by Sun in the License file that accompanied this
 * code.
 */

package filters;

import util.HistoryBuffer;
import util.Window;

/**
 * This class is used for debugging to dump arrays of bytes.
 */
public class Util {
	public static double getAverageLocalEnergy(
			final HistoryBuffer soundEnergyHistoryBuffer) {
		if (soundEnergyHistoryBuffer.getElementCount() == 0) {
			return 0;
		}
		double totalEnergyOfHistoryBuffer = 0;
		for (final double energy : soundEnergyHistoryBuffer) {
			totalEnergyOfHistoryBuffer = totalEnergyOfHistoryBuffer + energy;
		}
		return Math.round(totalEnergyOfHistoryBuffer
				/ soundEnergyHistoryBuffer.getElementCount());
	}

	public static double getInstantSoundEnergy(final double[] b, final int off,
			final int len) {
		double instantSoundEnergy = 0;
		for (int i = off; i < (off + len); i++) {
			instantSoundEnergy = instantSoundEnergy
					+ ((Math.abs(b[i]) + 1) * (Math.abs(b[i]) + 1));
		}
		System.err.println("off=" + off + ", len=" + len
				+ ", instantSoundEnergy=" + instantSoundEnergy);
		return Math.round(instantSoundEnergy);
	}

	public static double getInstantSoundEnergy(final Window w) {
		double instantSoundEnergy = 0;
		for (final Double d : w) {
			instantSoundEnergy = instantSoundEnergy + (d * d);
		}
		return Math.round(instantSoundEnergy);
	}

	public static double getLinearRegression(final double variance) {
		return Math.round((1.5 * variance) + 1);
	}

	public static double getVariance(
			final HistoryBuffer soundEnergyHistoryBuffer,
			final double averageLocalEnergy) {
		if (soundEnergyHistoryBuffer.getElementCount() == 0) {
			return averageLocalEnergy;
		}
		double variance = 0;
		for (final double d : soundEnergyHistoryBuffer) {
			variance = variance + Math.pow((d - averageLocalEnergy), 2);
		}
		return Math
				.round(variance / soundEnergyHistoryBuffer.getElementCount());
	}

	private Util() {
	}
}

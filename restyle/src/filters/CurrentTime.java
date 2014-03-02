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

import java.lang.reflect.Method;

public class CurrentTime {

	/*
	 * Return System.nanoTime() if running java 1.5 Otherwise return
	 * System.currentTimeMillis()
	 */
	private static Method currentTime = null;

	private static Class systemClass = null;
	private static int timeUnitsPerSecond = 0;

	public static double getElapsedSeconds(final long start) {
		final double elapsed = getTime() - start;

		return elapsed / timeUnitsPerSecond;
	}

	public static long getTime() {
		if (currentTime == null) {
			try {
				systemClass = Class.forName("java.lang.System");
			} catch (final Exception e) {
				return System.currentTimeMillis(); // something is wrong
			}

			final Method[] methods = systemClass.getMethods();

			Method currentTimeMillis = null;

			for (int i = 0; i < methods.length; i++) {
				final Method m = methods[i];

				if (m.getName().equals("currentTimeMillis")) {
					currentTimeMillis = m;
					timeUnitsPerSecond = 1000;
				} else if (m.getName().equals("nanoTime")) {
					timeUnitsPerSecond = 1000000000;
					currentTime = m;
				}
			}

			if (currentTime == null) {
				currentTime = currentTimeMillis;
			}

			if (currentTime == null) {
				return System.currentTimeMillis();
			}
		}

		try {
			final Long now = (Long) currentTime.invoke(systemClass,
					(Object[]) null);

			return now.longValue();
		} catch (final Exception e) {
			return System.currentTimeMillis();
		}
	}

	public static String getTimeUnits() {
		CurrentTime.getTime();

		String s = "milliseconds";

		if (timeUnitsPerSecond != 1000) {
			s = "nanoseconds";
		}

		return s;
	}

	public static int getTimeUnitsPerSecond() {
		if (timeUnitsPerSecond == 0) {
			getTime();
		}

		return timeUnitsPerSecond;
	}

	public static void main(final String[] args) {
		CurrentTime.getTime();

		int n = 100000;

		if (args.length > 0) {
			try {
				n = Integer.parseInt(args[0]);
			} catch (final NumberFormatException e) {
				System.out.println("invalid count specified.  defaulting to "
						+ n);
			}
		}

		long totalTime = 0;

		final long start = CurrentTime.getTime();

		final long s = System.currentTimeMillis();

		for (int i = 0; i < n; i++) {
			final long begin = CurrentTime.getTime();

			totalTime += (CurrentTime.getTime() - begin);
		}

		try {
			Thread.sleep(20);
		} catch (final InterruptedException e) {
		}

		System.out.println("Time units:  " + getTimeUnits());
		System.out.println("Average time to getTime():  "
				+ ((double) totalTime / n / getTimeUnitsPerSecond()));

		System.out.println("elapsed using getTime() "
				+ CurrentTime.getElapsedSeconds(start));

		System.out.println("elapsed using currentTimeMillis() "
				+ (System.currentTimeMillis() - s));
	}

	private CurrentTime() {
	}

}
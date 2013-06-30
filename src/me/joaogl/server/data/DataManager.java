/**
 * This file is part of ServerSideAcars.

    ServerSideAcars is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ServerSideAcars is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ServerSideAcars.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.joaogl.server.data;

import me.joaogl.server.aircraftmanager.AircraftManager;

public class DataManager {

	public static int getTotalAircraft() {
		return 4;
	}

	public static int getErrorvalue() {
		return (getTotalAircraft() + 1);
	}

	public static void setupRegList() {
		ServerLogger.println("Setting up the aircraft list.");
		AircraftManager.removeAll();
		for (int i = 0; i < DataManager.getTotalAircraft(); i++) {
			// SQL DATA INTO THE ARRAY
			// SQL DATA OF LIFE INTO THE ARRY TOO
			if (i == 0) AircraftManager.setId(i, "A", 10, 50);
			if (i == 1) AircraftManager.setId(i, "B", 50, 10);
			if (i == 2) AircraftManager.setId(i, "C", 60, 20);
			if (i == 3) AircraftManager.setId(i, "D", 100, 30);
		}
		ServerLogger.println("Done setting up the data planes, server up and ready.");
	}

	public static void setIntoDB(String where, String var, String content) {
		// SQL SET VAR = CONTENT WHERE where = VAR
	}

	public static boolean getPilot(String id, String pw) {
		// get DATA

		// BEBUG PURPOSES
		if (id.equalsIgnoreCase("tony")) {
			if (pw.equals("tufiste")) return true;
		}
		if (id.equalsIgnoreCase("joaogl")) {
			if (pw.equals("joaogl")) return true;
		}
		return false;
	}
}

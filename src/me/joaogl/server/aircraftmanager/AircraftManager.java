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


package me.joaogl.server.aircraftmanager;

import java.util.ArrayList;

import me.joaogl.server.data.DataManager;

public class AircraftManager {

	public static ArrayList<String> aircraftReg = new ArrayList<String>();
	public static ArrayList<Integer> aircraftLife = new ArrayList<Integer>();
	public static ArrayList<Integer> aircraftFuel = new ArrayList<Integer>();
	private static int deleted = 0;

	public static void setId(int id, String reg, int life, int fuel) {
		System.out.println(" - Setting aircraft " + reg + " with the id " + id + ". Aircraft status, condition " + life + " fuel available " + fuel + ".");
		aircraftReg.add(reg);
		aircraftLife.add(life);
		aircraftFuel.add(fuel);
	}

	public static void removeId(int id) {
		System.out.println("Removing id " + id);
		aircraftReg.remove(id);
		aircraftLife.remove(id);
		aircraftFuel.remove(id);
		deleted++;
	}

	public static void removeAll() {
		System.out.println("Removing all aircraft data.");
		aircraftReg.removeAll(aircraftReg);
		aircraftFuel.removeAll(aircraftFuel);
		aircraftLife.removeAll(aircraftLife);
		deleted = 0;
	}

	public static void removeId(String reg) {
		for (int i = 0; i < aircraftReg.size(); i++)
			if (aircraftReg.get(i) == reg) {
				aircraftReg.remove(i);
				aircraftLife.remove(i);
				aircraftFuel.remove(i);
				deleted++;
			}
	}

	public static int getTotal() {
		return (aircraftReg.size() - deleted);
	}

	public static int getId(String reg) {
		for (int i = 0; i < aircraftReg.size(); i++)
			if (aircraftReg.get(i).equalsIgnoreCase(reg)) return i;
		return DataManager.getErrorvalue();
	}

	public static String getReg(int id) {
		if (id > getTotal()) return null;
		return aircraftReg.get(id);
	}

	public static int getLife(int id) {
		if (id > getTotal()) return DataManager.getErrorvalue();
		return aircraftLife.get(id);
	}

	public static int getFuel(int id) {
		if (id > getTotal()) return DataManager.getErrorvalue();
		return aircraftFuel.get(id);
	}

	public static String getAllPlanes(int id) {
		return " - Aircraft information " + aircraftReg.get(id) + " with the id " + id + ". Aircraft status, condition " + aircraftLife.get(id) + " fuel available " + aircraftFuel.get(id) + ".";
	}

}

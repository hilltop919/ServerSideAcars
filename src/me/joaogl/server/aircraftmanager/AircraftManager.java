package me.joaogl.server.aircraftmanager;

import java.util.ArrayList;

import me.joaogl.server.DataManager;

public class AircraftManager {

	public static ArrayList<String> aircraftReg = new ArrayList<String>();
	public static ArrayList<Integer> aircraftLife = new ArrayList<Integer>();
	public static ArrayList<Integer> aircraftFuel = new ArrayList<Integer>();
	private static int deleted = 0;

	public static void setId(int id, String reg, int life, int fuel) {
		System.out.println("  - Setting plane " + reg + " with the id " + id + ". Aircraft status, condition " + life + " fuel available " + fuel + ".");
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
		System.out.println("Removing all Planes data.");
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
			if (aircraftReg.get(i) == reg) return i;
		return 0;
	}

	public static String getReg(int id) {
		return aircraftReg.get(id);
	}

	public static int getLife(int id) {
		return aircraftLife.get(id);
	}

	public static int getFuel(int id) {
		return aircraftFuel.get(id);
	}

	public static String getAllPlanes(int id) {
		return " - Aircraft information " + aircraftReg.get(id) + " with the id " + id + ". Aircraft status, condition " + aircraftLife.get(id) + " fuel available " + aircraftFuel.get(id) + ".";
	}

}

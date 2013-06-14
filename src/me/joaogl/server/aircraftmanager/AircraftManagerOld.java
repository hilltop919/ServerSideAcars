package me.joaogl.server.aircraftmanager;

import me.joaogl.server.DataManager;

public class AircraftManagerOld {

	public static String[] aircraftReg;
	public static int[] aircraftLife;
	public static int[] aircraftFuel;

	public AircraftManagerOld(){
		aircraftReg = new String[DataManager.getTotalAircraft()];
		aircraftLife = new int[DataManager.getTotalAircraft()];
		aircraftFuel = new int[DataManager.getTotalAircraft()];
		System.out.println("  - Done.");
	}
	
	public static void setId(int id, String reg, int life, int fuel) {
		System.out.println("  - Setting plane " + reg + " with the id " + id + ". Aircraft status, condition " + life + " fuel available " + fuel + ".");
		aircraftReg[id] = reg;
		aircraftLife[id] = life;
		aircraftFuel[id] = fuel;
	}

	public static void removeId(int id) {
		aircraftReg[id] = null;
	}

	public static void removeId(String reg) {
		for (int i = 0; i < aircraftReg.length; i++)
			if (aircraftReg[i] == reg) aircraftReg[i] = null;
	}

	public static int getId(String reg) {
		for (int i = 0; i < aircraftReg.length; i++)
			if (aircraftReg[i] == reg) return i;
		return 0;
	}

	public static String getReg(int id) {
		return aircraftReg[id];
	}

	public static int getFreeId() {
		for (int i = 0; i < aircraftReg.length; i++)
			if (aircraftReg[i] == null) return i;
		return DataManager.getErrorvalue();
	}

	public static String getAllPlanes(int id) {
		return " - Aircraft information " + aircraftReg[id] + " with the id " + id + ". Aircraft status, condition " + aircraftLife[id] + " fuel available " + aircraftFuel[id] + ".";
	}

}

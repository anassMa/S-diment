package Model;

public class Configuration {
	 private static Double longueur = 90.0 ;
	 private static Double Temperature = 22.0;
	 private final static Double a0 = 1.9347288;
	 private final static Double a1 = 0.08229381;
	 private final static Double a2 = 0.00229451;

	public static Double getLongueur() {
		return longueur;
	}
	public static void setLongueur(Double longueur) {
		Configuration.longueur = longueur;
	}
	public static Double getA0() {
		return a0;
	}
	public static Double getA1() {
		return a1;
	}
	public static Double getA2() {
		return a2;
	}
	public static Double getTemperature() {
		return Temperature;
	}
	public static void setTemperature(Double temperature) {
		Temperature = temperature;
	}


}

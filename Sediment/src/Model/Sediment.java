package Model;

import java.util.List;
import java.util.Map;



public class Sediment {


	Map<Integer,Double> mySediment ;
	Double poidsTotal;
	Integer NbInstants;
	List<Double> frequencesPonderales ;
	List<Double> frequencesPonderalesCumule ;
	List<Double> Vitesse ;
	List<Double> diametre ;



	public List<Double> getFrequencesPonderalesCumule() {
		return frequencesPonderalesCumule;
	}

	public void setFrequencesPonderalesCumule(List<Double> frequencesPonderalesCumule) {
		this.frequencesPonderalesCumule = frequencesPonderalesCumule;
	}

	public List<Double> getVitesse() {
		return Vitesse;
	}

	public void setVitesse(List<Double> vitesse) {
		Vitesse = vitesse;
	}

	public List<Double> getDiametre() {
		return diametre;
	}

	public void setDiametre(List<Double> diametre) {
		this.diametre = diametre;
	}


	public Map<Integer, Double> getMySediment() {
		return mySediment;
	}

	public void setMySediment(Map<Integer, Double> mySediment) {
		this.mySediment = mySediment;
	}

	public Double getPoidsTotal() {
		return poidsTotal;
	}

	public void setPoidsTotal(Double poidsTotal) {
		this.poidsTotal = poidsTotal;
	}

	public Integer getNbInstants() {
		return NbInstants;
	}

	public void setNbInstants(Integer nbInstants) {
		NbInstants = nbInstants;
	}

	public List<Double> getFrequencesPonderales() {
		return frequencesPonderales;
	}

	public void setFrequencesPonderales(List<Double> frequencesPonderales) {
		this.frequencesPonderales = frequencesPonderales;
	}

	public void afficher(List<?> res) {
		for(int i=0 ; i < res.size() ; i++ ) {
			System.out.println(" "+res.get(i));
		}
	}
}

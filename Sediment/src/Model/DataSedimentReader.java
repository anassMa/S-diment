package Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;


public class DataSedimentReader {

    List<String> data ;
    Double poidsTotal;
    String message ;

    public DataSedimentReader(String fileName) throws IOException {
        this.data=readInListe(fileName);
    }
    private  List<String> readInListe(String fileName) throws IOException {
        List<String> result;
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            result = lines.collect(Collectors.toList());
        }

        result.removeIf(StringUtils::isBlank);
        result.replaceAll(n -> n.trim());
        return result;

    }


    public Sediment read() throws IOException {

        Sediment sed = new Sediment();

        sed.setMySediment(this.stockerDataToMap(this.data)); // stocker la somme de chaque sediment par instant

        Set listKeys=this.stockerDataToMap(this.data).keySet();  // Obtenir la liste des clés
		Iterator iterateur=listKeys.iterator();
		// Parcourir les clés et afficher les entrées de chaque clé;
		while(iterateur.hasNext())
		{
			Object key= iterateur.next();
			System.out.println (key+"=>"+this.stockerDataToMap(this.data).get(key));
		}
        sed.setPoidsTotal(this.geTotalPoids());

        System.out.println("poids total  == "+this.geTotalPoids());

        sed.setNbInstants(sed.mySediment.keySet().size());

        System.out.println(sed.mySediment.keySet().size());

        sed.setFrequencesPonderales( this.calculFrequencesPonderales( sed.mySediment ,  sed.poidsTotal) );
        this.afficher(sed.getFrequencesPonderales());
        sed.setFrequencesPonderalesCumule( this.calculFreqPondCumule(sed.getFrequencesPonderales()) );
        this.afficher(sed.getFrequencesPonderalesCumule());
        sed.setVitesse(this.calculVitesse ( sed.mySediment ));

        sed.setDiametre(this.calculDiametre(sed.getVitesse()));

        return sed;

    }

    public boolean verifierFormatFichier(){

        boolean  ligne = false;


        if(this.data.isEmpty()){
            this.message="Le fichier est vide ";
            return false ;
        }
        else {
            ligne = Pattern.matches("\\d+(.(\\d+)*)*",this.data.get(0) ) ;

            if(!ligne){
                this.message = "Erreur dans la ligne numéro 1 ";
                return false;}

            for(int i=1 ; i < this.data.size() ; i++ ) {

                 ligne = Pattern.matches("SSSSS\\s+(-)?\\d+.\\d+\\sg\\s+(([01]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9]))",this.data.get(i) ) ;

                if(!ligne){
                    this.message = "Erreur dans la ligne numéro  "+(i+1);
                    return false;

                }

             }}

        return ligne ;
    }
    public List<String> NettoyerData (List<String> ensemble){
        List<String> result = new ArrayList<>();
        result.add(ensemble.get(0));
        int indiceNeg = 0 ;
        boolean v = false ;
        String[] t , t1;

        Double masseCurrent ;
        String instantTime = null , instantTime1 ;

        for (int i = ensemble.size()-1; i >=0; i--) {
        	 t = ensemble.get(i).split("\\s+");
        	 masseCurrent = Double.parseDouble(t[1]);
        	 instantTime = t[3] ;
        	 if(masseCurrent < 0){
        		 indiceNeg = i ;
        		 v=true;
        		 break ;
        	 }}

            if(v){
        	for (int i = indiceNeg ; i < ensemble.size(); i++) {

        		t1 = ensemble.get(i).split("\\s+");
        		instantTime1 = t1[3];

        		if(instantTime.equals(instantTime1))
        			continue;

        		else{
                   result.add(ensemble.get(i));
        		  }
        	}

        	}
            else {result = ensemble ;}
            return result;
        	}



    public Map<Integer, Double> stockerDataToMap(List<String> ensemble) {

    	ensemble = NettoyerData ( ensemble);
        Map<Integer, Double> myData = new HashMap<Integer, Double>();

        Double somme = 0.0;

        int heure = 0;

        Integer instantCurrent = null, instant = null;
        Double masseCurrent = null, masse = null;

        String[] t1;
        String[] time1;

        String[] t = ensemble.get(1).split("\\s+");

        String[] time = t[3].split(":");

        instantCurrent = Integer.parseInt(time[2]);
        masseCurrent = Double.parseDouble(t[1]);

        somme += masseCurrent;
        this.poidsTotal=somme;

        for (int i = 2; i < ensemble.size(); i++) {

            t1 = ensemble.get(i).split("\\s+");

            time1 = t1[3].split(":");

            instant = (Integer.parseInt(time1[2]));
            masse = Double.parseDouble(t1[1]);

            this.poidsTotal +=masse;

            heure = (instantCurrent == 59 && instant == 0) ? heure + 1 : heure;

            if (instantCurrent.equals(instant + (60 * heure))) {

                somme += masse;

                if (i == ensemble.size() - 1) {

                    myData.put(instantCurrent, somme);

                }

            } else {
                if (i == ensemble.size() - 1) {

                    myData.put(instantCurrent, somme);

                    myData.put((60 * heure) + instant, masse);

                } else {

                    myData.put(instantCurrent, somme);
                    somme = masse;
                    instantCurrent = (60 * heure) + instant;
                }
            }
        }
        Map<Integer,Double> sortedData = new TreeMap<Integer,Double>(myData);
        return sortedData;
    }

    public Double geTotalPoids() {
        return this.poidsTotal;
    }

    public List<Double> calculFrequencesPonderales( Map<Integer,Double> mySediment , Double poidstotal)	{
          List<Double> res = new ArrayList<>();
          Double current ;
          Set<Integer> listKeys = mySediment.keySet();
          Iterator<Integer> iterateur=listKeys.iterator();
          while(iterateur.hasNext())
            {
                Object key= iterateur.next();
                current = (mySediment.get(key)/ poidstotal)*100;
                res.add(current);
            }
          return res ;

      }

    public List<Double> calculVitesse ( Map<Integer,Double> mySediment ) {
        List<Double> res = new ArrayList<>();
      Double actualValeur ;
      Iterator<Map.Entry<Integer,Double >> iterator = mySediment.entrySet().iterator();
      while (iterator.hasNext()) {
              Map.Entry<Integer,Double> entry = iterator.next();
              actualValeur =    (  Configuration.getLongueur() / entry.getKey());
              res.add(actualValeur);
          }
          return res ;
}

    public List<Double> calculFreqPondCumule (List<Double> FrequencesPonderales ){

          List<Double> res = new ArrayList<>();
          Double actualValeur  = FrequencesPonderales.get(0);
          res.add(actualValeur);
          for(int i=1 ; i < FrequencesPonderales.size() ; i++ ) {
              actualValeur += FrequencesPonderales.get(i);
              res.add(actualValeur);
          }

          return res ;
      }

     public List<Double> calculDiametre ( List<Double> vitesse  ){
          List<Double> res = new ArrayList<>();
          Double actualValeur ;
           for(int i=0 ; i < vitesse.size() ; i++){
                   actualValeur = Configuration.getA0()+(Configuration.getA1()*vitesse.get(i))+(Configuration.getA2()*Configuration.getTemperature()) ;
                    res.add(Math.pow(10, actualValeur));
               }

          return res ;
     }
    public void afficher(List<?> res) {
        for(int i=0 ; i < res.size() ; i++ ) {
            System.out.println(" "+res.get(i));
        }
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }


}

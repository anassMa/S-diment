
package application;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;


import com.itextpdf.text.DocumentException;

import Chart.LogarithmicAxis;
import Model.Configuration;
import Model.DataSedimentReader;
import Model.Sediment;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;

import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;






public class SampleController implements Initializable{

	    @FXML
        private Label ch ;
	    @FXML
	    private Button traiter;
	    @FXML
	    private Button configurer;
	    @FXML
	    private Button tracerCourbe;
	    @FXML
	    private Label label;
	    @FXML
	    private TextArea auteur ;
	    @FXML
	    private TextArea echantillon ;
	    @FXML
	    private TextArea distance;
	    @FXML
	    private TextArea profil ;
	    @FXML
	    private TextArea latitude  ;
	    @FXML
	    private TextArea longitude ;
	    @FXML
	    private DatePicker  DatePrel ;
	    @FXML
	    private TextField longeur;
	    @FXML
	    private TextField temperature;
	    @FXML
	    private Label a0;
	    @FXML
	    private Label a1;
	    @FXML
	    private Label a2;
	    @FXML
	    private TextField min;
	    @FXML
	    private TextField max;
	    @FXML
	    private Button buttonChosir ;
	    @FXML
	    AnchorPane charteP;
	    @FXML
	     private Label medianeL ;
	    @FXML
	     private Label  quartileInf ;
	    @FXML
	     private Label qSup;
	    @FXML
	    private TextArea RepTravail;


	    private LineChart<Number,Number> chart;

	    DataSedimentReader r ;

	    String firstPath , firstPath1, firstPath2;

	    double mediane , quartileInff , quatileSup ;

	    String nameFile ,nameFile1 , nameFile2;

	    Sediment sed ;

	    String auteurOK ="", echantillonOK="", distanceOK="", profilOK="", latitudeOK="", longitudeOK="";

	    String pwd = System.getProperty("user.dir");

	    String cheminRepTravail = pwd ;




	    @FXML
	    private void changer(ActionEvent event){

	    	    cheminRepTravail=RepTravail.getText();

	    	    Alert alert = new Alert(AlertType.INFORMATION);
	  	    	alert.setTitle("Information");
	  	    	alert.setHeaderText(null);
	  	    	alert.setContentText("Le chemin de travail courant  est  : "+cheminRepTravail);
	  	    	alert.showAndWait();

	    	    }

	    @FXML
	    private void Choisir(ActionEvent event) {

	       FileChooser fc = new FileChooser();
	       File chemin = new File(cheminRepTravail);
	       fc.setInitialDirectory(chemin);

	       if (firstPath != null) {
	           File path = new File(firstPath);
	           fc.initialDirectoryProperty().set(path);
	       }

	       fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("dat fichiers" , "*.dat"));
	       fc.setTitle("Selectionner le fichier");

	       File f = fc.showOpenDialog(null);
	       if( f!= null){
	           label.setText(f.getAbsolutePath());
	           String path = f.getPath();
	           int len = path.lastIndexOf("/"); //no detec return -1
	           if (len == -1) {
	               len = path.lastIndexOf("\\");
	        }
		    firstPath = path.substring(0, len);
		    File file = new File(f.getAbsolutePath().replace('\\', '/'));
		    nameFile = file.getName();
           }

	     }

	    @FXML
	    private void charger(ActionEvent event) throws IOException {

	    	   FileChooser fc = new FileChooser();
		       File chemin = new File(cheminRepTravail);
		       fc.setInitialDirectory(chemin);

		       if (firstPath1 != null) {
		           File path = new File(firstPath1);
		           fc.initialDirectoryProperty().set(path);
		       }

		       fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("txt fichiers" , "*.txt"));
		       fc.setTitle("Selectionner le fichier");

		       File f = fc.showOpenDialog(null);

		       if( f!= null){
		           ch.setText(f.getAbsolutePath());
		           String path = f.getPath();
		           int len = path.lastIndexOf("/"); //no detec return -1
		           if (len == -1) {
		               len = path.lastIndexOf("\\");
		        }

			     firstPath1 = path.substring(0, len);
			     File file = new File(f.getAbsolutePath().replace('\\', '/'));
			     nameFile1 = file.getName();

	            }
		        if(! verifierFormat(ch.getText())){
                	Alert alert = new Alert(AlertType.WARNING);
		    		alert.setHeaderText(null);
			    	alert.setContentText("la format de fichier n'est pas OK  !\n");
			    	alert.showAndWait();
                }

		        else{
		        	lire(ch.getText());
		        }

		        }

	    @FXML
	    private void Enregistrer(ActionEvent event) throws IOException{

              if(auteur.getText().isEmpty() || echantillon.getText().isEmpty() || distance.getText().isEmpty() || profil.getText().isEmpty()  ||
            		latitude.getText().isEmpty() || longitude.getText().isEmpty() || DatePrel.getValue()== null ){

            	    Alert alert = new Alert(AlertType.WARNING);
		    		alert.setHeaderText(null);
			    	alert.setContentText("Il faut remplir tous les champs ! ");
			    	alert.showAndWait();
              }

              else{

            	  if( Pattern.matches("[a-zA-Z]+\\s*[a-zA-Z]*",auteur.getText() ) &&
        	    		  Pattern.matches("\\d+",echantillon.getText())                &&
        	    		  Pattern.matches("\\d+",distance.getText() )                  &&
        	    		  Pattern.matches("\\d+",profil.getText() )                    &&
        	    		  Pattern.matches("(-)?\\d+.\\d+",latitude.getText() )         &&
        	    		  Pattern.matches("(-)?\\d+.\\d+",longitude.getText() )
        	    		  ) {

                	    auteurOK=auteur.getText();
                	    echantillonOK=echantillon.getText();
                        distanceOK=distance.getText();
                        profilOK=profil.getText();
                        latitudeOK=latitude.getText();
                        longitudeOK=longitude.getText();

	                	final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	                	alert.setTitle("Demande de confirmation");
	                	alert.setContentText("Souhaitez-vous écraser le fichier courant ?");
	                	final Optional<ButtonType> result = alert.showAndWait();
	                	result.ifPresent(button -> {
	                	if (button == ButtonType.OK) {
	                	    	try {
	    							replacement(ch.getText());
	    						} catch (IOException e) {
	    							e.printStackTrace();
	    						}
	                	      }

                	     else {
                	    	  FileChooser fc = new FileChooser();
                        	  File chemin = new File(cheminRepTravail);
               		          fc.setInitialDirectory(chemin);
               		          if (firstPath2 != null) {
               		           File path = new File(firstPath2);
               		           fc.initialDirectoryProperty().set(path);
               		       }

                          FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                          fc.getExtensionFilters().add(extFilter);

                          File f = fc.showSaveDialog(null);

                          if (f != null) {
                               try {
    								replacement(f.getAbsolutePath());
    							} catch (IOException e) {
    								// TODO Auto-generated catch block
    								e.printStackTrace();
    					        }

                           String path = f.getPath();
               		       int len = path.lastIndexOf("/"); //no detec return -1

               		       if (len == -1) {
               		               len = path.lastIndexOf("\\");
               		           		}

               			    firstPath2 = path.substring(0, len);
               			    File file = new File(f.getAbsolutePath().replace('\\', '/'));
               			    nameFile2 = file.getName();

                              }
                	      }
                	  });


	                	  Alert alert1 = new Alert(AlertType.INFORMATION);
	    		  	      alert1.setTitle("Information");
	    		  	      alert1.setHeaderText(null);
	    		  	      alert1.setContentText("Les informations ont été bien enregistrées dans le fichier !");
	    		  	      alert1.showAndWait();
                  }

            	  else{

            		  if( !Pattern.matches("[a-zA-Z]+\\s*[a-zA-Z]*",auteur.getText() )){
		            	    Alert alert = new Alert(AlertType.WARNING);
				    		alert.setHeaderText(null);
					    	alert.setContentText("Le champ  Auteur doit être une chaine de caractères alphabétiques  !!!");
					    	alert.showAndWait();
					    	auteur.setText(auteurOK);
	              }

            		  if( !Pattern.matches("\\d+",echantillon.getText() )){
		            	    Alert alert = new Alert(AlertType.WARNING);
				    		alert.setHeaderText(null);
					    	alert.setContentText("Le champ  Numéro d'échantillon doit être un entier  !!!");
					    	alert.showAndWait();
					    	echantillon.setText(echantillonOK);
	              }

            		  if( !Pattern.matches("\\d+",distance.getText() )){
	            			Alert alert = new Alert(AlertType.WARNING);
				    		alert.setHeaderText(null);
					    	alert.setContentText("Le champ  Distance doit être un entier  !!! ");
					    	alert.showAndWait();
					    	distance.setText(distanceOK);
	              }

	               if( !Pattern.matches("\\d+",profil.getText() )){
		            	    Alert alert = new Alert(AlertType.WARNING);
				    		alert.setHeaderText(null);
					    	alert.setContentText("Le champ  Numéro de profil doit être un entier  !!! ");
					    	alert.showAndWait();
					    	profil.setText(profilOK);
	              }
	               if( !Pattern.matches("(-)?\\d+.\\d+",latitude.getText() )){
		            	    Alert alert = new Alert(AlertType.WARNING);
				    		alert.setHeaderText(null);
					    	alert.setContentText("Le champ  Latitude doit être de type double !!! ");
					    	alert.showAndWait();
					    	latitude.setText(latitudeOK);
	              }
	               if( !Pattern.matches("(-)?\\d+.\\d+",longitude.getText() )){
		            	    Alert alert = new Alert(AlertType.WARNING);
				    		alert.setHeaderText(null);
					    	alert.setContentText("Le champ  Longitude doit être de type double !!! ");
					    	alert.showAndWait();
					    	longitude.setText(longitudeOK);
	              }

            	 }
           }}

	    public void replacement(String txt ) throws IOException {

	    	   FileWriter fw = new FileWriter(txt);
	    	   BufferedWriter bw = new BufferedWriter(fw);

	    	   bw.write(auteur.getText());
	    	   bw.newLine();
	    	   bw.write(echantillon.getText());
	    	   bw.newLine();
	    	   bw.write(distance.getText());
	    	   bw.newLine();
	    	   bw.write(profil.getText());
	    	   bw.newLine();
	    	   bw.write(latitude.getText());
	    	   bw.newLine();
	    	   bw.write(longitude.getText());
	    	   bw.newLine();
	    	   bw.write(DatePrel.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

	    	   bw.flush();
	    	   bw.close();

	    }

	    @FXML
	    private void traiterFichier(ActionEvent event) throws IOException {

	    	  if(  label.getText().isEmpty() ){

	    		    Alert alert = new Alert(AlertType.WARNING);
		    		alert.setHeaderText(null);
			    	alert.setContentText("Vous avez entré aucun fichier  !");
			    	alert.showAndWait();

	    	  }
	    	  else {

	    		  r = new DataSedimentReader(label.getText());
                    if(! r.verifierFormatFichier()){
                    	Alert alert = new Alert(AlertType.WARNING);
    		    		alert.setHeaderText(null);
    			    	alert.setContentText("la format de fichier n'est pas OK   !\n"+r.getMessage());
    			    	alert.showAndWait();
                    }

                    else{
				    	sed = new Sediment();
			            sed = r.read();
			            Alert alert = new Alert(AlertType.INFORMATION);
			  	    	alert.setTitle("Information");
			  	    	alert.setHeaderText(null);
			  	    	alert.setContentText("Le fichier était chargé avec succés !");
			  	    	alert.showAndWait();
                    }
	    	  }
	    }

	    @FXML
	    private void configurer(ActionEvent event){

	    	if( Double.parseDouble(longeur.getText()) > 0 && Double.parseDouble(temperature.getText()) > 0) {

		    	Configuration.setLongueur(Double.parseDouble(longeur.getText()));
		    	Configuration.setTemperature(Double.parseDouble(temperature.getText()));
		    	Alert alert = new Alert(AlertType.INFORMATION);
		    	alert.setTitle("Information");
		    	alert.setHeaderText(null);
		    	alert.setContentText("La configuration est terminée avec succés !");
		    	alert.showAndWait();

	    	}
	    	else{
	    		if( Double.parseDouble(longeur.getText()) <= 0) {
		    		Alert alert = new Alert(AlertType.WARNING);
		    		alert.setHeaderText(null);
			    	alert.setContentText("La longueur du tube ( cm ) doit être supérieure à 0  !");
			    	alert.showAndWait();
		    	    }

		    	if( Double.parseDouble(temperature.getText()) <= 0) {
		    		Alert alert = new Alert(AlertType.WARNING);
		    		alert.setHeaderText(null);
			    	alert.setContentText("La température doit être supérieure à 0  !");
			    	alert.showAndWait();
		    	}
	    	}
	    }

	    @FXML
	    private void tracerCourbe(ActionEvent event) throws IOException{

	    	if(Double.parseDouble(min.getText()) > 0 && Double.parseDouble(max.getText()) > 0 && Double.parseDouble(max.getText()) > Double.parseDouble(min.getText()) ){

		        chart =  createLogScatterChart(sed.getDiametre() , sed.getFrequencesPonderalesCumule() , Double.parseDouble(min.getText())  , Double.parseDouble(max.getText()) );
		    	charteP.getChildren().clear();
		    	charteP.getChildren().add(fitToParent(chart));

	            mediane =   getMediane(sed.getDiametre() ,sed.getFrequencesPonderalesCumule());

	            medianeL.setText("Médiane : "+mediane);

	            quartileInff =   getQuartileInf(sed.getDiametre() ,sed.getFrequencesPonderalesCumule());

	            quartileInf.setText("Quartile inférieur :"+quartileInff);

	            quatileSup =getQuartileSup(sed.getDiametre() ,sed.getFrequencesPonderalesCumule());

                if(quatileSup != 0 )
                	qSup.setText("Quartile Supérieur :"+quatileSup);

                // Capture de l'image de la courbe
	            WritableImage image = chartsToImages(chart);
	            File file = new File("courbe"+nameFile);
	            try {
	                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);

	             } catch (IOException ex) {
	               }

	            // deplacemment de l'image vers le dossier result
	             Files.move
	                        (Paths.get(file.getAbsolutePath()),
	                        Paths.get(cheminRepTravail+"/result/"+"courbe"+nameFile+".png"));
	            }


	    	else {

	    		if( Double.parseDouble(min.getText()) <= 0) {

		    		Alert alert = new Alert(AlertType.WARNING);
		    		alert.setHeaderText(null);
			    	alert.setContentText("Le Min du diamètre doit être supérieur à 0  !");
			    	alert.showAndWait();

		    	}
	    		if( Double.parseDouble(max.getText()) <= 0) {

		    		Alert alert = new Alert(AlertType.WARNING);
		    		alert.setHeaderText(null);
			    	alert.setContentText("Le Max du diamètre doit être supérieur à 0  !");
			    	alert.showAndWait();

		    	}
	    		if( Double.parseDouble(min.getText()) >= Double.parseDouble(max.getText())) {

		    		Alert alert = new Alert(AlertType.WARNING);
		    		alert.setHeaderText(null);
			    	alert.setContentText("Le Max doit être supérieur au Min !");
			    	alert.showAndWait();

		    	}
	    	}
	    }
	    public  LineChart<Number,Number> createLogScatterChart(List<Double> X , List<Double> Y , double MIN_X , double MAX_X ) {

	    	final int NUM_POINTS = X.size();

	        final NumberAxis yAxis = new NumberAxis("Frequence cumulé", 0, 100 , 5);
	        final LogarithmicAxis xAxis = new LogarithmicAxis(MIN_X,MAX_X);
	        xAxis.setLabel("Diametre en micron");

	        final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);

	        lineChart.setTitle("Courbe du fichier "+nameFile);
	        lineChart.setAnimated(false);

	        XYChart.Series<Number,Number> series = new XYChart.Series<Number,Number> ();

	        //series.setName("Serie1");

	        for (int i = 0; i < NUM_POINTS; i++) {
	        	series.getData().add(new XYChart.Data<>( X.get(i),Y.get(i)));
	        	// System.out.println(  X.get(i) +"     " + Y.get(i) );
	        }

	        lineChart.getData().add(series);

	        return lineChart;

	    }

	    @FXML
	    private void generer(ActionEvent event) throws IOException, DocumentException{

	    	String FILE = cheminRepTravail+"/result/RapportDe"+nameFile+".txt";

	    	CreerText(FILE);

	    	Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information");
  	    	alert.setHeaderText(null);
  	    	alert.setContentText("Le fichier est bien enregistré !");
  	    	alert.showAndWait();

	    }

      public  void lire(String text ){

    	  try (BufferedReader reader = new BufferedReader(new FileReader(new File(text)))) {

    	  int l = 0;
    	  String line;

    	  while ((line = reader.readLine()) != null){

    	        	switch (l) {

    	        	  case 0: auteur.setText(line); break;

    	        	  case 1:echantillon.setText(line);break;

    	        	  case 2:distance.setText(line);break;

    	        	  case 3:profil.setText(line); break;

    	        	  case 4:latitude.setText(line);break;

    	        	  case 5:longitude.setText(line);break;

    	        	  case 6:DatePrel.setValue(LOCAL_DATE(line));break;

    	        	}

    	        	l++;

    	        }
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	    }
    	}


	    public static  double getMediane( List<Double> X ,  List<Double> Y ){

	    	double mediane = 0 ;
	        double Q50Min = 0;
	        double Q50Max = 0;

	    	for (int i=0 ; i < X.size() ;  i++) {

	              if(   Y.get(i) < 51. && Y.get(i) >= 50. ){
	              	return X.get(i);
	              }

	              else if( Y.get(i) < 50. ){
	            	  Q50Min = Math.max(X.get(i),Q50Min );
	              }

	              else {
	            	  Q50Max = X.get(i) ;
		              break;
	              }
	              }

	    	  if( Q50Min == 0 || Q50Max == 0 )
	          	  return 0;

	          mediane = (Q50Min + Q50Max )/2;

	    	return mediane ;
	    }
	    public static  double getQuartileInf(List<Double> X ,  List<Double> Y ){

	    	double quartile = 0 ;
	        double Q25Min = 0;
	        double Q25Max = 0;

	        for (int i=0 ; i < X.size() ;  i++) {

	          if(    Y.get(i) < 26. && Y.get(i) >= 25. ){
	              	return X.get(i);
	              }

	              else if(Y.get(i) < 25.){
	            	  Q25Min = Math.max(X.get(i),Q25Min );
	              }

	              else {
	            	  Q25Max = X.get(i) ;
		              break;
	              }
	          }

	        if(Q25Min == 0 || Q25Max == 0)
	          	  return 0;

	        quartile = (Q25Min + Q25Max )/2;

	    	return quartile ;
	    }

	    public static  double getQuartileSup(List<Double> X ,  List<Double> Y ){

	    	double quartile = 0 ;
	        double Q75Min = 0;
	        double Q75Max = 0;

	        for (int i=0 ; i < X.size() ;  i++) {

	          if(    Y.get(i) < 76. && Y.get(i) >= 75. ){
	              	return X.get(i);
	              }

	              else if(Y.get(i) < 75.){
	            	  Q75Min = Math.max(X.get(i),Q75Min );
	              }

	              else {
	            	  Q75Max = X.get(i) ;
		              break;
	              }
	        }

	        if(Q75Min == 0 || Q75Max == 0)
                	  return 0;

	        quartile = (Q75Min + Q75Max )/2;

	    	return quartile ;
	    }

	    private WritableImage chartsToImages(Chart chart) {

	        SnapshotParameters snapshotParameters = new SnapshotParameters();
	        snapshotParameters.setTransform(new Scale(2, 2));

	        return  chart.snapshot(snapshotParameters, null);
	    }

	    public static Node fitToParent(Node chart2) {

	        AnchorPane.setTopAnchor(chart2, 0.0);
	        AnchorPane.setBottomAnchor(chart2, 0.0);
	        AnchorPane.setLeftAnchor(chart2, 0.0);
	        AnchorPane.setRightAnchor(chart2, 0.0);
	        return chart2;

	    }


	    public  boolean verifierFormat(String cheminFile) throws IOException{
	    	  try (BufferedReader reader = new BufferedReader(new FileReader(new File(cheminFile)))) {
	    		    int l = 0;
	    	        String line;
	    	        boolean ligne;
	    	        while ((line = reader.readLine()) != null){
	    	        	l++;
	    	        }
	    	        if( l != 7)
	    	        	return false;
	    	        else {
	    	        	BufferedReader reader1 = new BufferedReader(new FileReader(new File(cheminFile))) ;
	                   l=0;
	    	    	        while ((line = reader1.readLine()) != null){
	    	    	           	switch (l) {
	    	    	        	  case 0:
	    	    	        		  ligne = Pattern.matches("[a-zA-Z]+\\s*[a-zA-Z]*",line ) ;
	    	    	                  if(!ligne)
	    	    	                      return false;
	    	    	        	       break;
	    	    	        	  case 1:
	    	    	        		ligne = Pattern.matches("\\d+",line ) ;
	  	    	                  if(!ligne)
	  	    	                      return false;
	    	    	        	    break;
	    	    	        	  case 2:
	    	    	        		ligne = Pattern.matches("\\d+",line ) ;
	  	    	                  if(!ligne)
	  	    	                      return false;
	    	    	        	    break;
	    	    	        	  case 3:
	    	    	        		ligne = Pattern.matches("\\d+",line ) ;
	  	    	                  if(!ligne)
	  	    	                      return false;
	    	    	        	    break;
	    	    	        	  case 4:
	    	    	        		ligne = Pattern.matches("(-)?\\d+.\\d+",line ) ;
	  	    	                  if(!ligne)
	  	    	                      return false;
	    	    	        	    break;
	    	    	        	  case 5:
	    	    	        		ligne = Pattern.matches("(-)?\\d+.\\d+",line ) ;
	  	    	                  if(!ligne)
	  	    	                      return false;
	    	    	        	    break;
	    	    	        	  case 6:
	    	    	        		ligne = Pattern.matches("([0-2][0-9]|3[0-1])/(((0)[0-9])|((1)[0-2]))/\\d{4}",line ) ;
	  	    	                  if(!ligne)
	  	    	                      return false;
	    	    	        	    break;
	    	    	        	}
	    	    	        	l++;
	    	    	        }  reader1.close();
	    	        }
	    }

	  		return true;}



	    public static final LocalDate LOCAL_DATE (String dateString){
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	        LocalDate localDate = LocalDate.parse(dateString, formatter);
	        return localDate;
	    }

	    public void CreerText(String nom) throws IOException {

	    	   FileWriter fw = new FileWriter(nom);
	    	   BufferedWriter bw = new BufferedWriter(fw);

	    	   bw.write("Auteur: " + auteur.getText());
	    	   bw.newLine();
	    	   bw.write("Numéro d'échantillon  : " + echantillon.getText());
	    	   bw.newLine();
	    	   bw.write("Distance  : " + distance.getText());
	    	   bw.newLine();
	    	   bw.write("Numéro de profil  : " + profil.getText());
	    	   bw.newLine();
	    	   bw.write("Latitude  : " + latitude.getText());
	    	   bw.newLine();
	    	   bw.write("Longitude  : " + longitude.getText());
	    	   bw.newLine();
	    	   bw.write("Date de prélèvement  : " + DatePrel.getValue());
	    	   bw.newLine();
	    	   bw.write( "Température (°C)   : " + Configuration.getTemperature());
	    	   bw.newLine();
	    	   bw.write( "Longeur du tube (cm)  : " + Configuration.getLongueur());
	    	   bw.newLine();
	    	   bw.write( "Fichier source  : " + nameFile);
	    	   bw.newLine();
	    	   bw.write( "Médiane : "+mediane);
	    	   bw.newLine();
	    	   bw.write("Quartile inférieur Q25 : "+quartileInff);
	    	   bw.newLine();
	    	   if(quatileSup !=0)
	    		   bw.write("Quartile Supérieur Q 75 : "+quatileSup);
	   	        else
	   	         bw.write("Quartile Supérieur Q 75 : "+"");

	    	   bw.flush();
	    	   bw.close();

	    }
	    @Override
	    public void initialize(URL url, ResourceBundle rb) {
	    	RepTravail.clear();
	    	RepTravail.setText(cheminRepTravail);

	    }



}

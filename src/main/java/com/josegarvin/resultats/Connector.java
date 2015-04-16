package com.josegarvin.resultats;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
  * Classe que s'encarrega de realitzar la connexió i realitzar accions sobre
  * la base de dades.
  * @author b4tm4n
  */
public class Connector {

  private Connection connexio;

  /**
   * Constructor per defecte le classe.
   */
  public Connector() {

    try {

      Context ic = new InitialContext();
      DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/MySQLConnection");
      connexio = ds.getConnection();

    } catch (NamingException ex) {
      Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
    } catch (SQLException ex) {
      Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Mètode per obtenir la connexio.
   * @return the connexio
   */
  public Connection getConnexio() {
    return connexio;
  }

  /**
   * Mètode per obtenir tots els equips de la base de dades.
   * 
   * @return --> Retorna un Map on la clau representa l'ID de
   *l'equip i el valor el nom de l'equip.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
public Map obtenirEquips() {
    ResultSet rs;
    Map resultats = new HashMap();
    try {
      Statement stmt = null;
      rs = null;
      System.out.println("Obtenint equips!!");
      String query = "select * from equips";
      stmt = null;

      stmt = connexio.createStatement();
      rs = stmt.executeQuery(query);
      while (rs.next()) {

        String nom = rs.getString("nom");
        int id = rs.getInt("id");
        resultats.put(id, nom);
      }
      return resultats;
      
    } catch (SQLException ex) {
      Logger.getLogger(Resultats.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  /**
   * Mètode per obtenir l'ID d'un equip a partir del nom.
   * 
   * @param nomEquip --> Nom de l'equip.
   * 
   * @return --> ID de l'equip.
   */
  public int obtenirIdEquipPerNom(String nomEquip) {
    try {
      Statement stmt = null;
      ResultSet rs = null;
      int id = -1;

      String query = "select id from equips where nom='" + nomEquip + "'";
      stmt = null;

      stmt = connexio.createStatement();
      rs = stmt.executeQuery(query);

      while (rs.next()) {
        id = rs.getInt(1);
        System.out.println("id=" + id);
      }

      return id;
    } catch (SQLException ex) {
      Logger.getLogger(Resultats.class.getName()).log(Level.SEVERE, null, ex);
    }
    return -1;
  }
   
  /**
   * Mètode que s'encarrega de netejar la sortida de "request.getPathInfo()"
   * per tal de esborrar les barres "/" i decodificar el resultat a UTF-8.
   * 
   * @param 
   * --> Ruta a netejar.
   * 
   * @return --> Retorna la ruta neta sense barres ni simbols estranys.
   */
  public String netejarRuta(String ruta) {

    String cadenaEquip = "";

    cadenaEquip = ruta.substring(1, ruta.length());

    String equip = "";
    try {
      equip = URLDecoder.decode(cadenaEquip, "UTF-8");
    } catch (UnsupportedEncodingException ex) {
      Logger.getLogger(Resultats.class.getName()).log(Level.SEVERE, null, ex);
    }

    return equip;
  }

    /**
     * Mètode per obtenir els punts, la posició i l'any de la temporada
     * a partir de l'ID d'un equip.
     * @param idEquip --> ID equip.
     * 
     * @return --> Retorna un ArrayList, que conté ArrayList amb l'info
     *de cada temporada.
     */
  public ArrayList<ArrayList<Integer>> obtenirInformacio(int idEquip) {
        
    ArrayList<ArrayList<Integer>> resultats = new ArrayList<>();
         
    try {
      Statement stmt = null;
      Logger.getLogger(Resultats.class.getName()).log(Level.INFO, "Analitzant resultats!!!");

      String query = "select temporada, punts, posicio from resultats where equip=" + idEquip;
      stmt = null;

      stmt = connexio.createStatement();
      ResultSet rs = null;    
      rs = stmt.executeQuery(query);
            
            
      while (rs.next()) {
        ArrayList<Integer> fila = new ArrayList<>();
                                
        int temporada = rs.getInt("temporada");
        int punts = rs.getInt("punts");
        int posicio = rs.getInt("posicio");
                
        fila.add(temporada);
        fila.add(punts);
        fila.add(posicio);
                
        resultats.add(fila);
      }

      return resultats;
    } catch (SQLException ex) {
      Logger.getLogger(Resultats.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;

  }
}

package com.josegarvin.resultats;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet de l'aplicació "Resultats Futbol".
 * 
 * @author Jose Garvin Victoria.
 */
@WebServlet("/*")
public class Resultats extends HttpServlet {

  /**
   * Serial Version UID.
   */
  private static final long serialVersionUID = 1L;
  String paginaInici = "index.jsp";
  Connector connector = new Connector();

  /**
   * Mètode per mostrar la benvinguda.
   *
   * @param request
   *            servlet request
   * @param response
   *            servlet response
   * @throws ServletException
   *             if a servlet-specific error occurs
   * @throws IOException
   *             if an I/O error occurs
   */
  @SuppressWarnings("rawtypes")
protected void mostrarBenvinguda(HttpServletRequest request,
         HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    try (PrintWriter out = response.getWriter()) {
      out.println("<!DOCTYPE html>");
      out.println("<html>");
      out.println("<head>");
      out.println("<title>Consulta Resultats</title>");
      out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css\">");
      out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css\">");
      out.println("</head>");
      out.println("<body>");
      out.println("<div class=\"container\">");
      out.println("<h1 class=\"text-center\">Benvingut!</h1>");
      out.println("<div class=\"row\">");
      out.println("<div class=\"jumbotron\">");
      out.println("<p class=\"text-center\">Pots consultar els"
                + " resultats d'un equip a partir d'URL's"
          + " amb aquest format.</p>");
      out.println("<p class=\"text-center bg-primary\">http://192.168.X.X:8080/Resultats/NomEquip</p>");
      out.println("</div>");
      out.println("</div>");
      out.println("<div class=\"row\">");
      out.println("<div class=\"jumbotron\">");
      out.println("<p class=\"text-center\">També pots escollir l'equip directament!</p>");
      out.println("<select class=\"col-md-4 col-md-offset-4\" name='Code'"
          + " onchange=\"redireccio(this.options[this.selectedIndex].innerHTML)\">  ");
      out.println("<option value=\"none\">Selecciona un equip:</option> ");

      Map resultats = connector.obtenirEquips();

      Iterator it = resultats.entrySet().iterator();
      
      while (it.hasNext()) {
        Map.Entry entry = (Map.Entry) it.next();

        out.println("<option value=" + entry.getKey() + ">" + entry.getValue()
            + "</option>");
      }
      out.println("</select>");
      out.println("</div>");
      out.println("</div>");
      out.println("</div>");
      out.println("<script type=\"text/javascript\">");
      out.println("function redireccio(nomEquip)\n"
          + "            {\n"
          + "                window.location = \"/Resultats/\" + nomEquip;\n"
          + "            }");
      out.println("</script>");
      out.println("</body>");
      out.println("</html>");
    }
  }

 /**
  * Mètode per mostrar un missatge d'error a l'usuari.
  * 
  * @param request
  *            servlet request
  * @param response
  *            servlet response
  * @throws ServletException
  *             if a servlet-specific error occurs
  * @throws IOException
  *             IOException if an I/O error occurs
  */
  protected void mostrarError(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    try (PrintWriter out = response.getWriter()) {
      out.println("<!DOCTYPE html>");
      out.println("<html>");
      out.println("<head>");
      out.println("<title>URL Incorrecte</title>");
      out.println("</head>");
      out.println("<body>");
      out.println("<h1>L'URL especificada es incorrecte o equip inexistent!</h1>");
      out.println("<h3>Pots consultar els resultats d'un equip a partir d'URL's"
          + " amb aquest format:</h3>");
      out.println("<h5>http://192.168.X.X:8080/Resultats/NomEquip</h5>");
      out.println("</body>");
      out.println("</html>");
    }
  }

   /**
    * Handles the HTTP <code>GET</code> method.
    *
    * @param request
    *            servlet request
    * @param response
    *            servlet response
    * @throws ServletException
    *             if a servlet-specific error occurs
    * @throws IOException
    *             if an I/O error occurs
*/
  @Override
  protected void doGet(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {


    response.setContentType("text/html");

    String ruta = request.getPathInfo();

    System.out.println(request.getPathInfo());

    if (ruta.equals("/")) {

      mostrarBenvinguda(request, response);

    } else {

      String nomEquip = connector.netejarRuta(ruta);
      int idEquip = connector.obtenirIdEquipPerNom(nomEquip);

      if (idEquip != -1) {
        System.out.println(nomEquip);
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

          out.println("<!DOCTYPE html>");
          out.println("<html>");
          out.println("<head>");
          out.println("<title>Informació de l'equip</title>");
          out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css\">");
          out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css\">");
          out.println("</head>");
          out.println("<body>");
          out.println("<div class='container'>");
          out.println("<h1 class='text-center'><u>Informacio trobada:</u></h1>");
          out.println("<h3 >Nom de l'equip: <b>" + nomEquip
              + "</b></h3>");

          ArrayList<ArrayList<Integer>> resultats = new ArrayList<>();
          resultats = connector.obtenirInformacio(idEquip);

          out.println("<table class='col-md-4 table table-bordered text-center'>");
          out.println("<thead>");
          out.println("<tr>");
          out.println("<th class='text-center'> Temporada </th>");
          out.println("<th class='text-center'> Punts </th>");
          out.println("<th class='text-center'> Posicio </th>");
          out.println("</tr>");
          out.println("</thead>");

          for (int i = 0; i < resultats.size(); i++) {
            out.println("<tr>");
            ArrayList<Integer> fila = resultats.get(i);

            out.println("<td>" + fila.get(0) + "</td>");
            out.println("<td>" + fila.get(1) + "</td>");
            out.println("<td>" + fila.get(2) + "</td>");
            out.println("</tr>");
          }

          out.println("</div>");
          out.println("</body>");
          out.println("</html>");
        }
      } else {
        mostrarError(request, response);
      }

    }

  }

   /**
    * Handles the HTTP <code>POST</code> method.
    *
    * @param request
    *            servlet request
    * @param response
    *            servlet response
    * @throws ServletException
    *             if a servlet-specific error occurs
    * @throws IOException
    *             if an I/O error occurs
    */
  @Override
  protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    mostrarError(request, response);
  }

   /**
   * Returns a short description of the servlet.
   *
   * @return a String containing servlet description
   */
  @Override
  public String getServletInfo() {
    return "Short description";
  }

}

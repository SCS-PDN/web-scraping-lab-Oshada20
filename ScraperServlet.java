import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ScraperServlet extends HttpServlet{
	

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String url = request.getParameter("url");
        String[] options = request.getParameterValues("options");

        
        HttpSession session = request.getSession();
        Integer visitCount = (Integer) session.getAttribute("visitCount");
        if (visitCount == null) visitCount = 1;
        else visitCount++;
        session.setAttribute("visitCount", visitCount);

        WebScraper.ScrapedData data = WebScraper.scrape(url);

        
        out.println("<p>You have visited this page " + visitCount + " times.</p>");

        
        out.println("<table border='1'>");

        if (options != null) {
            for (String opt : options) {
                switch (opt) {
                    case "title":
                        out.println("<tr><th>Title</th><td>" + data.title + "</td></tr>");
                        break;
                    case "headings":
                        out.println("<tr><th>Headings</th><td><ul>");
                        for (String h : data.headings) out.println("<li>" + h + "</li>");
                        out.println("</ul></td></tr>");
                        break;
                    case "links":
                        out.println("<tr><th>Links</th><td><ul>");
                        for (String link : data.links) out.println("<li><a href='" + link + "'>" + link + "</a></li>");
                        out.println("</ul></td></tr>");
                        break;
                }
            }
        }
        out.println("</table>");

        
        Gson gson = new Gson();
        String json = gson.toJson(data);
        out.println("<h3>JSON Data</h3><pre>" + json + "</pre>");

        
        out.println("<form action='download' method='post'>");
        out.println("<input type='hidden' name='json' value='" + json.replace("'", "&#39;") + "'/>");
        out.println("<button type='submit'>Download CSV</button>");
        out.println("</form>");
    }


}
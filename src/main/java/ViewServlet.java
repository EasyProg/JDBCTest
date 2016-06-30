import org.hibernate.Query;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

@WebServlet("/viewservlet")
public class ViewServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException,
                         IOException {
        Session sessionQuery = HibernateUtil.getSessionFactory().openSession();
        sessionQuery.beginTransaction();
        String Query = "FROM ProductsEntity";
        Query newQuery = sessionQuery.createQuery(Query);
        ProductsEntity NewProduct = new ProductsEntity();
        List<ProductsEntity> listProducts = newQuery.list();
        Iterator ListIterator = listProducts.listIterator();
//        String json = new Gson().toJson(listProducts);
//        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write(json);
        PrintWriter out = response.getWriter();
        //ResourceBundle rb = ResourceBundle.getBundle("LocalStrings", request.getLocale());
            try {
            out.println("<!DOCTYPE html>");  // HTML 5
            out.println("<html><head>");
            out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
            //String title = rb.getString("helloworld.title");
            //out.println("<title>" + title + "</title></head>");
            out.println("<body>");
            out.println("<h1>" + "ProductsTable" + "</h1>");  // Prints "Hello, world!"
            out.println("<table border=\"1\" cellspacing=\"0\" cellpadding=\"10\">");
            out.println("<th>ProductID</th>");
            out.println("<th>ProductName</th>");
            out.println("<th>ProductNameEng</th>");
            out.println("<th>Category</th>");
            out.println("<th>Weight</th>");
            out.println("<th>Price</th>");
                while (ListIterator.hasNext()){
                    NewProduct = (ProductsEntity) ListIterator.next();
                    out.println("<tr>");
                    out.println("<td>"+NewProduct.getProductID()+"</td>");
                    out.println("<td>"+NewProduct.getName_Ukr()+"</td>");
                    out.println("<td>"+NewProduct.getName_Eng()+"</td>");
                    out.println("<td>"+NewProduct.getProductCateg()+"</td>");
                    out.println("<td>"+NewProduct.getWEIGHT()+"</td>");
                    out.println("<td>"+NewProduct.getPrice()+"</td>");
                    out.println("</tr>");
                }
            } finally {
            out.close();  // Always close the output writer
        }
        sessionQuery.close();
    }
}





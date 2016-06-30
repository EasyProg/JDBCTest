/**
 * Created by Михаил on 12.12.2015.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class ConnectCoverUP {
    public String AddSpaces(String SName, Integer SpacesCount) {
        int i = 0;
        String StringWithSpaces = "";
        for (i = 0; i < SpacesCount; i++) {
            SName += " ";
        }
        StringWithSpaces = SName;
        return StringWithSpaces;
    }

    public void getConnectData() throws IOException {
        int i = 0;
        Session Querysession = HibernateUtil.getSessionFactory().openSession();
        Querysession.beginTransaction();
        Query QueryList = Querysession.createQuery("FROM ProductsEntity");
        List<ProductsEntity> ListQuery = QueryList.list();
        Iterator ListIterator = ListQuery.listIterator();
        FileWriter FileW = new FileWriter("D:\\Tomcat\\webapps\\MyApp\\json\\Products.json", false);
        JSONObject Json = new JSONObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        int count = 0;
        do {
            ProductsEntity NewProduct = new ProductsEntity();
            NewProduct = (ProductsEntity) ListIterator.next();
//            System.out.println(NewProduct.getProductID() + NewProduct.getName_Ukr() + NewProduct.getName_Eng() +
//                    NewProduct.getProductCateg() + NewProduct.getWEIGHT() + NewProduct.getPrice().toString());}
            int TrimLenght = 30;
            int IDLenght = 4;
            int NameLength = 4;
            int NameEngLength = 4;
            int WeightLength = 4;
            int PriceLength = 4;
            int CategLength = 4;
            Json.put("Id",NewProduct.getProductID());
            Json.put("Name",NewProduct.getName_Ukr());
            Json.put("Name_eng",NewProduct.getName_Eng());
            Json.put("Category",NewProduct.getProductCateg());
            Json.put("Weight",NewProduct.getWEIGHT());
            Json.put("Price",NewProduct.getPrice().toString());
            String sgson = gson.toJson(Json);
            FileW.write(sgson);
            int CountLength = Integer.toString(count).length();
            if (NewProduct.getProductID() != null)
                IDLenght = NewProduct.getProductID().length();
            if (NewProduct.getName_Ukr() != null)
                NameLength = NewProduct.getName_Ukr().length();
            if (NewProduct.getName_Eng() != null)
                NameEngLength = NewProduct.getName_Eng().length();
            if (NewProduct.getProductCateg() != null)
                CategLength = NewProduct.getProductCateg().length();
            if (NewProduct.getWEIGHT() != null)
                WeightLength = NewProduct.getWEIGHT().length();
            if (NewProduct.getPrice() != null)
                PriceLength = NewProduct.getPrice().toString().length();
            System.out.println(AddSpaces(Integer.toString(count), TrimLenght - CountLength - 15) +
                    AddSpaces(NewProduct.getProductID(), TrimLenght - IDLenght) +
                    AddSpaces(NewProduct.getName_Ukr(), TrimLenght - NameLength) +
                    AddSpaces(NewProduct.getName_Eng(), TrimLenght - NameEngLength) +
                    AddSpaces(NewProduct.getProductCateg(), TrimLenght - CategLength) +
                    AddSpaces(NewProduct.getWEIGHT(), TrimLenght - WeightLength) +
                    AddSpaces(NewProduct.getPrice().toString(), TrimLenght - PriceLength));
            count++;
        }
        while (ListIterator.hasNext());
        Querysession.close();
        FileW.close();
    }

    public void getConnectMS() {
        try {
            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
        } catch (java.sql.SQLException ex) {
            System.out.println("SQL excp");
        }
        try {
            try {
                Class.forName(com.microsoft.sqlserver.jdbc.SQLServerDriver.class.getName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Connection cn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:products",
                    "Developer", "password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] Args) throws IOException {
        {
            {
//                Parser P = new Parser();
//                P.ParserMatch("E://Downloads/Список1.xlsx", "1000");

   ConnectCoverUP Connect = new ConnectCoverUP();
   Connect.getConnectData();


            }
        }
    }
}



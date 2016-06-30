//import Libraries
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.Session;
import java.io.*;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Parser {
    public void ParserMatch(String File,String Supp_ID) {
        File ParsingFile = new File(File);
        InputStream in = null;
        int i = 0;
        int maxrows = 0;
        try {
            in = new FileInputStream(ParsingFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            XSSFWorkbook xlsFile = new XSSFWorkbook(in);
            Sheet sheet = xlsFile.getSheetAt(0);
            //define parsing tags
            String ID = new String();
            String Weight = "";
            String NameEng = "";
            String NameUkr = "";
            String Categ = "";
            Double Price = Double.parseDouble("1");
            Iterator<Row> it = sheet.iterator();
            Boolean WeightFind = true;
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            //cycle in Excel book
            while (it.hasNext()) {
                Row row = it.next();
                Iterator<Cell> cels = row.iterator();
                //make parsing tags empty
                ID = "0000";
                Weight = "";
                NameUkr = "";
                NameEng = "";
                Categ = "";
                Price = Double.parseDouble("1");
                while (cels.hasNext()) {
                    Cell cel = cels.next();
                    //System.out.println(cel.getStringCellValue());
                    if (cel.getColumnIndex() == 0 && row.getRowNum() != 0 && !cel.getStringCellValue().isEmpty()) {
                        ID = cel.getStringCellValue();
                    }
                    if (cel.getColumnIndex() == 1 && row.getRowNum() != 0) {
                        String MainParse = cel.getStringCellValue();
                        Pattern WeightPattern = Pattern.compile("^(\\,|\\.|\\s)((\\d+)(\\s*)(г.|кг.|гр.|кг|гр|л.|мл))|" +
                        "(\\,|\\.|\\s)(\\d+)(\\s*)(\\**)(\\d*)(\\s*)(г.|кг|гр.|кг.|гр|л.|мл)$");
                        Matcher P = WeightPattern.matcher(MainParse);
                        Pattern NamePatternUkr = Pattern.compile("[А-Я]+[А-Я,а-я,\\s,0-9\\`ґєҐЄ´ІіЇї]+[/]");
                        Matcher NameMatcherUkr = NamePatternUkr.matcher(MainParse);
                        Pattern NamePatternEng = Pattern.compile("[/][A-Z][A-Za-z\\s0-9]+");
                        Matcher NameMatcherEng = NamePatternEng.matcher(MainParse);
                        Pattern CategPattern = Pattern.compile("[,][а-яА-Я\\`ґєҐЄ´ІіЇї\\s0-9]+[,]");
                        Matcher CategMatcher = CategPattern.matcher(MainParse);
                        Pattern CategPatternNext = Pattern.compile("[,][\\s][а-яА-Я\\`ґєҐЄ´ІіЇї\\s0-9.]+");
                        Matcher CategMatcherNext = CategPatternNext.matcher(MainParse);
                        Pattern OnlyGoodPattern = Pattern.compile("[A-ZА-Я\\`ґєҐЄ´ІіЇї\\&]+[а-яА-Я\\`ґєҐЄ´ІіЇї\\s]+");
                        Matcher OnlyGoodMatcher = OnlyGoodPattern.matcher(MainParse);
                        Pattern OnlyGoodPatternE = Pattern.compile("[A-Z]+[A-Za-z0-9]+");
                        Matcher OnlyGoodMatcherE = OnlyGoodPatternE.matcher(MainParse);
                        WeightFind = false;
                        //Weight find in string
                        if (P.find()) {
                            WeightFind = true;
                            Weight = P.group();
                            String G = String.valueOf(Weight.charAt(0));
                            if (G.equals(".") || G.equals(",") || G.equals(" ")) {
                                Weight = Weight.substring(1, Weight.length());
                            }
                        }
                        //Name find in string
                        if (NameMatcherUkr.find()) {
                            NameUkr = NameMatcherUkr.group();
                            String G = String.valueOf(NameUkr.charAt(NameUkr.length()-1));
                            if (G.equals("/")) {
                                NameUkr = NameUkr.substring(0, NameUkr.length()-1);
                            }

                        }
                        //Name eng fin in string
                        if (NameMatcherEng.find()) {
                            NameEng = NameMatcherEng.group();
                            String G = String.valueOf(NameEng.charAt(0));
                            if (G.equals("/")) {
                                NameEng = NameEng.substring(1, NameEng.length());
                            }

                        }
                        //if only good foud
                          else if (OnlyGoodMatcher.find()) {
                            NameUkr = OnlyGoodMatcher.group();
                        } else if (OnlyGoodMatcherE.find()) {
                            NameEng = OnlyGoodMatcherE.group();
                        } else if (OnlyGoodMatcher.matches()) {
                            NameUkr = OnlyGoodMatcher.group();
                        }
                        //category match
                        if (CategMatcher.find()) {
                            Categ = CategMatcher.group();
                            String Prod = "";
                            Categ = Categ.replace(",", "");
                            String G = String.valueOf(Categ.charAt(0));
                            if (G.equals(".") || G.equals(",") || G.equals(" ")) {
                                Categ = Categ.substring(1, Categ.length());
                            }
                        }
                        //parsing category from group if no Weight
                        if (!WeightFind && CategMatcherNext.find()) {
                            Categ = CategMatcherNext.group();
                            String Prod = "";
                            Categ = Categ.replace(",", "");
                            String G = String.valueOf(Categ.charAt(0));
                            if (G.equals(".") || G.equals(",") || G.equals(" ")) {
                                Categ = Categ.substring(1, Categ.length());
                            }
                        }
                    }
                    if (cel.getColumnIndex() == 2 && cel.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                        Price = cel.getNumericCellValue();
                    }
                }
                //insert into Hibernate table
                if (ID!="0000") {
                    insert(session, Supp_ID, ID, "", Categ, NameUkr, NameEng, Price, Weight);
                }
            }
            session.getTransaction().commit();
            HibernateUtil.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//Hibernate insert
    public void insert(Session ss,String SuppID,String ID,String ProductGroup,
        String ProductCateg,String NameUkr,String NameEng,Double Price,String Weight){
        //Connection  cn = DriverManager.getConnection("jdbc:postgresql://coverup.cbng8zp9vqtk.sa-east-1.rds.amazonaws.com:5432/postgres", "Developer", "Latitude");
        //Connection  cn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:products", "Developer", "password");
        ProductsEntity Prd = new ProductsEntity();
        Prd.setSupID(SuppID);
        Prd.setProductID(ID);
        Prd.setProductGroup(ProductGroup);
        Prd.setProductCateg(ProductCateg);
        Prd.setName_Ukr(NameUkr);
        Prd.setName_Eng(NameEng);
        Prd.setBrand("");
        Prd.setPrice(Price);
        Prd.setWEIGHT(Weight);
        ss.merge(Prd);//}
    }
//Replace Hibernate IDClass
    public Boolean CheckUnique (Session ss,String SuppID,String ID,Double Price){
        ProductsEntity Prd = new ProductsEntity();
        Boolean F = true;
        String hql = "from ProductsEntity where SupID = :keyword1 and ProductID = :keyword2 and Price = :keyword3";
        Query query = ss.createQuery(hql);
        query.setParameter("keyword1", SuppID);
        query.setParameter("keyword2", ID);
        query.setParameter("keyword3", Price);
        System.out.println(query.list().size());
       if (query.list().size()==0)
        {F = false ;}
        else F = true;
        return F;
    }
    public void testHibernate () {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        ProductsEntity Prd = new ProductsEntity();
        Prd.setBrand("Vasya");
        Prd.setName_Eng("gg");
        Prd.setName_Ukr("ііі");
        Prd.setPrice(10.00);
        Prd.setProductCateg("кофе");
        Prd.setProductGroup("товрм");
        Prd.setProductID("109223");
        Prd.setSupID("0001");
        Prd.setWEIGHT("10 кг.");
        session.save(Prd);
        session.getTransaction().commit();
        HibernateUtil.shutdown();
    }
}



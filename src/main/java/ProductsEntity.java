/**
 * Created by Михаил on 24.01.2016.
 */
import javax.persistence.*;
import java.io.Serializable;
//Products class java
@Entity
@Table(name="Products",uniqueConstraints=@UniqueConstraint(columnNames={"ID"}))
@IdClass(EmbId.class)
public class ProductsEntity implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="ID" , nullable=false, insertable=false, updatable=false)
    private String ID;
    //@NotNull
    @Column(name="SupID" , nullable=false, insertable=false, updatable=false)
    private String SupID;
    @Column(name="ProductID" , nullable=false, insertable=false, updatable=false)
    private String ProductID;
    @Column(name="ProductGroup")
    private String ProductGroup;
    @Column(name="ProductCateg")
    private String ProductCateg;
    @Column(name="Name_Ukr")
    private String Name_Ukr;
    @Column(name="Name_Eng")
    private String Name_Eng;
    @Column(name="Brand")
    private String Brand;
    @Column(name="Price")
    private Double Price;
    @Column(name="WEIGHT")
    private String WEIGHT;
    public ProductsEntity(){
        Name_Ukr = null;
    }
    public ProductsEntity(ProductsEntity s){
        Name_Ukr = s.getName_Ukr();
    }
    public String getSuppID() {
        return SupID;
    }
    public String getID() {
        return ID;
    }
    public String getProductID(){
        return ProductID;
    }
    public String getProductGroup(){
        return ProductGroup;
    }
    public String getProductCateg(){
        return ProductCateg;
    }
    public String getName_Ukr(){
        return Name_Ukr;
    }
    public String getName_Eng(){
        return Name_Eng;
    }
    public String getBrand(){return Brand;}
    public Double getPrice(){
        return Price;
    }
    public String getWEIGHT(){
        return WEIGHT;
    }
    public void setID(String ID){ID = ID;}
    public void setSupID(String si){SupID = si;}
    public void setProductID(String pi){ProductID = pi;}
    public void setProductGroup(String pg){
        ProductGroup = pg;
    }
    public void setProductCateg(String pc){ProductCateg = pc;}
    public void setName_Ukr(String nu){Name_Ukr = nu;}
    public void setName_Eng(String ne){
        Name_Eng = ne;
    }
    public void setBrand(String b){
        Brand = b;
    }
    public void setPrice(Double p){
        Price = p;
    }
    public void setWEIGHT(String w){
        WEIGHT = w;
    }
}
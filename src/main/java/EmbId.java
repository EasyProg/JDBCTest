import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by Михаил on 21.02.2016.
 */
@Embeddable
public class EmbId implements Serializable {
    private String SupID;
    private String ProductID;
    private Double Price;
    EmbId() {}

    public EmbId(String SupID, String ProductID, Double Price) {
        this.SupID = SupID;
        this.ProductID = ProductID;
        this.Price = Price;
    }

    public String getSid() {
        return SupID;
    }
    public Double getPrice() {
        return Price;
    }
    private void setPrice(Double prc) {
    this.Price = prc;
}
    private void setSid(String sid) {
        this.SupID = sid;
    }
    public String getPrdid() {
        return ProductID;
    }
    private void setPrdid(String prdid) {
    this.ProductID = prdid;
    }
//        @Column(name="SupID")
//        String SupID;
//        @Column(name="ProductID")
//        String ProductID;
}
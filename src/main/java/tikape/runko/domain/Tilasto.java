

package tikape.runko.domain;


public class Tilasto {
    
    private String nimi;
    private Integer annoksissa;

    public Tilasto(String nimi, Integer annoksissa) {
        this.nimi = nimi;
        this.annoksissa = annoksissa;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public Integer getAnnoksissa() {
        return annoksissa;
    }

    public void setAnnoksissa(Integer annoksissa) {
        this.annoksissa = annoksissa;
    }
    
    
}

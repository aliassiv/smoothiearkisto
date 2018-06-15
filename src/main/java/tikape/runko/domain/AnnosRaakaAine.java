

package tikape.runko.domain;


public class AnnosRaakaAine {
    
    private Annos annos;
    private RaakaAine raakaAine;
    private Integer jarjestys;
    private String maara;
    private String ohje;

    public AnnosRaakaAine(RaakaAine raakaAine, Annos annos, Integer jarjestys, String maara, String ohje) {
        this.raakaAine = raakaAine;
        this.annos = annos;
        this.jarjestys = jarjestys;
        this.maara = maara;
        this.ohje = ohje;
    }

    public Annos getAnnos() {
        return annos;
    }

    public void setAnnos(Annos annos) {
        this.annos = annos;
    }

    public RaakaAine getRaakaAine() {
        return raakaAine;
    }

    public void setRaakaAine(RaakaAine raakaAine) {
        this.raakaAine = raakaAine;
    }

    public Integer getJarjestys() {
        return jarjestys;
    }

    public void setJarjestys(Integer jarjestys) {
        this.jarjestys = jarjestys;
    }

    public String getMaara() {
        return maara;
    }

    public void setMaara(String maara) {
        this.maara = maara;
    }

    public String getOhje() {
        return ohje;
    }

    public void setOhje(String ohje) {
        this.ohje = ohje;
    }    
    
}

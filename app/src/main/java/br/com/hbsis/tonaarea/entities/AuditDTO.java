package br.com.hbsis.tonaarea.entities;

import java.util.Date;

import br.com.hbsis.tonaarea.util.Mock;

public class AuditDTO implements Comparable<AuditDTO> {

    private String id;
    private String nameRevenda;
    private String name;
    private String product;
    private String nameAuditor;
    private String status;
    private String instant;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameRevenda() {
        return nameRevenda;
    }

    public void setNameRevenda(String namerevenda) {
        this.nameRevenda = namerevenda;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getNameAuditor() {
        return nameAuditor;
    }

    public void setNameAuditor(String nameAuditor) {
        this.nameAuditor = nameAuditor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInstant() {
        return instant;
    }

    public void setInstant(String instant) {
        this.instant = instant;
    }

    @Override
    public int compareTo(AuditDTO o) {
        Date date1 = Mock.parseStringToDate(o.getInstant());
        Date date2 = Mock.parseStringToDate(this.instant);
        if (date1.after(date2)){
            return -1;
        }else if (date1.before(date2)){
            return +1;

        }else{
            return 0;
        }
    }
}

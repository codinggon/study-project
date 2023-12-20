
package com.gon.fitness.web.settings.form;

import com.gon.fitness.domain.zone.Zone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZonesForm {

    //Andong(안동시)/North Gyeongsang
    private String zoneStr;

    private String city;

    private String localNameOfCity;

    private String province;


    public String getCity() {
        return zoneStr.substring(0,zoneStr.indexOf("("));
    }

    public String getLocalNameOfCity() {
        return zoneStr.substring(zoneStr.indexOf("(") + 1,zoneStr.indexOf(")"));
    }

    public String getProvince() {
        return zoneStr.substring(zoneStr.indexOf("/") + 1);
    }

    public static String getZoneStr(Zone zone){
        StringBuilder sb = new StringBuilder();
        sb.append(zone.getCity());
        sb.append("(");
        sb.append(zone.getLocalNameOfCity());
        sb.append(")");
        sb.append("/");
        sb.append(zone.getProvince());
        return sb.toString();
    }

}

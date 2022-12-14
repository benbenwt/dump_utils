package com.ti.mr.getSingleInfo.utils;


import com.ti.mr.getSingleInfo.domain.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ConvertLocation {
    GenerateLocationJson g=new GenerateLocationJson();
    public List<Location> convertToEnglish(ResultSet rs, Map<String,String> convertMap) throws SQLException {
        List<Location> result=new LinkedList<>();
        Location locationBean=new Location();
        while(rs.next())
        {
            String location=rs.getString(1);
            Integer value=rs.getInt(2);
            if(!convertMap.containsKey(location))
            {
                continue;
            }
            locationBean.setLocation(convertMap.get(location));
            locationBean.setValue(value);
            result.add(locationBean);
        }
        System.out.println(result);
        return  result;
    }

    public static void main(String[] args) {
        ConvertLocation convertLocation=new ConvertLocation();
    }

}

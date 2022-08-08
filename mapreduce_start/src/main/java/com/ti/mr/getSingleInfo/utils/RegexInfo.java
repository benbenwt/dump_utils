package com.ti.mr.getSingleInfo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexInfo {
    public String find(String str)
    {

        System.out.println("testRegex");
        String pattern="\"type\": \"malware\",.*\"last_seen\": \"(.{0,15})\"";
        Pattern p= Pattern.compile(pattern,Pattern.DOTALL);
        Matcher matcher= p.matcher(str);
        String last_seen="";
        if(matcher.find())
        {
            last_seen=matcher.group(1);
            System.out.println("last_seen:"+last_seen);
        }

        String pattern1=" \"type\": \"malware\",.*\"malware_types\": \\[(.*\")\\]";
        Pattern p1= Pattern.compile(pattern1,Pattern.DOTALL);
        Matcher matcher1= p1.matcher(str);
        String malware_types="";
        if(matcher1.find())
        {
            malware_types=matcher1.group(1);
            System.out.println("malware_types:"+malware_types);
        }

        String pattern2="\"type\": \"identity\",.*\"name\": \"(.{0,30})\",";
        Pattern p2= Pattern.compile(pattern2,Pattern.DOTALL);
        Matcher matcher2= p2.matcher(str);
        String family="";
        if(matcher2.find())
        {
            family=matcher2.group(1);
            System.out.println("family:"+family);
        }
        return last_seen+"\t"+malware_types+"\t"+family;
    }
    public static void main(String[] args) {
        RegexInfo regexInfo =new RegexInfo();
        regexInfo.find("{\n" +
                "  \"type\": \"bundle\",\n" +
                "  \"id\": \"bundle--d137e17d-8661-414f-a447-be4e11424404\",\n" +
                "  \"objects\": [\n" +
                "    {\n" +
                "      \"type\": \"identity\",\n" +
                "      \"spec_version\": \"2.1\",\n" +
                "      \"id\": \"identity--e5f1b90a-d9b6-40ab-81a9-8a29df4b6b65\",\n" +
                "      \"created_by_ref\": \"identity--f431f809-377b-45e*0-aa1c-6a4751cae5ff\",\n" +
                "      \"created\": \"2016-04-06T20:03:00.000Z\",\n" +
                "      \"modified\": \"2016-04-06T20:03:00.000Z\",\n" +
                "      \"name\": \"ACME Widget, Inc.\",\n" +
                "      \"identity_class\": \"organization\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"malware\",\n" +
                "      \"spec_version\": \"2.1\",\n" +
                "      \"id\": \"malware--31b940d4-6f7f-459a-80ea-9c1f17b5891b\",\n" +
                "      \"created\": \"2016-04-06T20:07:09.000Z\",\n" +
                "      \"modified\": \"2016-04-06T20:07:09.000Z\",\n" +
                "      \"created_by_ref\": \"identity--f431f809-377b-45e0-aa1c-6a4751cae5ff\",\n" +
                "      \"name\": \"Poison Ivy\",\n" +
                "      \"last_seen\": \"2019-03-03\",\n" +
                "      \"malware_types\": [\"adware\", \"Trojian\"]\n" +
                "    }\n" +
                "  ]\n" +
                "}\n");
    }
}

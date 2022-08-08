package com.ti.dump_es;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ReadJson {
    public Map<String,String> readJson(String path) {
        Map<String,String> result=new HashMap<>();
        try {
            File file = new File(path.replace("\"",""));
            System.out.println("path: "+path.replace("\"",""));
            System.out.println(file.exists());
            if (file.exists()) {
                String json_str = FileUtils.readFileToString(file, "UTF-8");
                JSONObject jsonObject = new JSONObject(json_str);
                jsonObject.remove("id");
                json_str=jsonObject.toString();
//                System.out.println(file.getName().replace(".json",""));
//                System.out.println(json_str);
                result.put(file.getName().replace(".json",""),json_str);
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public Map<String,String> readStixDirectory(String directory) {
        File file = new File(directory);
        Map<String,String> result = new HashMap<>();
        if (file.exists()) {
            File[] fs = file.listFiles();
            for (File f : fs) {
                String name = f.getName();
                if (!f.isDirectory() && name.endsWith("json")) {
                    Map<String,String> json_str = readJson(f.getPath());
                    for(Map.Entry<String,String> entry:json_str.entrySet())
                    {
                        if (entry.getValue() != "") {
                            result.put(f.getName(),entry.getValue());
                            System.out.println(f.getName());
                        }
                    }
                }
            }
        }
        return result;
    }

}

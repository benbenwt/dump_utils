package com.ti.mr.getSingleInfo.domain;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.util.List;

public class Info implements Writable {

    private String final_seen;
    private List<String> malware_types;
    private String  family;
    public Info()
    {

    }
    public Info(String final_seen,List<String> malware_types,String family)
    {
        this.final_seen=final_seen;
        this.malware_types=malware_types;
        this.family=family;
    }
    @Override
    public void write(DataOutput dataOutput) throws IOException {
        System.out.println("Info bean write");
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        System.out.println("Info bean read");
    }

    @Override
    public String toString() {
        return final_seen + "\t" + malware_types +"\t"+ family  +"\t";
    }

    public String getFinal_seen() {
        return final_seen;
    }

    public void setFinal_seen(String final_seen) {
        this.final_seen = final_seen;
    }

    public List<String> getMalware_types() {
        return malware_types;
    }

    public void setMalware_types(List<String> malware_types) {
        this.malware_types = malware_types;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }
}

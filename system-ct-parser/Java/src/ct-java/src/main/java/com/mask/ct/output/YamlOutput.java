package com.mask.ct.output;

import com.mask.ct.bean.CommentMate;
import org.yaml.snakeyaml.Yaml;

import java.io.*;

/**
 * Output yaml file.
 * @author mask(mask616@163.com)
 * @date 2020.08.04
 */
public class YamlOutput extends AbstractOutput {

    public YamlOutput() {
        super();
    }

    public YamlOutput(String outputFile) {
        this();
        super.output = outputFile;
    }

    @Override
    public void output(Object object){
        try {
            Yaml yaml = new Yaml();
            yaml.dump(object, new FileWriter(super.output));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

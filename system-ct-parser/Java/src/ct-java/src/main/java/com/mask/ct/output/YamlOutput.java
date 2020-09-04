package com.mask.ct.output;

import com.mask.ct.bean.CommentMate;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

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
        /*Yaml yaml = new Yaml();
        String str = yaml.dumpAs(object, Tag.MAP, null);

        new FileWriter(super.output)
        System.out.println(str);
        System.out.println("==========================");*/

        try (FileWriter fw = new FileWriter(super.output)){
            Yaml yaml = new Yaml();
            String yamlStr = yaml.dumpAs(object, Tag.MAP, null);
            fw.write(yamlStr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

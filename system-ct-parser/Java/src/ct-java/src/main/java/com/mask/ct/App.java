package com.mask.ct;

import com.mask.ct.core.CustomerDoclet;
import com.mask.ct.util.CommondUtil;
import com.mask.ct.util.StringUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * main class
 * @author mask(mask616@163.com)
 * @date 2020.08.04
 */
public class App {

    public static void main(String[] args) throws Exception {
        start(args);
    }

    private static void start(String[] args) throws Exception {
        Options options = CommondUtil.buildCommandLineOptions();

        CommandLine commandLine = CommondUtil.parseCmdLine("ct-java", args, options, new DefaultParser());
        checkOption(commandLine);

        CustomerDoclet.startParse(commandLine.getOptionValue("input"), commandLine.getOptionValue("output"));
    }

    private static void checkOption(CommandLine commandLine) throws Exception {
        if (commandLine == null && !(commandLine.hasOption("h") || commandLine.hasOption("help"))) {
            throw new Exception("parameter is missing.");
        }

        String input = commandLine.getOptionValue("input");
        String output = commandLine.getOptionValue("output");
        if (StringUtils.isEmpty(input)) {
            throw new Exception("input is requeired");
        }

        if (StringUtils.isEmpty(output)) {
            throw new Exception("output is requeired");
        }

        File file = new File(input);
        if (!file.exists() || !file.canRead()) {
            throw new FileNotFoundException("input file not exists");
        }

        try {
            file = new File(output);
            // TODO need deal later.
            /*if (!file.canWrite()) {
                throw new FileNotFoundException("output file can't write");
            }*/
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}

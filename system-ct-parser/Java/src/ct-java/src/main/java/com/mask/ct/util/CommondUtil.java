package com.mask.ct.util;

import org.apache.commons.cli.*;

/**
 * Command line util.
 * @author mask(mask616@163.com)
 * @date 2020.08.04
 */
public class CommondUtil {

    public static Options buildCommandLineOptions(){
        Options options = new Options();
        options.addOption("h","help", false, "Print help");
        options.addOption("input","sourcePath", true, "source path");
        // options.addOption("output","outputPath", true, "output path");
        return options;
    }


    public static CommandLine parseCmdLine(final String appName, String[] args, Options options,
                                           CommandLineParser parser){
        HelpFormatter hf = new HelpFormatter();
        hf.setWidth(110);
        CommandLine commandLine = null;
        try {
            commandLine = parser.parse(options, args);
            if (commandLine.hasOption('h')) {
                hf.printHelp(appName, options, true);
                return null;
            }
        } catch (ParseException e) {
            hf.printHelp(appName, options, true);
        }

        return commandLine;
    }


}

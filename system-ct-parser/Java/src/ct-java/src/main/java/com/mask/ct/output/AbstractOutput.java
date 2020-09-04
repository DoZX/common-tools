package com.mask.ct.output;

/**
 * Abstract output class.
 * @author mask(mask616@163.com)
 * @date 2020.08.04
 */
public class AbstractOutput {

    protected String output;

    public void output(Object object){
        // default print into console.
        System.out.println(object.toString());
    }
}

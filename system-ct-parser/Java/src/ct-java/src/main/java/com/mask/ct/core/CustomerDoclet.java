package com.mask.ct.core;

import com.mask.ct.bean.Attribute;
import com.mask.ct.bean.ClassInfo;
import com.mask.ct.bean.CommentMate;
import com.mask.ct.bean.MethodInfo;
import com.mask.ct.contants.CommentTypeConstants;
import com.mask.ct.output.AbstractOutput;
import com.mask.ct.output.YamlOutput;
import com.sun.javadoc.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Project core.
 * @author mask(mask616@163.com)
 * @date 2020.08.04
 */
public class CustomerDoclet extends Doclet {

    private static String sourcePath;

    private static AbstractOutput output;

    private static List<String> sourceFiles = new ArrayList<>();

    private static CommentMate commentMate = new CommentMate();

    public static boolean start(RootDoc rootDoc) {
        ClassDoc[] classes = rootDoc.classes();
        commentMate.setName("");
        commentMate.setDescription("");
        for (ClassDoc classDoc : classes) {
            // parse class comment
            parseClassDoc(commentMate, classDoc);

            ClassInfo classInfo = commentMate.getClasss().get(commentMate.getClasss().size() - 1);
            // parse field comment
            parseFiledDoc(classInfo, classDoc);

            // parse method comment
            parseMethodDoc(classInfo, classDoc);
            //commentMate.getClasss().add(classInfo);
        }

        return true;
    }

    /**
     * output result
     * @param object
     */
    private static void print(Object object) {
        output.output(object);

    }

    private static void parseFiledDoc(ClassInfo classInfo, ClassDoc classDoc) {
        FieldDoc[] fieldDocs = classDoc.fields(false);
        for (FieldDoc fieldDoc : fieldDocs) {
            Attribute attribute = new Attribute();
            attribute.setName(fieldDoc.name());
            attribute.setDescription(fieldDoc.commentText());
            classInfo.getAttributes().add(attribute);
        }
    }

    private static void parseMethodDoc(ClassInfo classInfo, ClassDoc classDoc) {
        MethodDoc[] methodDocs = classDoc.methods();
        for (MethodDoc methodDoc : methodDocs) {
            MethodInfo methodInfo = new MethodInfo();
            methodInfo.setName(methodDoc.name());
            methodInfo.setDescription(methodDoc.commentText());
            classInfo.getMethods().add(methodInfo);
        }
    }


    private static void parseClassDoc(CommentMate commentMate, ClassDoc classDoc) {
        ClassInfo classInfo = new ClassInfo();
        for (Tag tag : classDoc.tags()) {
            if (CommentTypeConstants.AUTHOR.equalsIgnoreCase(tag.name())) {
                classInfo.setAuth(tag.text());
            }

            if (CommentTypeConstants.DATE.equalsIgnoreCase(tag.name())) {
                classInfo.setDate(tag.text());
            }

            if (CommentTypeConstants.DESCRIPTION.equalsIgnoreCase(tag.name())) {
                classInfo.setDescription(tag.text());
            }
        }

        if (classDoc.commentText() != null && classDoc.commentText().trim().length() > 0) {
            classInfo.setDescription(classDoc.commentText());
        }
        classInfo.setName(classDoc.name());
        commentMate.getClasss().add(classInfo);
    }


    public static void startParse(String input, String output) {
        CustomerDoclet.sourcePath = input;
        String[] modules = sourcePath.split(";");

        for (String module : modules){
            CustomerDoclet.output = new YamlOutput(module + File.separator +output);

            // scan java source file.
            scanSourceFiles(module);

            // obtain comment.
            obtainComment();

            // output result
            print(commentMate);

            reset();
        }
    }

    private static void reset() {
        CustomerDoclet.sourceFiles = new ArrayList<>();
        CustomerDoclet.commentMate = new CommentMate();
    }

    private static void obtainComment() {
        String[] docArgs =  new String[]{"-doclet", CustomerDoclet.class.getName(), ""};

        for (String sourceFile : sourceFiles){
            docArgs[2] = sourceFile;
            com.sun.tools.javadoc.Main.execute(docArgs);
        }

    }

    private static void scanSourceFiles(String sourcePath) {
        File file = new File(sourcePath);

        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File file2 : files) {
                if (file2.isDirectory()) {
                    scanSourceFiles(file2.getAbsolutePath());
                } else {
                    if (file2.isFile() && isSourceFile(file2.getAbsolutePath())){
                        sourceFiles.add(file2.getAbsolutePath());
                    }
                }
            }
        }
    }

    private static boolean isSourceFile(String sourcePath) {
        int index = sourcePath.lastIndexOf(".");
        if (index == -1) {
            return false;
        }
        String extendName = sourcePath.substring(index);
        if (".java".equalsIgnoreCase(extendName)) {
            return true;
        }

        return false;
    }
}

package com.tools.git_tools;

import java.util.List;

public class Utils {
    private static final String TEMP_PATH = "C:\\Users\\Marcos Martins\\Documents\\workspaces\\MULTIDEPLOY\\topaz-temp";
    private static final String ABSOLUTE_PATH = "C:\\Users\\Marcos Martins\\Documents\\workspaces\\MULTIDEPLOY\\topaz\\cdk\\workspace\\multichannel\\investment\\fixed-income";

    public static List<String> replaceWindowsPathList(List<String> path){
        return path.stream()
                .map(x -> x.replace("\\", "/"))
                .toList();
    }

    public static String replaceWindowsPath(String path){
        return path.replace("\\", "/");
    }

    public static String getTempPath(){
        return replaceWindowsPath(TEMP_PATH);
    }

    public static String getAbsolutePath(){
        return replaceWindowsPath(ABSOLUTE_PATH);
    }
}

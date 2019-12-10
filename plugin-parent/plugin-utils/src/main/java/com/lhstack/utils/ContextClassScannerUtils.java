package com.lhstack.utils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ContextClassScannerUtils {

    private static Map<String,Class<?>> cacheMap = new HashMap<>();

    /**
     * 扫描
     * @param path
     * @return
     * @throws Exception
     */
    public static Set<String> scanner(String path) throws Exception {
        File file = new File(path);
        if(!file.exists())
            return new HashSet<>();
        if(isJar(file)){
            initJar(file);
        }else{
            initDirector(file);
        }
        return cacheMap.keySet();
    }

    /**
     * 处理文件夹，递归扫描jar
     * @param file
     * @throws Exception
     */
    private static void initDirector(File file) throws Exception {
        File[] files = file.listFiles();
        for (File f : files) {
            if(f.isDirectory()){
                initDirector(f);
            }else{
                if(isJar(f)){
                    initJar(f);
                }
            }
        }
    }

    /**
     * 初始化jar文件
     * @param file
     * @throws Exception
     */
    private static void initJar(File file) throws Exception {
        JarFile jarFile = new JarFile(file);
        addUrl(file.toURI().toURL());
        Enumeration<JarEntry> entries = jarFile.entries();
        while(entries.hasMoreElements()){
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            if(name.endsWith(".class")){
                String key = name.replace(".class","").replaceAll("/",".");
                try{
                    cacheMap.put(key,Class.forName(key));
                }catch (Error e){

                }
            }
        }
    }


    /**
     * 添加jar到classloader
     * @param url
     * @throws Exception
     */
    public static void addUrl(URL url) throws Exception {
        URLClassLoader urlClassLoader = getURLClassLoader();
        Method method = getAddUrlMethod();
        method.setAccessible(true);
        method.invoke(urlClassLoader,url);
    }

    /**
     * 判断是否是jar文件
     * @param file
     * @return
     */
    private static boolean isJar(File file) {
        if(file.isFile() && file.getName().endsWith(".jar") || file.isFile() && file.getName().endsWith(".zip")){
            return true;
        }
        return false;
    }

    public static URLClassLoader getURLClassLoader(){
        return (URLClassLoader) ClassLoader.getSystemClassLoader();
    }

    private static Method getAddUrlMethod() throws Exception {
        return URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
    }
}

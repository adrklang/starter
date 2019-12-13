package com.lhstack.utils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ContextClassScannerUtils {

    private static Map<String,String> cacheMap = new HashMap<>();

    private static Map<String,List<String>> jarCache = new HashMap<>();
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

    public static Map<String, String> getCacheMap() {
        return cacheMap;
    }

    public static Map<String, List<String>> getJarCache() {
        return jarCache;
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
        if(!jarCache.containsKey(file.getAbsolutePath())){
            List<String> list = new ArrayList<>();
            JarFile jarFile = new JarFile(file);
            addUrl(file.toURI().toURL());
            Enumeration<JarEntry> entries = jarFile.entries();
            while(entries.hasMoreElements()){
                JarEntry jarEntry = entries.nextElement();
                String name = jarEntry.getName();
                if(name.endsWith(".class")){
                    list.add(name);
                    String key = name.replace(".class","").replaceAll("/",".");
                    try{
                        cacheMap.put(key,key);
                    }catch (Error e){

                    }
                }
            }
            jarCache.put(file.getAbsolutePath(),list);
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

    public static ClassLoader getClassLoader(){
        return ClassLoader.getSystemClassLoader() == null ? Thread.currentThread().getContextClassLoader() : ClassLoader.getSystemClassLoader();
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
        if(ClassLoader.getSystemClassLoader() instanceof URLClassLoader){
            return (URLClassLoader) ClassLoader.getSystemClassLoader();
        }

        throw new NullPointerException("URLClassLoader不存在");
    }

    private static Method getAddUrlMethod() throws Exception {

        return URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
    }


}

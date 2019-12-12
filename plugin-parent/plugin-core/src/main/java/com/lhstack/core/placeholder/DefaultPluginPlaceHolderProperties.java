package com.lhstack.core.placeholder;

import com.lhstack.api.properties.PluginPlaceHolderProperties;
import com.lhstack.utils.ContextClassScannerUtils;
import com.lhstack.utils.MapUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultPluginPlaceHolderProperties implements PluginPlaceHolderProperties {

    private static final String PLUGIN_PROPERTIES = "META-INF/plugin.properties";

    private static Map<String,Object> map;

    @Override
    public Map<String, Object> getProperties() {
        init();
        return map;
    }

    public void init() {
        ClassLoader classLoader = ContextClassScannerUtils.getClassLoader();
        List<Properties> list = new ArrayList<>();
        try {
            Enumeration<URL> resources = classLoader.getResources(PLUGIN_PROPERTIES);
            while(resources.hasMoreElements()){
                URL url = resources.nextElement();
                Properties properties = new Properties();
                properties.load(url.openStream());
                list.add(properties);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Map<String, String>> mapList = list.stream().map(MapUtils::propertiesToMap).collect(Collectors.toList());
        map = MapUtils.aggregationMap(mapList.toArray(new HashMap[mapList.size()]));
    }

    @Override
    public long getOrder() {
        return 0;
    }
}

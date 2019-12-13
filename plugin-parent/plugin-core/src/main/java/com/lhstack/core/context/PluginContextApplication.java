package com.lhstack.core.context;

import com.lhstack.api.bind.ParameterBind;
import com.lhstack.api.context.PluginContext;
import com.lhstack.api.initialize.PluginContextApplicationInitialize;
import com.lhstack.api.order.Order;
import com.lhstack.api.properties.PluginPlaceHolderProperties;
import com.lhstack.utils.ContextClassScannerUtils;
import com.lhstack.utils.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class PluginContextApplication implements PluginContext {

    public static final String PLUGIN_FACTORIES = "META-INF/plugin.factories";

    /**
     * 聚合后的properties文件，通过PluginPlaceHolderProperties的getProperties获取配置,后面的相同key会覆盖前面的key
     */
    private static Map<String,Object> aggregationProperties = new HashMap<>();

    /**
     * plugin.factories文件
     */
    private static List<Properties> factories = new ArrayList<>();


    /**
     * 读取的所有class文件
     */
    private static Set<String> CLASS_ESS;

    private static Map<String,Object> beanCache = new ConcurrentHashMap<>();

    private static List<PluginContextApplicationInitialize> pluginContextApplicationInitializes;

    private static List<ParameterBind> parameterBindList;

    private static ReentrantLock reentrantLock = new ReentrantLock();


    public PluginContextApplication(){

    }


    public void init(String path) throws Exception {
        scanner(path);
        initFactories();
        initPreLifeContext();
        initPostPlaceHolderProperties();
        preInit();
        initParameterBind();
        processParameterBind();
    }

    public void scanner(String path) throws Exception {
        CLASS_ESS = ContextClassScannerUtils.scanner(path);
    }
    protected void startInit() {
        pluginContextApplicationInitializes.forEach(item -> {
            item.start(PluginContextApplication.this);
        });
    }

    protected void postInit() {
        pluginContextApplicationInitializes.forEach(item -> {
            item.postInit(PluginContextApplication.this);
        });
    }

    protected void processParameterBind() {
        beanCache.entrySet().forEach(em ->{
            parameterBindList.forEach(item ->{
                try{
                    reentrantLock.lock();
                    Object value = em.getValue();
                    beanCache.put(em.getKey(),item.getObject(aggregationProperties, value));
                }finally {
                    reentrantLock.unlock();
                }
            });
        });
    }

    protected void initParameterBind() {
        parameterBindList = factories.stream().map(item -> item.getProperty(ParameterBind.PLUGIN_CONTEXT_APPLICATION_INITIALIZE))
                .filter(StringUtils::isNotBlank)
                .map(item -> {
                    String[] split = item.split(",");
                    return Arrays.asList(split).stream().map(em -> {
                        try {
                            return (ParameterBind) Class.forName(em).newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
                }).flatMap(List::stream).sorted(Comparator.comparing(Order::getOrder))
                .collect(Collectors.toList());
    }

    /**
     * 生命周期
     */
    protected void preInit() {
        pluginContextApplicationInitializes.forEach(item -> {
            item.preInit(PluginContextApplication.this);
        });
    }

    /**
     * 初始化前置生命周期函数
     */
    protected void initPreLifeContext() {
        pluginContextApplicationInitializes = factories.stream().map(item -> item.getProperty(PluginContextApplicationInitialize.PLUGIN_CONTEXT_APPLICATION_INITIALIZE))
                .filter(StringUtils::isNotBlank)
                .map(item -> {
                    String[] split = item.split(",");
                    return Arrays.asList(split).stream().map(em -> {
                        try {
                            return (PluginContextApplicationInitialize) Class.forName(em).newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
                }).flatMap(item -> item.stream())
                .sorted(Comparator.comparing(Order::getOrder))
                .collect(Collectors.toList());
    }

    /**
     * 初始化 com.lhstack.api.properties.PluginPlaceHolderProperties
     */
    protected void initPostPlaceHolderProperties() {
        List<PluginPlaceHolderProperties> placeHolderProperties = factories.stream().map(item -> item.getProperty(PluginPlaceHolderProperties.PLUGIN_PLACE_HOLDER_PROPERTIES)).filter(StringUtils::isNotBlank).map(item -> {
            String[] split = item.split(",");
            return Arrays.asList(split).stream().map(em -> {
                try {
                    return (PluginPlaceHolderProperties) Class.forName(em).newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
        }).flatMap(item -> item.stream()).filter(ObjectUtils::isNotEmpty).sorted(Comparator.comparing(Order::getOrder)).collect(Collectors.toList());

        List<Map<String, Object>> maps = placeHolderProperties.stream().map(item -> item.getProperties()).collect(Collectors.toList());
        aggregationProperties = MapUtils.aggregationMap(maps.toArray(new Map[maps.size()]));
    }

    /**
     * 读取 PLUGIN_FACTORIES配置文件
     * @throws IOException
     */
    protected void initFactories() throws IOException {
        ClassLoader classLoader = ContextClassScannerUtils.getClassLoader();
        Enumeration<URL> resources = classLoader.getResources(PLUGIN_FACTORIES);
        while(resources.hasMoreElements()){
            URL url = resources.nextElement();
            Properties properties = new Properties();
            properties.load(url.openStream());
            factories.add(properties);
        }
    }


    @Override
    public List<Properties> getFactories() {
        return factories;
    }

    @Override
    public Map<String, Object> getBeanCache() {
        return beanCache;
    }

    @Override
    public Map<String, Object> getAggregationProperties() {
        return aggregationProperties;
    }

    @Override
    public Set<String> getClassEss() {
        return CLASS_ESS;
    }

}

package com.lhstack.api.context;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public interface PluginContext {

    List<Properties> getFactories();

    Map<String, Object> getBeanCache();

    Map<String, Object> getAggregationProperties();

    Set<String> getClassEss();

}

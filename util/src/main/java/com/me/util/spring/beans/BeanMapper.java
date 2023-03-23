package com.me.util.spring.beans;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.me.util.annotations.MappableBean;
import com.me.util.annotations.MappableSingleton;

@Component
public class BeanMapper {

	private final Map<String, Map<String, String>> beanNameMap = new HashMap<>();

	@Autowired
	private ApplicationContext context;

	@PostConstruct
	public void buildMap() {
		final String[] beans = context.getBeanNamesForAnnotation(MappableBean.class);

		for(final String beanName : beans) {
			final MappableBean mapData = context.findAnnotationOnBean(beanName, MappableBean.class);
			final String mapName = mapData.mapName();

			final Map<String, String> typeMap = beanNameMap.getOrDefault(mapName, new HashMap<>());
			typeMap.put(mapData.key(), beanName);
			beanNameMap.put(mapName, typeMap);
		}

		final String[] singeltons = context.getBeanNamesForAnnotation(MappableSingleton.class);

		for(final String beanName : singeltons) {
			final MappableSingleton mapData = context.findAnnotationOnBean(beanName, MappableSingleton.class);
			final String mapName = mapData.mapName();

			final Map<String, String> typeMap = beanNameMap.getOrDefault(mapName, new HashMap<>());
			typeMap.put(mapData.key(), beanName);
			beanNameMap.put(mapName, typeMap);
		}
	}

	public <T> T getBean(final Class<T> type, final String mapName, final String key) {
		return context.getBean(beanName(mapName, key), type);
	}

	public Class<?> getBeanType(final String mapName, final String key) {
		return context.getType(beanName(mapName, key));
	}

	public boolean containsKey(final String mapName, final String key) {
		return map(mapName).containsKey(key);
	}

	public Set<String> keySet(final String mapName) {
		return map(mapName).keySet();
	}

	private String beanName(final String mapName, final String key) {
		final String beanName = map(mapName).get(key);
		if(beanName == null) {
			throw new IllegalArgumentException("No mappable bean defined with map [" + mapName + "] and key [" + key + "]");
		}
		return beanName;
	}

	private Map<String, String> map(final String mapName) {
		return beanNameMap.getOrDefault(mapName, new HashMap<>());
	}

}

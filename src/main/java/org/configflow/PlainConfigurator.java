package org.configflow;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.configflow.library.Library;
import org.configflow.library.LibraryEntry;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Configurator implementation that works with plain old java objects.
 */
public class PlainConfigurator implements Configurator {

    private static final List<String> UNEDITABLE_PROPERTIES = Arrays.asList();

    private Map<Class, Map<String, Prop>> registeredClasses = new HashMap<Class, Map<String, Prop>>();

    private final Kryo serializer;

    public PlainConfigurator() {
        serializer = new Kryo();
        serializer.setRegistrationRequired(true);

        registerType(Library.class);
        registerType(LibraryEntry.class);

        // Register primitive wrapper types
        registerType(Boolean.class);
        registerType(Byte.class);
        registerType(Short.class);
        registerType(Integer.class);
        registerType(Long.class);
        registerType(Float.class);
        registerType(Double.class);
        registerType(Character.class);
        registerType(String.class);

        // Register collection types
        registerType(Set.class);
        registerType(Collection.class);
        registerType(List.class);
        registerType(Map.class);
        registerType(HashSet.class);
        registerType(ArrayList.class);
        registerType(LinkedList.class);
        registerType(HashMap.class);
        registerType(LinkedHashMap.class);

    }

    @Override public void registerTypes(Class... configurationObjectType) {
        for (Class type : configurationObjectType) {
            registerType(type);
        }
    }

    @Override public void registerType(Class configurationObjectType) {
        final Method[] methods = configurationObjectType.getMethods();

        // Find getters
        Map<String, Method> getters = new HashMap<String, Method>();
        for (Method method : methods) {
            final String name = getGetterName(method);
            if (name != null) {
                getters.put(name, method);
            }
        }

        // Find properties with both getter and setter
        Map<String, Prop> properties = new HashMap<String, Prop>();
        for (Method method : methods) {
            final String name = getSetterName(method);
            if (name != null) {
                final Method getter = getters.get(name);
                if (getter != null) {
                    // Both getter and setter found
                    properties.put(name, new Prop(name, getter.getReturnType(), getter, method));
                }
            }
        }

        registeredClasses.put(configurationObjectType, properties);

        // Register with serializer
        serializer.register(configurationObjectType);
    }

    private String getGetterName(Method method) {
        final String name = method.getName();
        if (method.getParameterTypes().length != 0) return null;
        else if (UNEDITABLE_PROPERTIES.contains(name)) return null;
        else if (name.startsWith("get")) return name.substring(3);
        else if (name.startsWith("is")) return name.substring(2);
        else if (name.startsWith("has")) return name.substring(3);
        else return null;
    }

    private String getSetterName(Method method) {
        final String name = method.getName();

        if (method.getParameterTypes().length != 1) return null;
        else if (name.startsWith("set")) return name.substring(3);
        else return null;
    }

    @Override public Collection<Prop> getProperties(Object conf) {
        return getPropMap(conf).values();
    }

    @Override public Collection<String> getPropertyNames(Object conf) {
        return getPropMap(conf).keySet();
    }

    @Override public void setProperty(Object conf, String name, Object value) {
        getProp(conf, name).setValue(conf, value);
    }

    @Override public Object getProperty(Object conf, String name) {
        return getProp(conf, name).getValue(conf);
    }

    @Override public <T> T deepCopy(T conf) {
        return serializer.copy(conf);
    }

    @Override public <T> T loadConf(InputStream inputStream, Class<T> expectedType) {
        Input input = new Input(inputStream);
        final T result = serializer.readObject(input, expectedType);
        input.close();
        return result;
    }

    @Override public void saveConf(OutputStream outputStream, Object conf) {
        final Output output = new Output(outputStream);
        serializer.writeObject(output, conf);
        output.close();
    }

    private Map<String, Prop> getPropMap(Object conf) {
        final Map<String, Prop> stringPropMap = registeredClasses.get(conf.getClass());
        if (stringPropMap == null) throw new ConfigurationException("Unregistered type: "+conf.getClass());
        return stringPropMap;
    }

    private Prop getProp(Object conf, String name) {
        final Prop prop = getPropMap(conf).get(name);
        if (prop == null) throw new ConfigurationException("No property named '"+name+"' found for class '"+conf.getClass()+"'");
        return prop;
    }
}

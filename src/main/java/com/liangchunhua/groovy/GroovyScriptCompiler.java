package com.liangchunhua.groovy;

import com.liangchunhua.util.MD5HashUtil;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.runtime.InvokerHelper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class GroovyScriptCompiler {
    private static Map<String, GroovyClass> classMap = new ConcurrentHashMap<>();

    /**
     * @param file groovy script file will be initialized and cached for use.
     */
    public static void initializeGroovyScript(File file){
        if(file == null || !file.exists() || file.isHidden()) return;

        try (GroovyClassLoader groovyClassLoader = new GroovyClassLoader(GroovyScriptCompiler.class.getClassLoader())) {
           Class<?> clazz = groovyClassLoader.parseClass(file);
           if(!clazz.isInterface()){
               String fileMD5String = MD5HashUtil.MD5.getFileMD5String(file);
               if(StringUtils.isNotBlank(fileMD5String)) {
                   if(!classMap.containsKey(fileMD5String)
                           || classMap.get(fileMD5String) == null
                           || classMap.get(fileMD5String).getLastModifiedTime() != file.lastModified()) {
                       GroovyClass groovyClass = new GroovyClass();
                       groovyClass.setClazz(clazz);
                       groovyClass.setLastModifiedTime(file.lastModified());
                       classMap.put(fileMD5String, groovyClass);
                   }
               }
           }
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
    }

    /**
     *
     * @param file groovy script file will be invoked, before invoking the script should be cached.
     * @return return the groovy script instance.
     */
    public static GroovyScriptInstance<Script> getScriptInstance(File file){
        if(file == null || !file.exists() || file.isHidden()) return null;
        String fileMD5String = MD5HashUtil.MD5.getFileMD5String(file);
        if(StringUtils.isNotBlank(fileMD5String)){
            Map<String, Object> context = new HashMap<>();
            Binding bindingContext = new Binding(context);
            GroovyClass cacheClass = null;
            if(!classMap.containsKey(fileMD5String)
                    || (cacheClass = classMap.get(fileMD5String)) == null
                    || cacheClass.getLastModifiedTime() != file.lastModified()) {
                    initializeGroovyScript(file);
            }
            Class<?> clazz = null;
            if(cacheClass != null && (clazz = cacheClass.getClazz()) != null){
                Script script = InvokerHelper.createScript(clazz, bindingContext);
                GroovyScriptInstance<Script> instance = new GroovyScriptInstance<>();
                instance.setScriptInstance(script);
                instance.setScriptFileLastModifiedTime(cacheClass.lastModifiedTime);
                return instance;
            }
        }
        return null;
    }

    /**
     * internal class is used to cache the groovy script class, if the script file last modified time is not equal to
     * cache script class modified time. The cached class must be refreshed.
     */
    public static class GroovyClass{
        private Class<?> clazz;
        private long lastModifiedTime;

        public Class<?> getClazz() {
            return clazz;
        }

        public void setClazz(Class<?> clazz) {
            this.clazz = clazz;
        }

        public long getLastModifiedTime() {
            return lastModifiedTime;
        }

        public void setLastModifiedTime(long lastModifiedTime) {
            this.lastModifiedTime = lastModifiedTime;
        }


    }
}


package com.liangchunhua.groovy;

import groovy.lang.Script;

import java.util.Map;

/**
 * @author liangchunhua
 * Groovy script instance class contains script class, scprit object(instance), script file last modified time.
 * If the script file is changed instantly, this object need to be refreshed.
 * When groovy script was initialized and invoked, the groovy script context bindings shoud be set
 * and returned the result.
 */
public class GroovyScriptInstance<T> {
    private T scriptInstance;
    private long scriptFileLastModifiedTime;

    public T getScriptInstance() {
        return scriptInstance;
    }

    public void setScriptInstance(T scriptInstance) {
        this.scriptInstance = scriptInstance;
    }

    public long getScriptFileLastModifiedTime() {
        return scriptFileLastModifiedTime;
    }

    public void setScriptFileLastModifiedTime(long scriptFileLastModifiedTime) {
        this.scriptFileLastModifiedTime = scriptFileLastModifiedTime;
    }

    /**
     * Invoke script object and return result
     * @param bindings groovy script inputs arguments
     * @return script return result
     */
    @SuppressWarnings("unchecked")
    public Object get(Map bindings){
        if(this.scriptInstance instanceof Script){
            Script script = (Script) this.scriptInstance;
            if(bindings != null) {
                script.getBinding().getVariables().putAll(bindings);
            }
            Object object = script.run();
            script = null;
            return object;
        }
        return this.scriptInstance;
    }

}
package com.liangchunhua.groovy;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class GroovyResourceLoader {
    public static void loadResource(String rootPath){
        Path path = Paths.get(rootPath);
        File file = null;
        if((file = path.toFile()).exists() && file.isDirectory()){
           try(DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)){
                directoryStream.forEach(p ->{
                    File f = p.toFile();
                    if(f == null || !f.exists() || f.isHidden()){
                        return;
                    }
                    if(f.isDirectory()){
                        loadResource(f.getPath());
                    }else{
                        System.out.println(f.toPath());
                        GroovyScriptCompiler.initializeGroovyScript(f);
                    }
                    log.debug(f.getPath());
                });
           } catch (IOException e) {
              log.debug(e.getMessage());
           }
        }
    }
}

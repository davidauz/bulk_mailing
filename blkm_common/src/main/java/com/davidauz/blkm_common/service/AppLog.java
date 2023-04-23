package com.davidauz.blkm_common.service;


import com.davidauz.blkm_common.entity.LogItem;
import com.davidauz.blkm_common.repo.LogMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

@Service
public class AppLog {

    final static int APPLOG_INFO = 1;
    @Autowired
    private LogMessageRepository lmrepo;

    public void log(String str) {
        log(str, APPLOG_INFO);
    }

    public void log(String str, int level){
        LogItem li=new LogItem();
        li.setValue(str);
        li.setLevel(level);
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stackTrace[3];
        li.setModule(caller.getClassName()+":"+caller.getMethodName());
        lmrepo.save(li);
    }
}

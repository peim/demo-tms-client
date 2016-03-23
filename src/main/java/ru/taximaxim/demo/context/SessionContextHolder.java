package ru.taximaxim.demo.context;

import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.stereotype.Repository;

@Repository
public class SessionContextHolder {

    private CopyOnWriteArraySet<String> sessionContexts;
    
    public SessionContextHolder() {
        sessionContexts = new CopyOnWriteArraySet<String>();
    }
    
    public CopyOnWriteArraySet<String> getContexts() {
        return sessionContexts;
    }
    
    public boolean addContext(String context) {
        return sessionContexts.add(context);
    }
    
    public boolean removeContext(String context) {
        return sessionContexts.remove(context);
    }
}

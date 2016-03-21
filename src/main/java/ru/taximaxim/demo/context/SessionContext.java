package ru.taximaxim.demo.context;

import org.springframework.stereotype.Component;

@Component
public class SessionContext {

    private String user;
    private String context;

    public SessionContext() {
        System.err.println("SessionContext - init");
    }

    public SessionContext(String user, String context) {
        this.user = user;
        this.context = context;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the context
     */
    public String getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext(String context) {
        this.context = context;
    }
}

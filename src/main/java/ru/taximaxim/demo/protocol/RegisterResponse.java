package ru.taximaxim.demo.protocol;

public class RegisterResponse {

    private String context;
    private String queue;
    private String canSpyOper;

    public RegisterResponse() {}

    public RegisterResponse(String context, String queue, String canSpyOper) {
        this.context = context;
        this.queue = queue;
        this.canSpyOper = canSpyOper;
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

    /**
     * @return the queue
     */
    public String getQueue() {
        return queue;
    }

    /**
     * @param queue the queue to set
     */
    public void setQueue(String queue) {
        this.queue = queue;
    }

    /**
     * @return the canSpyOper
     */
    public String getCanSpyOper() {
        return canSpyOper;
    }

    /**
     * @param canSpyOper the canSpyOper to set
     */
    public void setCanSpyOper(String canSpyOper) {
        this.canSpyOper = canSpyOper;
    }
}

package proxy.impl;

import proxy.external.HttpClient;

public class HttpClientImpl implements HttpClient {
    @Override
    public void initialize() {
        System.out.println("Initializing HTTP Client");
        try{
            Thread.sleep(500);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override
    public String sendRequest(String request) {
        System.out.println("Sending request: " + request);
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("received response");
        return "someResponse data";
    }
}

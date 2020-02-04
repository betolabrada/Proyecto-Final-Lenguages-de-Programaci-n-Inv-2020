    
package com.mycompany.proyectofinallp;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Buffer {
    
    private LinkedList<Product> buffer;
    private List<Producer> producers;
    private List<Consumer> consumers;
    private Updater updater;
    
    private int n;
    
    public Buffer(int n, Updater updater) {
        this.buffer = new LinkedList<>();
        this.n = n;
        
        this.producers = new LinkedList<>();
        this.consumers = new LinkedList<>();
     
        this.updater = updater;
    }
    
    public int getSize() {
        return this.buffer.size();
    }
    
    
    synchronized Product consume() {
        Product product;
        
        
//        System.out.println("Consuming in bufffer: " + this);
        while(this.buffer.isEmpty()) {
            try {
//                System.out.println(Thread.currentThread().getName() +" Waiting to consume...\n");
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
//        System.out.println(Thread.currentThread().getName() + " trying to consume product");
        product = this.buffer.remove();
        this.updater.updateConsumer("1"/*consumer.getIdConsumer*/, product.getProduct(), product.getIdProducer(), "0", this.buffer.size());
//        System.out.println(Thread.currentThread().getName() + " consumed " + product);
//        System.out.println("Buffer size after consumption: " + this.buffer.size() + "\n");

        notify();
        return product;
    }
    
    synchronized void produce(Product product) {
        while(this.buffer.size() == this.n) {
            try {
//                System.out.println("Buffer is full, waiting for consumers...");
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
//        System.out.println(Thread.currentThread().getName() + " produced " + product);
        this.buffer.add(product);
        this.updater.updateProducer(product.getIdProducer(), product.getProduct(), this.buffer.size());
        
//        System.out.println("Buffer size: " + this.buffer.size() + "\n");
        
        notify();
    }
    
    static int count = 1;
    synchronized static void print(String string) {
        System.out.print(count++ + " ");
        System.out.println(string);
    }
    
}

package com.example.demo1;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.beans.*;
public class Queue {
    int teściwo = 109;
    ArrayList<LinkedList<String>> listOfChanges = new ArrayList<>(); //lista list zawierających dane o wywołanych metodach
    ArrayList<LinkedList<String>> secondList = new ArrayList<>();
    public void save() throws IOException {
        Queue queue = new Queue();
        LinkedList<String> sd = new LinkedList<>();
        sd.add("studia");
        sd.add("szkoła");
        queue.listOfChanges.add(sd);
        String jeden = "abc";
        String dwa = "cde2";
        queue.addOperation(jeden,dwa);

        XMLEncoder xmlEncoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("queue.xml")));
        xmlEncoder.writeObject(queue.listOfChanges); //zapisuje obiekt THIS klasy w xml
        xmlEncoder.close();

        XMLDecoder xmlDecoder = new XMLDecoder(new BufferedInputStream(new FileInputStream("queue.xml")));
        secondList = (ArrayList<LinkedList<String>>) xmlDecoder.readObject();
        System.out.println(secondList);
    }
    public void addOperation(String method,String arguments){
        LinkedList<String> list = new LinkedList<>(); //lista zawierająca nazwe metody i argumenty
        list.add(method);
        list.addAll(List.of(arguments));
        listOfChanges.add(list); //dodanie jej do głównej listy
    }

    public void invokeMetods() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?> myclass = Class.forName("com.example.demo1.Controller");
        Method method = myclass.getMethod("test", String.class);
        Object object = myclass.newInstance();
        String output = (String)method.invoke(object,"hello there");
        System.out.println(output);

    }
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Queue queueChanges = new Queue();
       // queueChanges.save();
        queueChanges.invokeMetods();
    }
}

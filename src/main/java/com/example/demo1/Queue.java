package com.example.demo1;

import javax.swing.text.html.HTMLEditorKit;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.beans.*;
public class Queue {
    ArrayList<Integer> listOfmetods = new ArrayList<>();//lista list zawierających dane o wywołanych metodach
    ArrayList<long[]> kList = new ArrayList<>();
    ArrayList<String> textInputList = new ArrayList<>();
    ArrayList<Double> numberInputList = new ArrayList<>();

    //metoda jest zgarniana razem z ustawieniem jej globalnie
    // k jest zgarniane ze slidera tak jak na początku operationChoser()
    //textInput i numberInput są zgarniane ze zmiennych globalnych przed wywołaniem operationChoser()(może nie działać)
    public void save(int metoda, long[] k,String textInput,Double numberInput) throws IOException {

        listOfmetods.add(metoda);
        kList.add(k);
        //zapisywanie w dwóch listach nazwy metody i parametru k

        XMLEncoder xmlEncoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("listOfmetods.xml")));
        xmlEncoder.writeObject(listOfmetods); //zapisuje list of changes w xml
        xmlEncoder.close();


        XMLEncoder xmlEncoder2 = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("kList.xml")));
        xmlEncoder2.writeObject(kList); //zapisuje list of changes w xml
        xmlEncoder2.close();

        XMLEncoder xmlEncoder3 = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("textInputList.xml")));
        xmlEncoder3.writeObject(textInputList); //zapisuje list of changes w xml
        xmlEncoder3.close();

        XMLEncoder xmlEncoder4 = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("numberInputList.xml")));
        xmlEncoder4.writeObject(numberInputList); //zapisuje list of changes w xml
        xmlEncoder4.close();

    }
    //otwiera odpowiednie pliki xml z kazdego odczytuje arrayliste i podstawia pod zmienną globalną
    public void load() throws FileNotFoundException {
        XMLDecoder xmlDecoder2 = new XMLDecoder(new BufferedInputStream(new FileInputStream("listOfmetods.xml")));
        listOfmetods = (ArrayList<Integer>) xmlDecoder2.readObject();

        XMLDecoder xmlDecoder = new XMLDecoder(new BufferedInputStream(new FileInputStream("kList.xml")));
        kList = (ArrayList<long[]>) xmlDecoder.readObject();

        XMLDecoder xmlDecoder3 = new XMLDecoder(new BufferedInputStream(new FileInputStream("textInputList.xml")));
        textInputList = (ArrayList<String>) xmlDecoder.readObject();

        XMLDecoder xmlDecoder4 = new XMLDecoder(new BufferedInputStream(new FileInputStream("numberInputList.xml")));
        numberInputList = (ArrayList<Double>) xmlDecoder.readObject();


        //System.out.println(secondList);

    }



//    public void invokeMetods() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
//        Class<?> myclass = Class.forName("com.example.demo1.Controller");
//        Method method = myclass.getMethod("test", String.class);
//        Object object = myclass.newInstance();
//        String output = (String)method.invoke(object,"hello there");
//        System.out.println(output);
//
//    }
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Queue queueChanges = new Queue();
        //queueChanges.save();

    }
}

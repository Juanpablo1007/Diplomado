package util;

import Persintencia.Person;

import java.util.ArrayList;

public class Base64Tester02 {

    public static void main(String[] args) throws Exception {

        Person person = new Person("Fransisco",29,1.78);


        System.out.println(person);

        byte[] personBA = Util.objectToByteArray(person);
        String personB64 = Base64.encode(personBA);
        System.out.println(personB64);

        byte[] namesBA2 = Base64.decode(personB64);
        Person names2 = (Person) Util.byteArrayToObject(namesBA2);
        System.out.println(names2);
    }
}

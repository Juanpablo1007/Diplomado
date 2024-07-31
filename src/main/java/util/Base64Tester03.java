package util;

import Persintencia.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Base64Tester03 {
    public static void main(String[] args) throws Exception {
        List<Person> personList = new ArrayList<>();
        personList.add(new Person("Fransisco",29,1.78));
        personList.add(new Person("Enrique",28,1.77));
        personList.add(new Person("Mike",27,1.76));

        byte[] personBA = Util.objectToByteArray(personList);
        String personB64 = Base64.encode(personBA);
        System.out.println(personB64);

        byte[] namesBA2 = Base64.decode(personB64);
        List<Person> names2 = (List<Person>) Util.byteArrayToObject(namesBA2);
        System.out.println(names2);
    }
}

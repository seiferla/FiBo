package de.dhbw.ka.se.fibo.utils;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ApiUtilsTest {

    @Test(expected = IllegalStateException.class)
    public void testNotAllowedConstructor() throws Throwable {
        //use reflection to avoid the need of a public constructor
        Constructor<ApiUtils> c = ApiUtils.class.getDeclaredConstructor();
        c.setAccessible(true);
        try {
            c.newInstance(); // create ApiUtils class
        } catch (InvocationTargetException e) {
            throw e.getTargetException();  //throw the exception that is wrapped by InvocationTargetException
        }
    }

}

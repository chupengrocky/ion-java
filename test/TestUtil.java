import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestUtil {

    public static Object invoke(Object methodHostInstance, String methodName,
                                Object arg) {
        Class<?>[] parameterTypes = { arg.getClass() };
        Object[] args = { arg };
        return invoke(methodHostInstance, methodName, parameterTypes, args);
    }


    public static Object invoke(Object methodHostInstance, String methodName,
                                Class<?>[] parameterTypes, Object[] args) {
        try {
            Method method = methodHostInstance.getClass().getDeclaredMethod(
                    methodName, parameterTypes);
            method.setAccessible(true);
            try {
                return method.invoke(methodHostInstance, args);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            method.setAccessible(false);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
/**
 * Created by pvnghe on 8/19/17.
 */

package pvnghe.patternslibrary.designpatterns;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Task {

    private static final List<Registration<?>> mRegistrationList = new ArrayList<Registration<?>>();

    public static synchronized <T extends ActionTask> Registration<T> register(
            Object tag,
            Class<T> mClass,
            Assigner<T> assigner) {
        Registration<T> registration = new Registration<T>(tag, mClass, assigner);
        mRegistrationList.add(registration);
        return registration;
    }

    private static synchronized void unregister(Registration<?> registration) {
        mRegistrationList.remove(registration);
    }

    public static synchronized void unregister(Object tag) {
        for (Iterator<Registration<?>> iter = mRegistrationList.iterator(); iter.hasNext();) {
            Registration<?> registration = iter.next();
            if (registration.mTag == tag) {
                iter.remove();
            }
        }
    }

    public static synchronized <T extends ActionTask> void publish(T object) {
        Log.d(Task.class.getSimpleName(), object.toString());
        List<Registration<?>> matchedList = new ArrayList<Registration<?>>();
        for (Registration<?> registration : mRegistrationList) {
            if (registration.mClass.isInstance(object)) {
                matchedList.add(registration);
            }
        }
        for (Registration<?> registration : matchedList) {
            ((Assigner<T>) registration.mAssigner).onRun(object);
        }
    }

    public static synchronized void clear() {
        mRegistrationList.clear();
    }

    public static class Registration<T> {

        private final Object mTag;

        private final Class<T> mClass;

        private final Assigner<T> mAssigner;


        public Registration(Object tag, Class<T> objectClass, Assigner<T> assigner) {
            mTag = tag;
            mClass = objectClass;
            mAssigner = assigner;
        }

        public void cancel() {
            unregister(this);
        }

        @Override
        public String toString() {
            return "{Class=" + mClass + ", assigner=" + mAssigner + "}";
        }

    }

    public interface Assigner<T> {
        public void onRun(T object);
    }

    public interface ActionTask {}
}

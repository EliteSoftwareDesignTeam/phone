package com.teamness.smane.event;

import com.teamness.smane.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by samtebbs on 13/02/2018.
 */

public class EventChannel {

    public enum EventPriority {
        HIGH, MEDIUM, LOW
    }

    public EventPriority defaultPriority;

    public final List<EventChannel> parents;

    private final Map<Class<? extends Event>, Map<EventPriority, List<Pair<Method, Object>>>> subscribers = new HashMap<>();
    private final Map<EventPriority, List<Pair<Method, Object>>> defaultSubscribers = new HashMap<EventPriority, List<Pair<Method,Object>>>() {{
        for(EventPriority p : EventPriority.values()) put(p, new ArrayList<Pair<Method, Object>>());
    }};

    public EventChannel() {
        this(EventPriority.MEDIUM);
    }

    public EventChannel(EventPriority defaultPriority) {
        this(defaultPriority, defaultPriority);
    }

    public EventChannel(EventPriority defaultPriority, EventPriority parentPriority, EventChannel... parentChannels) {
        this.parents = Arrays.asList(parentChannels);
        this.defaultPriority = defaultPriority;
        for(EventChannel parent : parents) parent.onAny(parentPriority, "triggerChild", this);
    }

    /**
     * Trigger an event and its listeners
     * @param event the event to trigger
     * @param <T> the type of the event
     */
    public <T extends Event> void trigger(T event) {
        triggerChild(event);
        for(EventChannel parent : parents) parent.trigger(event);
    }

    private <T extends Event> void triggerChild(T event) {
        if(subscribers.containsKey(event.getClass())) {
            Map<EventPriority, List<Pair<Method, Object>>> consumers = subscribers.get(event.getClass());
            for(EventPriority priority : EventPriority.values()) {
                for(Pair<Method, Object> consumer : consumers.get(priority)) invoke(consumer, event);
            }
        }
        for(EventPriority priority : EventPriority.values()) for(Pair<Method, Object> consumer : defaultSubscribers.get(priority)) invoke(consumer, event);
    }

    private <T extends Event> void invoke(Pair<Method, Object> consumer, T event) {
        try {
            Method method = consumer.first;
            method.invoke(consumer.second, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Register an event listener
     * @param priority the listener's priority
     * @param eventClass the class of the event to listen for
     * @param method the name of the method to invoke when the event is triggered
     * @param caller the object to call the method from
     * @param <T> the type of the event
     */
    public <T extends Event> boolean on(EventPriority priority, Class<T> eventClass, String method, Object caller) {
        Pair<Method, Object> consumer = newConsumer(caller, method, eventClass);
        if(consumer == null) return false;
        if(subscribers.containsKey(eventClass)) subscribers.get(eventClass).get(priority).add(consumer);
        else {
            Map<EventPriority, List<Pair<Method, Object>>> map = new HashMap<EventPriority, List<Pair<Method, Object>>>() {{
                for(EventPriority priority : EventPriority.values()) {
                    put(priority, new ArrayList<Pair<Method, Object>>());
                }
            }};
            map.get(priority).add(consumer);
            subscribers.put(eventClass, map);
        }
        return true;
    }

    /**
     * Register an event listener that is called when any event type is triggered
     * @param priority the listener's priority
     * @param method the name of the method to invoke when the event is triggered
     * @param caller the object to call the method from
     * @return
     */
    public boolean onAny(EventPriority priority, String method, Object caller) {
        Pair<Method, Object> consumer = newConsumer(caller, method, Event.class);
        if(consumer == null) return false;
        defaultSubscribers.get(priority).add(consumer);
        return true;
    }

    /**
     * Creates a consumer pair
     * @param caller the object to call the method from
     * @param methodName the name of the method to call
     * @param eventClass the event class that the method takes
     * @param <T> the type of the event
     * @return null if a consumer pair can't be created (no methods with that name and parameter class), else the new consumer pair
     */
    public <T extends Event> Pair<Method, Object> newConsumer(Object caller, String methodName, Class<T> eventClass) {
        if(caller != null) {
            try {
                boolean isStatic = caller instanceof Class;
                Class<?> callerClass = isStatic ? (Class<?>) caller : caller.getClass();
                Method method = callerClass.getMethod(methodName, eventClass);
                return new Pair<>(method, isStatic ? null : caller);
            } catch (NoSuchMethodException ignored) {}
        }
        return null;
    }

    /**
     * Same as {@link EventChannel#on(EventPriority, Class, String, Object)} but with medium priority
     * @param eventClass the class of the event to listen for
     * @param method the method name to invoke when the event is triggered
     * @param caller the object to call the method from
     * @param <T> the type of the event
     */
    public <T extends Event> boolean on(Class<T> eventClass, String method, Object caller) {
        return on(defaultPriority, eventClass, method, caller);
    }

}

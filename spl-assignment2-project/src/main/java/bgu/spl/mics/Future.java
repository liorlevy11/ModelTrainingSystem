package bgu.spl.mics;

import java.util.concurrent.TimeUnit;

/**
 * A Future object represents a promised result - an object that will
 * eventually be resolved to hold a result of some operation. The class allows
 * Retrieving the result once it is available.
 * <p>
 * Only private methods may be added to this class.
 * No public constructor is allowed except for the empty constructor.
 */

//@INV: if (isDone() == false) -> get() = BLOCKED
public class Future<T> {

    /**
     * This should be the the only public constructor in this class.
     */
    private volatile boolean isFinished;
    private T value;

    public Future() {
        isFinished = false;
        value = null;
    }

    /**
     * retrieves the result the Future object holds if it has been resolved.
     * This is a blocking method! It waits for the computation in case it has
     * not been completed.
     * <p>
     * @return return the result of type T if it is available, if not wait until it is available.
     *
     */


    /**
     * @PRE: none
     * @POST: trivial
     */
    public synchronized T get() {
        //TODO: implement this.
        System.out.println("Future try to get");
        while (!isFinished) {
            System.out.println("Future try to get- isfinish?" + isFinished);
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.isFinished = value==null?false:true;
        return value;

    }


    /**
     * Resolves the result of this Future object.
     */

    /**
     * @PRE: isDone() == false
     * @POST: isDone() == true && value=result && get() !BLOCKED
     */
    public synchronized void resolve(T result) {
        //TODO: implement this.
        System.out.println("Future->resolve->the result->" + result);
        if (!isDone()) {
            if (result == null) {
                System.out.println("we are in finish Training");
                notifyAll();
                isFinished = true;
                System.out.println("we are in finish Training-successful");
            } else {
                System.out.println("Future->resolve->is not Done");
                value = result;
                isFinished = true;
                System.out.println("Future->resolve->is notDone->value " + value + " isFinish-> " + isFinished);
                System.out.println("Future->resolve->tell to do notifyAll");
                notifyAll();
                System.out.println("Future->resolve->tell to do notifyAll------>successful");
            }
        }

    }

    /**
     * @return true if this object has been resolved, false otherwise
     */
    /**
     * @PRE: none
     * @POST: trivial
     */
    public boolean isDone() {
        //TODO: implement this.
        return isFinished;
    }

    /**
     * retrieves the result the Future object holds if it has been resolved,
     * This method is non-blocking, it has a limited amount of time determined
     * by {@code timeout}
     * <p>
     * @param timeout    the maximal amount of time units to wait for the result.
     * @param unit        the {@link TimeUnit} time units to wait.
     * @return return the result of type T if it is available, if not,
     * 	       wait for {@code timeout} TimeUnits {@code unit}. If time has
     *         elapsed, return null.
     */
    /**
     * @PRE: get()->BLOCKED
     * @POST: get()!=BLOCKED && isDone() -> T
     * @POST: get()==BLOCKED && !isDone() -> null
     */
    public synchronized T get(long timeout, TimeUnit unit) {
        //TODO: implement this.

        while (!isDone()) {
            try {
                wait(unit.toSeconds(timeout));
                return value;//can be null if not resolved
            } catch (InterruptedException ignored) {
            }
        }
        this.isFinished = value==null?false:true;
        return value;

    }

    public String toString() {
        String s = "lololololololololololo" + isFinished;
        return s;
    }
}
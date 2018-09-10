package hq.demo.net.utils;

public class ExceptonUtils {

    public static void handlerException(Throwable e) {
        handlerException(e,null);
    }
    public static void handlerException(Throwable e, ExceptionHandler0 handler) {
        if (handler!=null){
            handler.handle();
        }
    }


    public static void handlerException1(Throwable e, ExceptionHandler1 handler) {
        if (handler!=null){
            handler.handle(e);
        }
    }


    public interface ExceptionHandler0{
        void handle();
    }

    public interface ExceptionHandler1{
        void handle(Throwable e);
    }
}

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.List;
/**
* 1. 什么情况下使用initialize和close方法呢？
* 在拦截器中读取外部数据例如Redis等数据时，在initialize中建立连接，在close中关闭连接
* 2. 为什么有两个intercept？
* 第一个是按个处理的，第二个按批处理，只需要循环调用第一个就可以。
* Source有的有batchSize，有的没有，intercept重载就是解决不同的场景
*/
public class CustomInterceptor implements Interceptor {


    @Override
    public void initialize() {

    }

    @Override
    public Event intercept(Event event) {

        byte[] body = event.getBody();
        if (body[0] < 'z' && body[0] > 'a') {
            event.getHeaders().put("type", "letter");
        } else if (body[0] > '0' && body[0] < '9') {
            event.getHeaders().put("type", "number");
        }
        return event;

    }

    @Override
    public List<Event> intercept(List<Event> events) {
        for (Event event : events) {
            intercept(event);
        }
        return events;
    }

    @Override
    public void close() {

    }

    public static class Builder implements Interceptor.Builder {

        @Override
        public Interceptor build() {
            return new CustomInterceptor();
        }

        @Override
        public void configure(Context context) {
        }
    }
}

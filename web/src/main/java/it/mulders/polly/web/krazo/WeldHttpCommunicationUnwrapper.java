package it.mulders.polly.web.krazo;

import jakarta.annotation.Priority;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.krazo.core.HttpCommunicationUnwrapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

@Priority(1)
public class WeldHttpCommunicationUnwrapper implements HttpCommunicationUnwrapper {
    private static final Logger logger = Logger.getLogger(WeldHttpCommunicationUnwrapper.class.getName());

    @Override
    public boolean supports(Object obj) {
        try {
            Class<?> weldClientProxyClass = Class.forName(
                    "org.jboss.weld.proxy.WeldClientProxy",
                    false,
                    obj.getClass().getClassLoader());
            return weldClientProxyClass.isInstance(obj);
        } catch (ClassNotFoundException cnfe) {
            logger.log(Level.FINE, "Probably not using Weld", cnfe);
            return false;
        }
    }

    @Override
    public HttpServletRequest unwrapRequest(HttpServletRequest obj, Class<HttpServletRequest> type) {
        try {
            //
            // Invoke WeldClientProxy#getMetadata()
            //
            Class<?> weldClientProxyClass = Class.forName(
                    "org.jboss.weld.proxy.WeldClientProxy",
                    false,
                    obj.getClass().getClassLoader());
            Method getMetadataMethod = weldClientProxyClass.getDeclaredMethod("getMetadata");
            Object weldClientProxyMetadata = getMetadataMethod.invoke(obj);

            //
            // Invoke WeldClientProxy.Metadata#getContextualInstance()
            //
            Class<?> weldClientProxyMetadataClass = Class.forName(
                    "org.jboss.weld.proxy.WeldClientProxy$Metadata",
                    false,
                    obj.getClass().getClassLoader());
            Method getContextualInstanceMethod =
                    weldClientProxyMetadataClass.getDeclaredMethod("getContextualInstance");
            Object contextualInstance = getContextualInstanceMethod.invoke(weldClientProxyMetadata);

            return (HttpServletRequest) contextualInstance;
        } catch (ClassNotFoundException
                | NoSuchMethodException
                | IllegalAccessException
                | InvocationTargetException e) {
            throw new RuntimeException("Encountered exception when unwrapping Weld request object", e);
        }
    }

    @Override
    public HttpServletResponse unwrapResponse(HttpServletResponse obj, Class<HttpServletResponse> type) {
        return null;
    }
}

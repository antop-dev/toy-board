package extensions

import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry

object InterceptorRegistry {
    /**
     * Adds an interceptor to the registry.
     *
     * @param interceptor the interceptor to add
     */
    operator fun InterceptorRegistry.plusAssign(interceptor: HandlerInterceptor) {
        this.addInterceptor(interceptor)
    }
}

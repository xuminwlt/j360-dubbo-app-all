package me.j360.dubbo.trace.brave.util;

/**
 * Package: me.j360.dubbo.trace.brave.util
 * User: min_xu
 * Date: 2017/4/28 下午6:43
 * 说明：
 */
public interface PatternMatcher {
    /**
     * Returns <code>true</code> if the given <code>source</code> matches the specified <code>pattern</code>,
     * <code>false</code> otherwise.
     *
     * @param pattern the pattern to match against
     * @param source  the source to match
     * @return <code>true</code> if the given <code>source</code> matches the specified <code>pattern</code>,
     *         <code>false</code> otherwise.
     */
    boolean matches(String pattern, String source);

}

package me.j360.dubbo.trace.brave.http;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.ServerSpan;
import com.twitter.zipkin.gen.Span;

import javax.servlet.http.HttpServletRequest;

/**
 * Package: me.j360.dubbo.trace.brave.http
 * User: min_xu
 * Date: 2017/4/27 下午8:16
 * 说明：
 */
public final class MaybeAddClientHeaderFromRequest  {

    final Brave brave;

    public static MaybeAddClientHeaderFromRequest create(Brave brave) {
        return new MaybeAddClientHeaderFromRequest(brave);
    }

    MaybeAddClientHeaderFromRequest(Brave brave) { // intentionally hidden
        this.brave = brave;
    }


    public final void accept(HttpServletRequest input) {
        // Kick out if we can't read the current span
        ServerSpan serverSpan = brave.serverSpanThreadBinder().getCurrentServerSpan();
        Span span = serverSpan != null ? serverSpan.getSpan() : null;
        if (span == null) return;

        //span.addToBinary_annotations();
    }
}

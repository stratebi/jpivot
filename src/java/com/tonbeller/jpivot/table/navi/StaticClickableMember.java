package com.tonbeller.jpivot.table.navi;

import javax.servlet.http.HttpSession;

import org.w3c.dom.Element;

import com.tonbeller.jpivot.olap.model.OlapModel;
import com.tonbeller.jpivot.param.ParameterProvider;
import com.tonbeller.jpivot.table.SpanBuilder;
import com.tonbeller.jpivot.table.SpanBuilderDecorator;
import com.tonbeller.jpivot.table.TableComponent;
import com.tonbeller.jpivot.table.span.Span;
import com.tonbeller.wcf.controller.RequestContext;

/**
 * wraps a ClickableMemberSupport into a SpanBuilderDecorator, so the clickable member
 * may be attached statically to the table component. Once attached, the clickable
 * will be available independent of the current query.
 * 
 * @author av
 * @since 15.12.2004
 */
public class StaticClickableMember extends SpanBuilderDecorator {
  class MyClickableMember extends ClickableMemberSupport {
    public MyClickableMember(String uniqueName, String urlPattern, String page,
        ParameterProvider parameterProvider) {
      super(uniqueName, urlPattern, page, parameterProvider);
    }

    protected OlapModel getOlapModel() {
      return table.getOlapModel();
    }
  };

  MyClickableMember clickable;

  public StaticClickableMember(SpanBuilder delegate, String uniqueName, String urlPattern,
      String page, ParameterProvider parameterProvider) {
    super(delegate);
    clickable = new MyClickableMember(uniqueName, urlPattern, page, parameterProvider);
  }

  public void initialize(RequestContext context, TableComponent table) throws Exception {
    super.initialize(context, table);
    table.getDispatcher().addRequestListener(null, null, clickable);
    table.getOlapModel().addModelChangeListener(clickable);
  }

  public void destroy(HttpSession session) throws Exception {
    table.getDispatcher().removeRequestListener(clickable);
    table.getOlapModel().removeModelChangeListener(clickable);
    super.destroy(session);
  }

  public void startBuild(RequestContext context) {
    super.startBuild(context);
    clickable.startRendering(context, table);
  }

  public void stopBuild() {
    clickable.stopRendering();
    super.stopBuild();
  }

  public Element build(Span span, boolean even) {
    Element elem = super.build(span, even);
    clickable.decorate(elem, span.getObject());
    return elem;
  }
  
}
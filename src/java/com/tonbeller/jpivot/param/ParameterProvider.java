/*
 * Created on 14.12.2004
 *
 */
package com.tonbeller.jpivot.param;

import java.util.Collection;

import com.tonbeller.jpivot.olap.model.Member;
import com.tonbeller.jpivot.olap.model.OlapModel;


/**
 * creates a list of SessionParameter from a member.
 * @see com.tonbeller.wcf.param.SessionParam
 */
public interface ParameterProvider {
  public Collection createSessionParams(OlapModel model, Member member);
}
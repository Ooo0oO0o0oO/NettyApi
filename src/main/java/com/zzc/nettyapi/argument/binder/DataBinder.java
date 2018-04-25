package com.zzc.nettyapi.argument.binder;

import com.google.common.collect.Maps;
import com.zzc.nettyapi.Exception.ConvertException;
import java.util.Map;

import com.zzc.nettyapi.argument.BeanWrapper;
import com.zzc.nettyapi.request.RequestDetail;

/**
 * @author zhengzechao
 * @date 2018/4/22
 * Email ooczzoo@gmail.com
 */
public interface DataBinder {


    void doBinder(Object attribute, RequestDetail requestDetail,Class Type);

    Object convertIfNecessary(Class type, String value) throws ConvertException;
}
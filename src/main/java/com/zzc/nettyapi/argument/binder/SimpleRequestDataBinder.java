package com.zzc.nettyapi.argument.binder;

import com.zzc.nettyapi.exception.ConvertException;
import com.zzc.nettyapi.argument.conversion.DefaultConversion;
import com.zzc.nettyapi.request.RequestDetail;

/**
 * @author zhengzechao
 * @date 2018/4/22
 * Email ooczzoo@gmail.com
 */
public class SimpleRequestDataBinder implements DataBinder {




    private DefaultConversion conversion;


    public SimpleRequestDataBinder(DefaultConversion conversion) {
        this.conversion = conversion;

    }

    @Override
    public void doBinder(Object attribute, RequestDetail requestDetail,Class type) {
    }

    @Override
    public Object convertIfNecessary(Class sourceClass,Class targetClass, Object value ) throws ConvertException {
        return conversion.convertIfNecessary(sourceClass, targetClass, value);
    }


}

package me.j360.dubbo.common.api;

import java.io.Serializable;

public abstract class BaseRequest implements Serializable{

    public interface ParamFilter{
        public void filter();
    }

    public interface Validation{
        public void validation();
    }

    public void execute(ParamFilter paramFilter){
        paramFilter.filter();
    }

    public void execute(Validation validation){
        validation.validation();
    }

    public abstract BaseRequest checkArguments();

}

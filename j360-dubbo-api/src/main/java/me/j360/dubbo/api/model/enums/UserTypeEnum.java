package me.j360.dubbo.api.model.enums;

/**
 * Package: me.j360.dubbo.entity.enums
 * User: min_xu
 * Date: 16/8/23 下午2:37
 * 说明：
 */
public enum  UserTypeEnum{

    ASSISTANT("ASSISTANT", "助理"),
    DOCTOR("DOCTOR", "顾问"),
    EXPERT("EXPERT", "专科");

    UserTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    String value;
    String desc;
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static UserTypeEnum getByValue(String value){
        for (UserTypeEnum tmp : UserTypeEnum.values()) {
            if (tmp.getValue().equals(value)) {
                return tmp;
            }
        }
        return null;
    }
}

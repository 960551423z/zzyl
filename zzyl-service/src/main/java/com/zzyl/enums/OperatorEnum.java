package com.zzyl.enums;

/**
 * @author 阿庆
 */
public enum OperatorEnum {
    GE{
        @Override
        public boolean isOverthreshold(float threshold, float v) {
            return Float.compare(threshold, v) >= 0;
        }
    },
    LT {
        @Override
        public boolean isOverthreshold(float threshold, float v) {
            return Float.compare(threshold, v) < 0;
        }
    },
    ;

    public abstract boolean isOverthreshold(float threshold, float v);
}

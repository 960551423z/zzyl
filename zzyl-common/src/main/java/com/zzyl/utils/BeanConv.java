package com.zzyl.utils;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/***
 * 对象转换工具，当对象成员变量属性：名称及类型相同时候会自动填充其值
 */
@Slf4j
public class BeanConv {

    private static MapperFacade mapper;

    private static MapperFacade notNullMapper;

    static {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        ConverterFactory converterFactory = mapperFactory.getConverterFactory();
        converterFactory.registerConverter(new LocalDateTimeConverter());
        converterFactory.registerConverter(new LocalDateConverter());
        converterFactory.registerConverter(new LocalTimeConverter());
        mapper = mapperFactory.getMapperFacade();
        MapperFactory notNullMapperFactory = new DefaultMapperFactory.Builder().mapNulls(false).build();
        notNullMapper = notNullMapperFactory.getMapperFacade();
    }

    private static class LocalDateTimeConverter extends BidirectionalConverter<LocalDateTime, LocalDateTime> {

        @Override
        public LocalDateTime convertTo(LocalDateTime localDateTime, Type<LocalDateTime> type, MappingContext mappingContext) {
            return LocalDateTime.from(localDateTime);
        }

        @Override
        public LocalDateTime convertFrom(LocalDateTime localDateTime, Type<LocalDateTime> type, MappingContext mappingContext) {
            return LocalDateTime.from(localDateTime);
        }
    }
    private static class LocalDateConverter extends BidirectionalConverter<LocalDate, LocalDate> {
        @Override
        public LocalDate convertTo(LocalDate localDate, Type<LocalDate> type, MappingContext mappingContext) {
            return LocalDate.from(localDate);
        }

        @Override
        public LocalDate convertFrom(LocalDate localDate, Type<LocalDate> type, MappingContext mappingContext) {
            return LocalDate.from(localDate);
        }
    }
    private static class LocalTimeConverter extends BidirectionalConverter<LocalTime, LocalTime> {

        @Override
        public LocalTime convertTo(LocalTime localTime, Type<LocalTime> type, MappingContext mappingContext) {
            return LocalTime.from(localTime);
        }

        @Override
        public LocalTime convertFrom(LocalTime localTime, Type<LocalTime> type, MappingContext mappingContext) {
            return LocalTime.from(localTime);
        }
    }


    /***
     *  深度复制对象
     *
     * @param source 源对象
     * @param destinationClass 目标类型
     * @return
     */
    public static <T> T toBean(Object source, Class<T> destinationClass) {
        if (EmptyUtil.isNullOrEmpty(source)){
            return null;
        }
        return mapper.map(source, destinationClass);
    }

    /***
     *  复制List
     *
     * @param sourceList 源list对象
     * @param destinationClass 目标类型
     * @return
     */
    public static <T> List<T> toBeanList(List<?> sourceList, Class<T> destinationClass) {
        if (EmptyUtil.isNullOrEmpty(sourceList)){
            return new ArrayList<>();
        }
        return mapper.mapAsList(sourceList,destinationClass);
    }

}

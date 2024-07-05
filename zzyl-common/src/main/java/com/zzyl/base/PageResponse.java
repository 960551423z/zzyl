package com.zzyl.base;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.Page;
import com.zzyl.utils.ConvertHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * 分页结果包装
 *
 * @author 阿庆
 */
@Data
@ApiModel(value = "分页数据消息体", description = "分页数据统一对象")
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PageResponse<T> {

    @ApiModelProperty(value = "总条目数", required = true)
    private Long total = 0L;

    @ApiModelProperty(value = "页尺寸", required = true)
    private Integer pageSize = 0;

    @ApiModelProperty(value = "总页数", required = true)
    private Long pages = 0L;

    @ApiModelProperty(value = "页码", required = true)
    private Integer page = 0;

    @ApiModelProperty(value = "数据列表", required = true)
    private List<T> records = Collections.EMPTY_LIST;


    /**
     * 通过mybatis的分页对象构造对象，不封装 items 属性
     *
     * @param page 分页对象
     */
    public PageResponse(Page<?> page) {
        this.page = Convert.toInt(page.getPageNum());
        this.total = page.getTotal();
        this.pageSize = Convert.toInt(page.getPageSize());
        this.pages = (long) page.getPages();
    }

    /**
     * 通过mybatis的分页对象构造对象，封装 items 属性
     *
     * @param page  分页对象
     * @param clazz 指定items 属性的类型
     */
    public PageResponse(Page<?> page, Class<T> clazz) {
        this.page = Convert.toInt(page.getPageNum());
        this.total = page.getTotal();
        this.pageSize = Convert.toInt(page.getPageSize());
        this.pages = (long) page.getPages();
    }

    /**
     * 返回一个分页对象实例
     *
     * @return 分页数据对象
     */
    public static <T> PageResponse<T> getInstance() {
        return PageResponse.<T>builder().build();
    }

    /**
     * Page{@link Page}对象封装为PageResponse,不封装 items 属性
     *
     * @param page 源分页对象
     * @return 目标分页数据对象
     */
    public static <T> PageResponse<T> of(Page<?> page) {
        //封装分页数据
        return PageResponse.<T>builder()
                .page(Convert.toInt(page.getPageNum()))
                .pageSize(Convert.toInt(page.getPageSize()))
                .pages((long) page.getPages())
                .total(page.getTotal())
                .build();
    }

    /**
     * Page{@link Page}对象封装为PageResponse,
     * 并将Page中的Records转换为指定类型封装为items
     *
     * @param page 源分页对象
     * @return 目标分页数据对象
     */
    public static <T> PageResponse<T> of(Page<?> page, Class<T> clazz) {
        return of(page, clazz, null);
    }

    /**
     * Page{@link Page}对象封装为PageResponse,
     * 并将Page中的Records转换为指定类型封装为items
     *
     * @param page           源分页对象
     * @param convertHandler 特殊对象类型转换器，可传null，即不进行特殊处理
     * @return 目标分页数据对象
     */
    public static <O, T> PageResponse<T> of(Page<O> page, Class<T> clazz, ConvertHandler<O, T> convertHandler) {
        //封装分页数据
        return PageResponse.<T>builder()
            .page(Convert.toInt(page.getPageNum()))
            .pageSize(Convert.toInt(page.getPageSize()))
            .pages((long) page.getPages())
            .total(page.getTotal())
            .records(copyToList(page.getResult(), clazz, convertHandler))
            .build();
    }

    /**
     * 对items进行类型转换
     *
     * @param origin 源分页数据对象
     * @param clazz  指定items 属性的类型,不能为null
     * @return 目标分页数据对象
     */
    public static <O, T> PageResponse<T> of(PageResponse<O> origin, Class<T> clazz) {
        return of(origin, clazz, null);
    }

    /**
     * 对items进行类型转换
     *
     * @param origin         源分页数据对象
     * @param clazz          指定items 属性的类型,不能为null
     * @param convertHandler 特殊对象类型转换器，可传null，即不进行特殊处理
     * @return 目标分页数据对象
     */
    public static <O, T> PageResponse<T> of(PageResponse<O> origin, Class<T> clazz, ConvertHandler<O, T> convertHandler) {
        //复制除items外的属性
        PageResponse<T> target = PageResponse.getInstance();
        BeanUtil.copyProperties(origin, target, "items");

        //items为空，直接返回
        if (CollUtil.isEmpty(origin.getRecords())) {
            return target;
        }
        List<T> targetList = copyToList(origin.getRecords(), clazz, convertHandler);
        target.setRecords(targetList);

        //封装分页数据
        return target;
    }

    /**
     * List{@link List}封装为分页数据对象
     *
     * @param items    item数据
     * @param page     页码,可不传,数据不为空时默认为1
     * @param pageSize 页尺寸,可不传,数据不为空时默认为1
     * @param pages    页尺寸,可不传,数据不为空时默认为1
     * @param counts   总条目数,可不传,数据不为空时默认为1
     * @return 目标分页数据对象
     */
    public static <T> PageResponse<T> of(List<T> items, Integer page, Integer pageSize, Long pages, Long counts) {
        //封装分页数据
        PageResponse<T> pageResponse = PageResponse.<T>builder()
                .page(Optional.ofNullable(page).orElse(1))
                .pageSize(Optional.ofNullable(pageSize).orElse(1))
                .pages(Optional.ofNullable(pages).orElse(1L))
                .total(Optional.ofNullable(counts).orElse(1L))
                .build();

        if (CollUtil.isEmpty(items)) {
            return pageResponse;
        }

        pageResponse.setRecords(items);
        return pageResponse;
    }

    /**
     * List{@link List}封装为分页数据对象
     * 数据不为空时，page、pageSize、pages、counts均默认为1
     *
     * @param items item数据
     * @return 目标分页数据对象
     */
    public static <T> PageResponse<T> of(List<T> items) {
        return of(items, null, null, null, null);
    }

    /**
     * 返回包含任意数量元素的分页对象
     * 数据不为空时，page、pageSize、pages、counts均默认为1
     *
     * @param elements items元素
     * @return 目标分页数据对象
     */
    @SafeVarargs
    public static <E> PageResponse<E> of(E... elements) {
        return of(List.of(elements));
    }

    /**
     * 对items进行类型转换
     *
     * @param origin         源分页数据对象
     * @param function 自定义函数
     * @return 目标分页数据对象
     */
    public static <O, T> PageResponse<T> of(PageResponse<O> origin, Function<List<O>, List<T>> function) {
        List<T> orderVOList = function.apply(origin.getRecords());
        return PageResponse.of(orderVOList, origin.getPage(), origin.getPageSize(), origin.getPages(), origin.total);
    }

    /**
     * 转换结构
     * @param content
     * @param clazz
     * @param convertHandler
     * @param <T>
     * @param <O>
     * @return
     */
    private static <T, O> List<T> copyToList(List<O> content, Class<T> clazz, ConvertHandler<O, T> convertHandler) {
        //对items进行类型转换
        List<T> targetList = BeanUtil.copyToList(content, clazz);
        //特殊类型转换
        //特殊类型转换
        if (CollUtil.isNotEmpty(targetList) && ObjectUtil.isNotEmpty(convertHandler)) {
            for (int i = 0; i < content.size(); i++) {
                convertHandler.map(content.get(i), targetList.get(i));
            }
        }
        return targetList;
    }
}

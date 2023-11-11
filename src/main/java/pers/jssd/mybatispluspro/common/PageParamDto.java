package pers.jssd.mybatispluspro.common;

import lombok.Data;

/**
 * @author jssd
 */
@Data
public class PageParamDto<E> {

    private long page;

    private long size;

    private String asc;

    private String desc;
}

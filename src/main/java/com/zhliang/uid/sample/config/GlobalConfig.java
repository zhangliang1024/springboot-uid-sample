package com.zhliang.uid.sample.config;

import com.pzy.zhliang.uid.leaf.SegmentServiceImpl;
import com.pzy.zhliang.uid.spring.ColumnMaxValueIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @创建人：zhiang
 * @创建时间：2020/7/1 19:35
 * @version：V1.0
 */
@Configuration
public class GlobalConfig {

    //@Autowired
    //private JdbcTemplate jdbcTemplate;
    //
    //@Bean
    //public SegmentServiceImpl segmentService(){
    //    SegmentServiceImpl segmentService = new SegmentServiceImpl(jdbcTemplate,"order");
    //    segmentService.setAsynLoadingSegment(true);
    //    return segmentService;
    //}
    //
    //@Bean
    //public ColumnMaxValueIncrementer productNoIncrementer(){
    //    ColumnMaxValueIncrementer columnMaxValueIncrementer = new ColumnMaxValueIncrementer(jdbcTemplate,"productNo");
    //    columnMaxValueIncrementer.setPaddingLength(6);
    //    return columnMaxValueIncrementer;
    //}
}

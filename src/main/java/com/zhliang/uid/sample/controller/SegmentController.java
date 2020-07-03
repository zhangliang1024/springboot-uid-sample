//package com.zhliang.uid.sample.controller;
//
//import com.pzy.zhliang.uid.leaf.SegmentServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.jdbc.core.BatchPreparedStatementSetter;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
//import org.springframework.transaction.TransactionStatus;
//import org.springframework.transaction.support.TransactionCallback;
//import org.springframework.transaction.support.TransactionTemplate;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.TimeUnit;
//
///**
// * @项目名称：springboot-uid-sample
// * @包名：com.zhliang.uid.sample.controller
// * @类描述：
// * @创建人：zhiang
// * @创建时间：2020/7/1 19:41
// * @version：V1.0
// */
//@RestController
//@RequestMapping("seg")
//public class SegmentController {
//
//
//    @Autowired
//    @Qualifier("productNoIncrementer")
//    private DataFieldMaxValueIncrementer incrementer;
//
//    @GetMapping("incre")
//    public void testINcre() {
//        int i = 0;
//        while (i < 1000) {
//            System.out.println("long id=" + incrementer.nextLongValue());
//            System.out.println("int id=" + incrementer.nextIntValue());
//            System.out.println("string id=" + incrementer.nextStringValue());
//            i++;
//        }
//    }
//
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//    @Autowired
//    private TransactionTemplate transactionTemplate;
//
//    private BlockingQueue<Long> queue = new LinkedBlockingQueue<Long>(1000);
//
//    @Autowired
//    @Qualifier("segmentService")
//    private SegmentServiceImpl segmentServiceImpl;
//
//    @GetMapping("leaf")
//    public void synGetId() {
//        int i =0;
//        while (i < 100) {
//            System.out.println(++i +" ：" + segmentServiceImpl.getId());
//        }
//    }
//
//    @GetMapping("queue")
//    public void getId() {
//        new Thread() {
//            @Override
//            public void run() {
//                List<Long> list = new ArrayList<Long>(10000);
//                while (true) {
//                    try {
//                        Long id = queue.take();
//                        // jdbcTemplate.update("insert into id_test(p_id) values(?)",
//                        // l);
//                        // System.out.println("id=" + id);
//                        if (list.size() == 10000) {
//                            final List<Long> insertedList = list;
//                            transactionTemplate.execute(new TransactionCallback<Integer>() {
//                                @Override
//                                public Integer doInTransaction(TransactionStatus status) {
//                                    jdbcTemplate.batchUpdate("insert into id_test(p_id) values(?)", new BatchPreparedStatementSetter() {
//                                        @Override
//                                        public void setValues(PreparedStatement ps, int i)
//                                                throws SQLException {
//                                            Long insertedId = insertedList.get(i);
//                                            ps.setLong(1, insertedId);
//                                        }
//                                        @Override
//                                        public int getBatchSize() {
//                                            return insertedList.size();
//                                        }
//                                    });
//                                    return insertedList.size();
//                                }
//                            });
//                            list.clear();
//                        } else {
//                            list.add(id);
//                        }
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        }.start();
//
//        int count = 0;
//        while (true) {
//            // System.out.println(idLeafService.getId());
//            try {
//                queue.put(segmentServiceImpl.getId());
//                count++;
//                if (count % 1000 == 0) {
//                    System.out.println("current count no is " + count);
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @GetMapping("table")
//    public void batchInsert() {
//        List<Long> list = new ArrayList<Long>(1000);
//        for (Long i = 0L; i < 1000L; i++) {
//            list.add(i);
//        }
//        try {
//            TimeUnit.SECONDS.sleep(3);
//        } catch (InterruptedException e1) {
//            e1.printStackTrace();
//        }
//        try {
//
//            final List<Long> insertedList = list;
//            transactionTemplate.execute(new TransactionCallback<Integer>() {
//                @Override
//                public Integer doInTransaction(TransactionStatus status) {
//                    jdbcTemplate.batchUpdate("insert into id_test(p_id) values(?)", new BatchPreparedStatementSetter() {
//                        @Override
//                        public void setValues(PreparedStatement ps, int i) throws SQLException {
//                            Long insertedId = insertedList.get(i);
//                            ps.setLong(1, insertedId);
//                        }
//                        @Override
//                        public int getBatchSize() {
//                            return insertedList.size();
//                        }
//                    });
//                    return insertedList.size();
//                }
//            });
//            System.out.println("oooolk");
//        } catch (Exception e) {
//        }
//    }
//}

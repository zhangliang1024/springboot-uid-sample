package com.zhliang.uid.sample.controller;

import com.pzy.zhliang.uid.baidu.UidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;


/**@类描述：测试百度UID获取ID
 * @创建人：zhiang
 * @创建时间：2020/7/1 19:37
 * @version：V1.0
 */
@RestController
@RequestMapping("gen")
public class UidGeneratorController {

    @Autowired
    private UidGenerator uidOne;

    @Autowired
    private UidGenerator uidTwo;



    @GetMapping("")
    public void test() {
        // Generate UID
        long uid = uidOne.getUID();
        System.out.println("one:" + uid);
        System.out.println("one:" + uidOne.getUID());
        // Parse UID into [Timestamp, WorkerId, Sequence]
        // {"UID":"180363646902239241","parsed":{ "timestamp":"2017-01-19 12:15:46", "workerId":"4", "sequence":"9" }}
        System.out.println("one:" + uidOne.parseUID(uid));

        Runnable threadOne= new  Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    System.out.println("one:" + uidOne.getUID());
                }
            }
        };
        Runnable threadTwo= new  Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    System.out.println("two:" + uidTwo.getUID());
                }
            }
        };
        threadOne.run();
        threadTwo.run();

        uid = uidTwo.getUID();
        System.out.println("two:" + uid);
        System.out.println("two:" + uidTwo.getUID());
        // Parse UID into [Timestamp, WorkerId, Sequence]
        // {"UID":"180363646902239241","parsed":{ "timestamp":"2017-01-19 12:15:46", "workerId":"4", "sequence":"9" }}
        System.out.println("two:" + uidTwo.parseUID(uid));
    }

    //private static final int SIZE = 100000; // 10w
    //private static final boolean VERBOSE = true;
    //private static final int THREADS = Runtime.getRuntime().availableProcessors() << 1;


    private static final int SIZE = 7000000; // 700w
    private static final boolean VERBOSE = false;
    private static final int THREADS = Runtime.getRuntime().availableProcessors() << 1;


    @Resource
    private UidGenerator uidGenerator;

    /**
     * Test for serially generate
     */
    @GetMapping("general")
    public void testSerialGenerate() {
        // Generate UID serially
        Set<Long> uidSet = new HashSet<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            doGenerate(uidSet, i);
        }
        // Check UIDs are all unique
        checkUniqueID(uidSet);
    }

    /**
     * Test for parallel generate
     *
     * @throws InterruptedException
     */
    @GetMapping("generals")
    public void testParallelGenerate() throws InterruptedException {
        final AtomicInteger control = new AtomicInteger(-1);
        final Set<Long> uidSet = new ConcurrentSkipListSet<>();

        // Initialize threads
        List<Thread> threadList = new ArrayList<>(THREADS);
        for (int i = 0; i < THREADS; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    workerRun(uidSet, control);
                }
            });
            thread.setName("UID-generator-" + i);
            threadList.add(thread);
            thread.start();
        }
        // Wait for worker done
        for (Thread thread : threadList) {
            thread.join();
        }
        // Check generate 10w times
        if(SIZE != control.get()){
            throw new RuntimeException();
        }
        // Check UIDs are all unique
        checkUniqueID(uidSet);
    }

    public int updateAndGet(AtomicInteger control) {
        int prev, next;
        do {
            prev = control.get();
            next = prev == SIZE ? SIZE : prev + 1;
        } while (!control.compareAndSet(prev, next));
        return next;
    }

    /**
     * Worker run
     */
    private void workerRun(Set<Long> uidSet, AtomicInteger control) {
        for (;;) {
            int myPosition = updateAndGet(control);
            if (myPosition == SIZE) {
                return;
            }
            doGenerate(uidSet, myPosition);
        }
    }

    /**
     * Do generating
     */
    private void doGenerate(Set<Long> uidSet, int index) {
        long uid = uidGenerator.getUID();
        String parsedInfo = uidGenerator.parseUID(uid);
        uidSet.add(uid);

        // Check UID is positive, and can be parsed
        if(uid < 0L){
            throw new RuntimeException();
        }
        if(StringUtils.isEmpty(parsedInfo)){
            throw new RuntimeException();
        }
        if (VERBOSE) {
            System.out.println(Thread.currentThread().getName() + " No." + index + " >>> " + parsedInfo);
        }
    }

    /**
     * Check UIDs are all unique
     */
    private void checkUniqueID(Set<Long> uidSet) {
        System.out.println(uidSet.size());
        if(SIZE != uidSet.size()){
            throw new RuntimeException();
        }
    }
}


    @Bean
    public WorkerIdAssigner workerIdAssigner(){
        if(uidProperties.getZookeeper().isEnabled()){
            ZkWorkerIdAssigner workerIdAssigner = new ZkWorkerIdAssigner();
            workerIdAssigner.setZkAddress(uidProperties.getZookeeper().getZkAddress());
            return workerIdAssigner;
        }else if(uidProperties.getRedis().isEnabled()){
            RedisWorkIdAssigner redisWorkIdAssigner = new RedisWorkIdAssigner();
            return redisWorkIdAssigner;
        }else if(uidProperties.getSimple().isEnabled()){
            SimpleWorkerIdAssigner simpleWorkerIdAssigner = new SimpleWorkerIdAssigner();
            return simpleWorkerIdAssigner;
        }else {
            DisposableWorkerIdAssigner disposableWorkerIdAssigner = new DisposableWorkerIdAssigner();
            return disposableWorkerIdAssigner;
        }
    }


    /***********************************************workerId提供策略**************************************************/
    @Bean
    public WorkerIdAssigner workerIdAssigner(){
        if(uidProperties.getZookeeper().isEnabled()){
            ZkWorkerIdAssigner workerIdAssigner = new ZkWorkerIdAssigner();
            workerIdAssigner.setZkAddress(uidProperties.getZookeeper().getZkAddress());
            return workerIdAssigner;
        }else if(uidProperties.getRedis().isEnabled()){
            RedisWorkIdAssigner redisWorkIdAssigner = new RedisWorkIdAssigner();
            return redisWorkIdAssigner;
        }else if(uidProperties.getSimple().isEnabled()){
            SimpleWorkerIdAssigner simpleWorkerIdAssigner = new SimpleWorkerIdAssigner();
            return simpleWorkerIdAssigner;
        }else {
            DisposableWorkerIdAssigner disposableWorkerIdAssigner = new DisposableWorkerIdAssigner();
            return disposableWorkerIdAssigner;
        }
    }

    ///**
    // * 利用zookeeper来实现wordId的提供管理
    // * 可设置interval-心跳间隔、pidHome-workerId文件存储目录、zkAddress-zk地址、pidPort-心跳端口
    // */
    //@Bean
    //@ConditionalOnProperty(value = "pzy.uid.zookeeper.enabled",havingValue = "true")
    //public ZkWorkerIdAssigner zkWorkerIdAssigner(){
    //    ZkWorkerIdAssigner zkWorkerIdAssigner = new ZkWorkerIdAssigner();
    //    zkWorkerIdAssigner.setZkAddress(uidProperties.getZookeeper().getZkAddress());
    //    return zkWorkerIdAssigner;
    //}
    ///**
    // * 利用redis来实现wordId的提供管理
    // * 可设置interval-心跳间隔、pidHome-workerId文件存储目录、pidPort-心跳端口
    // */
    //@Bean
    //@ConditionalOnProperty(value = "pzy.uid.redis.enabled",havingValue = "true")
    //public RedisWorkIdAssigner redisWorkIdAssigner(){
    //    RedisWorkIdAssigner redisWorkIdAssigner = new RedisWorkIdAssigner();
    //    return redisWorkIdAssigner;
    //}
    ///**
    // * 利用数据库来管理生成workId
    // */
    //@Bean
    //@ConditionalOnProperty(value = "pzy.uid.def.enabled",havingValue = "true")
    //public DisposableWorkerIdAssigner disposableWorkerIdAssigner(){
    //    DisposableWorkerIdAssigner disposableWorkerIdAssigner = new DisposableWorkerIdAssigner();
    //    return disposableWorkerIdAssigner;
    //}
    ///**
    // * 固定了workId的提供: 值为0
    // */
    //@Bean
    //@ConditionalOnProperty(value = "pzy.uid.simple.enabled",havingValue = "true")
    //public SimpleWorkerIdAssigner simpleWorkerIdAssigner(){
    //    SimpleWorkerIdAssigner simpleWorkerIdAssigner = new SimpleWorkerIdAssigner();
    //    return simpleWorkerIdAssigner;
    //}
    /***********************************************workerId提供策略**************************************************/


 /***********************************************uid策略**************************************************/
    /**
     * 百度策略
     */
    @Bean
    @ConditionalOnProperty(value = "pzy.uid.strategy",havingValue = "baidu")
    public BaiduUidStrategy baiduUidStrategy(){
        BaiduUidStrategy baiduUidStrategy = new BaiduUidStrategy();
        return baiduUidStrategy;
    }
    /**
     * 美团策略
     */
    @Bean
    @ConditionalOnProperty(value = "pzy.uid.strategy",havingValue = "segment")
    public LeafSegmentStrategy leafSegmentStrategy(){
        LeafSegmentStrategy leafSegmentStrategy = new LeafSegmentStrategy();
        return leafSegmentStrategy;
    }
    /**
     * Twitter策略
     */
    @Bean
    @ConditionalOnProperty(value = "pzy.uid.strategy",havingValue = "snowflake")
    public TwitterSnowflakeStrategy twitterSnowflakeStrategy(){
        TwitterSnowflakeStrategy twitterSnowflakeStrategy = new TwitterSnowflakeStrategy();
        return twitterSnowflakeStrategy;
    }
    /**
     * Spring策略
     */
    @Bean
    @ConditionalOnProperty(value = "pzy.uid.strategy",havingValue = "step")
    public SpringStrategy springStrategy(){
        SpringStrategy springStrategy = new SpringStrategy();
        return springStrategy;
    }
    /***********************************************uid策略**************************************************/

    /***********************************************uid生成策略**************************************************/
    /**
     * 借用未来时间来解决sequence天然存在的并发限制
     * 采用RingBuffer来缓存已生成的UID, 并行化UID的生产和消费,同时对CacheLine补齐，避免了由RingBuffer带来的硬件级「伪共享」问题. 最终单机QPS可达600万
     */
    @Bean
    @ConditionalOnProperty(value = "pzy.uid.generator",havingValue = UidProperties.CACHED_GENERATOR)
    public CachedUidGenerator cachedUidGenerator(){
        CachedUidGenerator cachedUidGenerator = new CachedUidGenerator();
        cachedUidGenerator.setWorkerIdAssigner(workerIdAssigner());
        return cachedUidGenerator;
    }
    /**
     * Snowflake算法的变种: 扩展了支持自定义workerId位数和初始化策略
     * @return
     */
    @Bean
    @ConditionalOnProperty(value = "pzy.uid.generator",havingValue = UidProperties.DEFAULT_GENERATOR)
    public DefaultUidGenerator defaultUidGenerator(){
        DefaultUidGenerator defaultUidGenerator = new DefaultUidGenerator();
        defaultUidGenerator.setWorkerIdAssigner(workerIdAssigner());
        return defaultUidGenerator;
    }

    @Bean
    public UidGenerator uidGenerator(){
        String generator = uidProperties.getGenerator();
        if(UidProperties.DEFAULT_GENERATOR.equals(generator)){
            CachedUidGenerator cachedUidGenerator = new CachedUidGenerator();
            cachedUidGenerator.setWorkerIdAssigner(workerIdAssigner());
            return cachedUidGenerator;
        }else {
            DefaultUidGenerator defaultUidGenerator = new DefaultUidGenerator();
            defaultUidGenerator.setWorkerIdAssigner(workerIdAssigner());
            return defaultUidGenerator;
        }
    }
    /***********************************************uid生成策略**************************************************/

    /***********************************************Uid策略上下文**************************************************/
    /**
     * 默认使用：baidu策略
     */
    @Bean
    public UidContext uidContext(){
        String strategy = uidProperties.getStrategy();
        if(UidModel.snowflake.getName().equals(strategy)){
            return new UidContext(new TwitterSnowflakeStrategy());
        }else if(UidModel.segment.getName().equals(strategy)){
            return new UidContext(new LeafSegmentStrategy());
        }else if(UidModel.step.getName().equals(strategy)){
            return new UidContext(new SpringStrategy());
        }else {
            return new UidContext(new BaiduUidStrategy());
        }
    }
    /***********************************************Uid策略上下文**************************************************/

    @Bean
    public SegmentServiceImpl segmentService(){
        SegmentServiceImpl segmentService = new SegmentServiceImpl(jdbcTemplate,"order");
        segmentService.setAsynLoadingSegment(true);
        return segmentService;
    }

    @Bean
    public ColumnMaxValueIncrementer productNoIncrementer(){
        ColumnMaxValueIncrementer columnMaxValueIncrementer = new ColumnMaxValueIncrementer(jdbcTemplate,"productNo");
        columnMaxValueIncrementer.setPaddingLength(6);
        return columnMaxValueIncrementer;
    }
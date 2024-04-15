package com.pengzexuan.uga.core.config;

import com.pengzexuan.uga.common.constants.UgaBasicConstant;
import com.pengzexuan.uga.common.constants.UgaBufferHelper;
import com.pengzexuan.uga.common.utils.NetUtils;
import lombok.Data;

import java.util.UUID;


@Data
public class UgaConfiguration {

    /**
     * Uga's default port
     *
     * <p>Default Server Port is 8888</p>
     */
    private Integer port = 8888;

    /**
     * Gateway Service Unique ID
     *
     * <p>Random UUID + $ + local Ip + : + Server Port</p>
     */
    private String ugaId = UUID.randomUUID() +  UgaBasicConstant.DOLLAR_SEPARATOR + NetUtils.getLocalIp() + UgaBasicConstant.COLON_SEPARATOR + port;

    /**
     * The registration center address of the gateway
     *
     * <p>Default is 127.0.0.1:2379 for the etcd</p>
     */
    private String registrationCenterAddressList = "127.0.0.1:2379";

    /**
     * The namespace of the gateway
     *
     * <p>Default NameSpace is default-namespace</p>
     */
    private String nameSpace = "default-namespace";

    /**
     * The number of CPU cores mapped to threads in the gateway server
     */
    private Integer processThread = Runtime.getRuntime().availableProcessors();

    /**
     * The Number of Netty's Boss threads
     *
     * <p>Default is 1</p>
     */
    private Integer eventLoopGroupBossNum = 1;

    /**
     * The Number of Work threads in Netty
     *
     * <p>Default processThread</p>
     */
    private Integer eventLoopGroupWorkNum = processThread;

    /**
     * Is EPOLL enabled
     *
     * <p>Default is Boolean.TRUE</p>
     */
    private Boolean useEPoll = Boolean.TRUE;

    /**
     * Is Netty memory allocation mechanism enabled
     *
     * <p>Default is Boolean.TRUE</p>
     */
    private Boolean nettyAllocator = Boolean.TRUE;

    /**
     * Maximum size of HTTP body message
     *
     * <p>Default is 64MB</p>
     */
    private Integer maxContentLength = 64 * 1024 * 1024;

    /**
     * Number of Dubbo connections opened
     *
     * <p>Default is equals processThread</p>
     */
    private Integer dubboConnections = processThread;


    /**
     *  Set response mode, default to single asynchronous mode: CompletableFuture callback processing result: whenComplete or whenCompleteAsync
     *
     * <p>Default is Boolean.TRUE</p>
     */
    private Boolean whenComplete = Boolean.TRUE;

    /**
     * Gateway queue configuration: buffering mode;
     *
     * <p>Default is FLUSHER</p>
     * @see com.pengzexuan.uga.common.constants.UgaBufferHelper
     */
    private String bufferType = UgaBufferHelper.FLUSHER;

    /**
     * Gateway queue: Memory queue size
     *
     * <p>Default is 16KB</p>
     */
    private Integer bufferSize = 1024 * 16;


    /**
     * Gateway queue: blocking/waiting strategy
     *
     * <p>Default is blocking</p>
     */
    private String waitStrategy = "blocking";


    /**
     * Connection timeout
     *
     * <p>Default is 3000ms</p>
     */
    private Integer httpConnectTimeout = 30 * 1000;

    /**
     * Request timeout
     *
     * <p>Default is 3000ms</p>
     */
    private Integer httpRequestTimeout = 30 * 1000;

    /**
     * Number of client request retries
     *
     * <p>Default is 2 times</p>
     */
    private Integer httpMaxRequestRetry = 2;

    /**
     * Maximum number of client requests for connections
     *
     * <p>Default is 10000<p/>
     */
    private Integer httpMaxConnections = 10000;


    /**
     * The maximum number of connections supported by each address of the client
     *
     * <p>Default is 8000</p>
     */
    private Integer httpConnectionsPerHost = 8000;


    /**
     * Client idle connection timeout
     *
     * <p>Default is 6000ms</p>
     */
    private Integer httpPooledConnectionIdleTimeout = 60 * 1000;
}

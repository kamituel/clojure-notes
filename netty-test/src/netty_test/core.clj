(ns netty-test.core
  (:import (io.netty buffer.ByteBuf
                     channel.ChannelHandler
                     channel.ChannelHandlerContext
                     channel.ChannelHandlerAdapter
                     channel.ChannelInitializer
                     channel.ChannelOption
                     channel.ChannelFutureListener
                     channel.nio.NioEventLoopGroup
                     channel.socket.nio.NioSocketChannel
                     channel.socket.nio.NioServerSocketChannel
                     util.CharsetUtil
                     bootstrap.Bootstrap
                     bootstrap.ServerBootstrap)
           java.util.Date)
  (:require clojure.reflect)
  (:use [clojure.pprint :only [pprint]]))

(defn- make-echo-server-handler
  "Echo server."
  []
  (proxy [ChannelHandlerAdapter] []
                      (channelRead [ctx msg]
                                   (println "Incoming data: " (.toString msg CharsetUtil/US_ASCII))
                                   (doto ctx (.write msg) .flush)
                                   (println "responded."))
                      (exceptionCaught [ctx cause]
                                       (.printStackTrace cause)
                                       (.close ctx))))

(defn- timestamp-to-time
  "Converts unix timestamp time to TIME protocol value."
  [x]
  (+ (int (/ x 1000)) 2208988800))

(defn- time-to-timestamp
  "Converts TIME protocol value to unix timestamp."
  [x]
  (* (- x 2208988800) 1000))

(defn- make-time-server-handler
  "TIME protocol server."
  []
  (proxy [ChannelHandlerAdapter] []
    (channelActive [ctx]
                   (let [time-buf (-> ctx .alloc (.buffer 4))
                         time-now (timestamp-to-time (System/currentTimeMillis))
                         _ (.writeInt time-buf time-now)
                         f (.writeAndFlush ctx time-buf)]
                     (println "Sending " time-now)
                     (.addListener f (reify ChannelFutureListener
                                       (operationComplete [this f]
                                                          (.close ctx))))))))

(defn- make-time-client-handler
  "TIME protocol client."
  []
  (proxy [ChannelHandlerAdapter] []
    (channelRead [ctx msg]
                 (let [received (.readUnsignedInt msg)
                       time-now (time-to-timestamp received)]
                   (println "received " received " time: " (Date. time-now)))
                 (.close ctx))
    (exceptionCaught [ctx cause]
                     (.printStackTrace cause)
                     (.close ctx))))

(defn- make-initializer [handler]
  (proxy [ChannelInitializer] []
    (initChannel [socket-channel] (-> socket-channel
                                      .pipeline
                                      (.addLast (into-array ChannelHandlerAdapter [(handler)]))))))

(defn- run-server [handler port]
  (let [boss-group (NioEventLoopGroup.)
        worker-group (NioEventLoopGroup.)
        b (ServerBootstrap.)]
    (doto b
      (.group boss-group worker-group)
      (.channel NioServerSocketChannel)
      (.childHandler (make-initializer handler))
      (.option ChannelOption/SO_BACKLOG (int 128))
      (.childOption ChannelOption/SO_KEEPALIVE true))
    (-> b
        (.bind (int port))
        .sync)))

(defn- run-client [handler {:keys [host port]}]
  (let [worker-group (NioEventLoopGroup.)
        b (Bootstrap.)]
    (doto b
      (.group worker-group)
      (.channel NioSocketChannel)
      (.option ChannelOption/SO_KEEPALIVE true)
      (.handler (make-initializer handler)))
    (-> b
        (.connect host port)
        .sync)))

(defn -main
  [cmd]

  (cond
   (= cmd "echo-server") (run-server make-echo-server-handler 6000)
   (= cmd "time-server") (run-server make-time-server-handler 6000)
   (= cmd "time-client") (run-client make-time-client-handler {:host "127.0.0.1" :port 6000}))

  )
